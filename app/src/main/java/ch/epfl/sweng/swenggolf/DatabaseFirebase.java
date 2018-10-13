package ch.epfl.sweng.swenggolf;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DatabaseFirebase extends Database {

    private final DatabaseReference myRef;


    public DatabaseFirebase(DatabaseReference myRef) {
        this.myRef = myRef;
    }



    @Override
    public void addUser(User user) {
        DatabaseReference tmpRef = myRef.child("users").child(user.getUserId());
        tmpRef.child("email").setValue(user.getEmail());
        tmpRef.child("login").setValue("Google");
        tmpRef.child("photoUrl").setValue(user.getPhoto().toString());
        tmpRef.child("userId").setValue(user.getUserId());
        tmpRef.child("username").setValue(user.getUserName());
    }

    @Override
    public void containsUser(final ExistsOnData listener, User user) {
        listener.onStart();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });

    }

}
