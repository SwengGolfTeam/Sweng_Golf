package ch.epfl.sweng.swenggolf;

import com.google.firebase.database.DatabaseReference;

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
    public boolean containsUser(User user) {
        return true;
    }

}
