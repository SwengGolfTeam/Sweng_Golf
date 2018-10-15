package ch.epfl.sweng.swenggolf;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

/**
 * Class which represents a FirebaseUser.
 */
public class UserFirebase {

    private final FirebaseUser fu;

    public UserFirebase(FirebaseUser fu){
        this.fu = fu;
    }

    public String getEmail() {
        return fu.getEmail();
    }

    public String getUserName() {
        return fu.getDisplayName();
    }

    public String getUserId() {
        return fu.getUid();
    }

    public Uri getPhoto() {
        return fu.getPhotoUrl();
    }
}
