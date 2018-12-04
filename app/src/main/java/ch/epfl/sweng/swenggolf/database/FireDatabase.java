package ch.epfl.sweng.swenggolf.database;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.swenggolf.network.Network;
import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.Offer;

import static ch.epfl.sweng.swenggolf.database.DbError.DISCONNECTED;
import static ch.epfl.sweng.swenggolf.database.DbError.NONE;

/**
 * Database which uses Firebase.
 */
public final class FireDatabase extends Database {
    private final FirebaseDatabase database;
    private final Map<Pair<String, ValueListener>, ValueEventListener> listeners;


    protected FireDatabase() {
        database = FirebaseDatabase.getInstance();
        listeners = new HashMap<>();
    }

    /**
     * Set the database to use. This should only be used when testing FireDatabase. If you just
     * want a test Database to use, you should use FakeDatabase instead.
     *
     * @param database the database
     */
    public FireDatabase(FirebaseDatabase database) {
        this.database = database;
        listeners = new HashMap<>();
    }

    @NonNull
    private static <T> ValueEventListener getListValueListener(
            @NonNull final ValueListener<List<T>> listener, @NonNull final Class<T> c) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<T> list = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    list.add(data.getValue(c));
                }
                listener.onDataChange(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onCancelled(DbError.getError(databaseError));
            }
        };
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

    @Override
    public void write(@NonNull String path, @NonNull String id, @NonNull Object object){
        database.getReference(path).child(id).setValue(object);
    }

    @Override
    public void write(@NonNull String path, @NonNull String id, @NonNull Object object,
    @NonNull final CompletionListener listener){
        checkInternetConnection(listener);
        DatabaseReference.CompletionListener firebaseListener = getCompletionListener(listener);
        database.getReference(path).child(id).setValue(object, firebaseListener);
    }

    @Override
    public <T> void read(@NonNull String path, @NonNull String id,
    @NonNull final ValueListener<T> listener, @NonNull final Class<T> c){
        checkInternetConnection(listener);
        DatabaseReference ref = database.getReference(path);
        ref.child(id).addListenerForSingleValueEvent(convertValueListener(listener, c));
    }

    @Override
    public <T> void listen(@NonNull String path, @NonNull String id,
            @NonNull ValueListener < T > listener, @NonNull Class < T > c){
        checkInternetConnection(listener);
        DatabaseReference ref = database.getReference(path);
        ValueEventListener firebaseEquivalent = convertValueListener(listener, c);
        ref.child(id).addValueEventListener(firebaseEquivalent);
        listeners.put(new Pair<String, ValueListener>(path + "/" + id, listener),
                firebaseEquivalent);
    }

    @Override
    public <T> void deafen(@NonNull String path, @NonNull String id,
            @NonNull ValueListener < T > listener){
        checkInternetConnection(listener);
        Pair<String, ValueListener<T>> listenerAtPath = Pair.create(path + "/" + id, listener);
        if (listeners.containsKey(listenerAtPath)) {
            database.getReference(path).child(id)
                    .removeEventListener(listeners.get(listenerAtPath));
            listeners.remove(listenerAtPath);
        }
    }

    private <T> ValueEventListener convertValueListener(
    final ValueListener<T> listener,
    final Class<T> c){

        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                T value = dataSnapshot.getValue(c);
                listener.onDataChange(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onCancelled(DbError.getError(databaseError));
            }
        };
    }

    @Override
    public <T> void readList(@NonNull String path,
    @NonNull final ValueListener<List<T>> listener,
    @NonNull final Class<T> c){
        checkInternetConnection(listener);
        DatabaseReference ref = database.getReference(path);
        ValueEventListener firebaseListener = getListValueListener(listener, c);
        ref.addListenerForSingleValueEvent(firebaseListener);

    }

    @Override
    public <T> void readList(@NonNull String path,
    @NonNull final ValueListener<List<T>> listener,
    @NonNull final Class<T> c, AttributeFilter filter){
        checkInternetConnection(listener);
        final DatabaseReference ref = database.getReference(path);
        Query query = ref.orderByChild(filter.getAttribute()).equalTo(filter.getValue());
        readListQuery(listener, query, c, false);
    }

    @Override
    public <T> void readList(@NonNull String path,
    @NonNull final ValueListener<List<T>> listener,
    @NonNull final Class<T> c, AttributeOrdering ordering){
        checkInternetConnection(listener);
        final DatabaseReference ref = database.getReference(path);
        Query query = ref.orderByChild(ordering.getAttribute());
        if (ordering.isAscending()) {
            query = query.limitToFirst(ordering.getNumberOfElements());
        } else {
            query = query.limitToLast(ordering.getNumberOfElements());
        }
        readListQuery(listener, query, c, ordering.isDescending());
    }

    @Override
    public void remove(@NonNull String path, @NonNull String id,
            @NonNull CompletionListener listener){
        checkInternetConnection(listener);
        DatabaseReference.CompletionListener firebaseListener = getCompletionListener(listener);
        database.getReference(path).child(id).removeValue(firebaseListener);
    }

    @Override
    public void readOffers(@NonNull final ValueListener<List<Offer>> listener,
    final List<Category> categories){
        checkInternetConnection(listener);

        final DatabaseReference ref = database.getReference(OFFERS_PATH);

        if (categories.isEmpty()) {
            listener.onDataChange(new ArrayList<Offer>());
        }
        for (int i = 0; i < categories.size(); ++i) {
            Query query = ref.orderByChild("tag").equalTo(categories.get(i).toString());
            readListQuery(listener, query, Offer.class, false);
        }
    }

    private <T> void readListQuery(
    @NonNull final ValueListener<List<T>> listener, Query query,
    final Class<T> c, final boolean reverserOrder){
        checkInternetConnection(listener);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<T> list = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot offer : dataSnapshot.getChildren()) {
                        list.add(offer.getValue(c));
                    }
                }
                if (reverserOrder) {
                    Collections.reverse(list);
                }
                listener.onDataChange(list); // when no data was found -> return empty list
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onCancelled(DbError.getError(databaseError));
            }
        });
    }

    /**
     * Informs the listener of the failure if there is no internet connection.
     * @param listener a ValueListener
     */
    private void checkInternetConnection(ValueListener listener){
        if (!Network.getStatus()) {
            Log.d("FIREBASE", "no internet connection");
            listener.onCancelled(DISCONNECTED);
        }
    }

    /**
     * Informs the listener of the failure if there is no internet connection.
     * @param listener a CompletionListener
     */
    private void checkInternetConnection(CompletionListener listener){
        if (!Network.getStatus()) {
            Log.d("FIREBASE", "no internet connection");
            listener.onComplete(DISCONNECTED);
        }
    }

}
