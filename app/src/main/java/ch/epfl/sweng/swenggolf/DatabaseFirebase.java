package ch.epfl.sweng.swenggolf;

import android.provider.Settings;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class DatabaseFirebase extends Database {

    private final DatabaseReference myRef;


    public DatabaseFirebase(DatabaseReference myRef) {
        this.myRef = myRef;
    }



    @Override
    public void addUser(User user) {
        DatabaseReference tmpRef = myRef.child("users").child(user.getUserId());
        tmpRef.setValue(user);
       // tmpRef.child("login").setValue("Google");
    }

    @Override
    public void containsUser(final DataUser listener, User user) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = Config.getUser();
                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(UserLocal.class);
                }
                listener.onSuccess(dataSnapshot.exists(),user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }
}
