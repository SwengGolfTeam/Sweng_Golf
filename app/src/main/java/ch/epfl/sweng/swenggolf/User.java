package ch.epfl.sweng.swenggolf;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

public final class User {

    private final String name;
    private final String mail;
    private final String uid;
    private final Uri photo;

    User(FirebaseUser fu){
        name = fu.getDisplayName();
        mail = fu.getEmail();
        uid = fu.getUid();
        photo = fu.getPhotoUrl();
    }
    
    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getUid() {
        return uid;
    }

    public Uri getPhoto() {
        return photo;
    }

}
