package ch.epfl.sweng.swenggolf;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private final FirebaseDatabase db;
    private DatabaseReference ref;
    private final String DBREAD = "FIREBASE_READ";
    private final String DBWRITE = "FIREBASE_WRITE";

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
        Log.d(DBWRITE, type+"id="+id);
    }

    /**
     * Reads all the offers that are in the database.
     */
    public void readOffers(){
        ref = db.getReference("/offers");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Offer> offers = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Offer offer = noteDataSnapshot.getValue(Offer.class);
                    offers.add(offer);
                    Log.d(DBREAD, "offer read: "+offer.getTitle());
                }

                //TODO: call the display function for the list of offers
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(DBREAD, "failed all offers");
            }
        });
    }

    /**
     * Reads a specific offer from the database.
     * @param type the type of element eg "offers" or "users"
     * @param id the identifier of the object
     *
     */
    public void readObject(final String type, final String id){
        ref = db.getReference(type+"/"+id);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object obj = dataSnapshot.getValue(Object.class);
                Log.d(DBREAD, type+" read: "+obj.toString());

                //TODO: call to display/handle function for offer
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(DBREAD, "failed "+type+"/"+id);
            }
        });
    }
}