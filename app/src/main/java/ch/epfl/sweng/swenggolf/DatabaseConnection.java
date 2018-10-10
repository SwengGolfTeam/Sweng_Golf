package ch.epfl.sweng.swenggolf;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference.CompletionListener;

import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private final FirebaseDatabase db;
    private DatabaseReference ref;
    private final String dbRead = "FIREBASE_READ";
    private final String dbWrite = "FIREBASE_WRITE";

    public DatabaseConnection(){
        db = FirebaseDatabase.getInstance();
        ref = db.getReference();
    }

    /**
     * Writes a new offer in the database.
     * @param type what we want to write "offers" or "users"
     * @param id the unique identifier for this element
     * @param newObject the element we want to add to the database
     *
     */
    public void writeObject(String type, String id, Object newObject){
        ref = db.getReference();
        ref.child(type).child(id).setValue(newObject);
        Log.d(dbWrite, type+"id="+id);
    }

    public void writeObject(String type, String id, Object newObject, CompletionListener listener){
        DatabaseReference ref = db.getReference();
        ref.child(type).child(id).setValue(newObject, listener);
    }

    /**
     * Reads all the offers that are in the database.
     */
    public void readOffers(ValueEventListener listener){
        ref = db.getReference("/offers");
        ref.addListenerForSingleValueEvent(listener);
    }

    /**
     * Reads a specific offer from the database.
     * @param type the type of element eg "offers" or "users"
     * @param id the identifier of the object
     * @param listener
     */
    public void readObject(@NonNull  final String type, @NonNull final String id,
                           @NonNull  ValueEventListener listener){
        if(type == null || type.isEmpty()){
            throw new IllegalArgumentException("type should have a value");
        }
        else if(id == null || id.isEmpty()){
            throw new IllegalArgumentException("id should have a value");
        }
        else if(listener == null){
            throw new IllegalArgumentException("listener should not be null");
        }

        ref = db.getReference(type+"/"+id);
        ref.addListenerForSingleValueEvent(listener);
    }
}