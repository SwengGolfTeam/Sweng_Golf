package ch.epfl.sweng.sweng_golf;

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

    public DatabaseConnection(){
        db = FirebaseDatabase.getInstance("https://sweng-golf.firebaseio.com/");
        ref = db.getReference();
    }

    /**
     * Write a new offer in the database
     * @param offerID an ID unique to this offer
     * @param newOffer the new Offer that needs to be added
     *
     */
    public void writeOffer(String offerID, Offer newOffer){
        ref = db.getReference();
        ref.child("offers").child(offerID).setValue(newOffer);
    }

    /**
     * Reads all the offers that are in the database
     *
     */
    public void readOffers(){
        ref = db.getReference("/offers");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List offers = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Offer offer = noteDataSnapshot.getValue(Offer.class);
                    offers.add(offer);
                    Log.d("DB_READ", "offer read: "+offer.getName());
                }

                //TODO: call to display/handle function for offers list
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DB_READ", "failed all offers");
            }
        });
    }

    /**
     * Reads a specific offer from the database
     * @param offerID the ID of the offer
     *
     */
    public void readOffer(final String offerID){
        ref = db.getReference("/offers/"+offerID);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Offer offer = dataSnapshot.getValue(Offer.class);
                Log.d("DB_READ", "offer read: "+offer.getName());

                //TODO: call to display/handle function for offer
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DB_READ", "failed offerID="+offerID);
            }
        });
    }

    //TODO: add write/read functions for users

    //TODO: add empty constructors for classes to recover from Database (eg Offer, etc...)
}
