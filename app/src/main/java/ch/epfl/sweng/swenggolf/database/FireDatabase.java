package ch.epfl.sweng.swenggolf.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static ch.epfl.sweng.swenggolf.database.DatabaseError.NONE;

public final class FireDatabase extends Database {
    private final FirebaseDatabase database;

    protected FireDatabase() {
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public void write(String path, String id, Object object) {
        DatabaseReference ref = database.getReference();
        ref.child(path).child(id).setValue(object);
    }

    @Override
    public void write(String path, String id, Object object, final CompletionListener listener) {
        DatabaseReference ref = database.getReference(path);

        DatabaseReference.CompletionListener firebaseListener =
                new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable com.google.firebase.database.DatabaseError databaseError,
                                   @NonNull DatabaseReference databaseReference) {
                DatabaseError  error = NONE;
                if(databaseError != null){
                    error = DatabaseError.getError(databaseError);
                }
                listener.onComplete(error);
            }
        };
        ref.child(id).setValue(object, firebaseListener);
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
                listener.onCancelled(DatabaseError.getError(databaseError));
            }
        };
        ref.child(id).addListenerForSingleValueEvent(firebaseListener);
    }

    @Override
    public <T> void readList(String path, final ValueListener<List<T>> listener
            , final Class<T> c) {
        DatabaseReference ref = database.getReference(path);
        final ArrayList<T> list = new ArrayList<>();

        ValueEventListener firebaseListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    list.add(data.getValue(c));
                }
                listener.onDataChange(list);
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {
                listener.onCancelled(DatabaseError.getError(databaseError));
            }
        };
        ref.addListenerForSingleValueEvent(firebaseListener);

    }
}
