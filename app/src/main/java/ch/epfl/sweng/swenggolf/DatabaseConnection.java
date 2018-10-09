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

    public DatabaseConnection(){
        db = FirebaseDatabase.getInstance();
        ref = db.getReference();
    }

    /**
     * Writes a new offer in the database
     * @param offerID an ID unique to this offer
     * @param newOffer the new Offer that needs to be added
     *
     */
    public void writeOffer(String offerID, Offer newOffer){
        ref = db.getReference();
        ref.child("offers").child(offerID).setValue(newOffer);
        Log.d("DB_WRITE", "offerID="+offerID);
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
                List<Offer> offers = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Offer offer = noteDataSnapshot.getValue(Offer.class);
                    offers.add(offer);
                    Log.d("DB_READ", "offer read: "+offer.getTitle());
                }

                //TODO: call to display/handle function for offers list
                //ListOfferAdapter loa = new ListOfferAdapter(offers);
                //loa.notifyDataSetChanged();
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
                Log.d("DB_READ", "offer read: "+offer.getTitle());

                //TODO: call to display/handle function for offer
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DB_READ", "failed offerID="+offerID);
            }
        });
    }

    /**
     * Writes a user in the database
     * @param userID an ID unique to this user
     * @param newUser the new User that needs to be added
     *
     */
    public void writeUser(String userID, User newUser){
        ref = db.getReference();
        ref.child("users").child(userID).setValue(newUser);
        Log.d("DB_WRITE", "userID="+userID);

    }

    /**
     * Reads a specific user from the database
     * @param userID the ID of the user
     *
     */
    public void readUser(final String userID){
        ref = db.getReference("/users/"+userID);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.d("DB_READ", "user read: "+user.getUsername());

                //TODO: call to display/handle function for user
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DB_READ", "failed userID="+userID);
            }
        });
    }
}