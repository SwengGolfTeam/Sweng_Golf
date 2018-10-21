package ch.epfl.sweng.swenggolf.database;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static ch.epfl.sweng.swenggolf.database.DbError.NONE;

public final class FireDatabase extends Database {
    private final FirebaseDatabase database;

    protected FireDatabase() {
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public void write(String path, String id, Object object) {

        database.getReference().child(path).child(id).setValue(object);
    }

    @Override
    public void write(String path, String id, Object object, final CompletionListener listener) {

        DatabaseReference.CompletionListener firebaseListener = getCompletionListener(listener);
        database.getReference(path).child(id).setValue(object, firebaseListener);
    }


    @Override
    public <T> void read(String path, String id, final ValueListener<T> listener,
                         final Class<T> c) {
        DatabaseReference ref = database.getReference(path);

        ValueEventListener firebaseListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                T value = dataSnapshot.getValue(c);
                listener.onDataChange(value);
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {
                listener.onCancelled(DbError.getError(databaseError));
            }
        };
        ref.child(id).addListenerForSingleValueEvent(firebaseListener);
    }

    @Override
    public <T> void readList(String path, final ValueListener<List<T>> listener, final Class<T> c) {
        DatabaseReference ref = database.getReference(path);
        final ArrayList<T> list = new ArrayList<>();

        ValueEventListener firebaseListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    list.add(data.getValue(c));
                }
                listener.onDataChange(list);
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {
                listener.onCancelled(DbError.getError(databaseError));
            }
        };
        ref.addListenerForSingleValueEvent(firebaseListener);

    }

    @Override
    public void remove(@NonNull String path, @NonNull String id,
                       @NonNull CompletionListener listener) {
        DatabaseReference.CompletionListener firebaseListener = getCompletionListener(listener);
        database.getReference().child(path).child(id).removeValue(firebaseListener);
    }

    @NonNull
    private static DatabaseReference.CompletionListener getCompletionListener(
            final CompletionListener listener) {
        return new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError,
                                   @NonNull DatabaseReference databaseReference) {
                DbError error = NONE;
                if (databaseError != null) {
                    error = DbError.getError(databaseError);
                }
                listener.onComplete(error);
            }
        };
    }

}
