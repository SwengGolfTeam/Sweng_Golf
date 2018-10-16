package ch.epfl.sweng.swenggolf;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

/**
 * Class which represents a FirebaseUser.
 */
public class UserFirebase implements User {

    private final FirebaseUser fu;

    public UserFirebase(FirebaseUser fu) {
        this.fu = fu;
    }

    @Override
    public String getEmail() {
        return fu.getEmail();
    }

    @Override
    public String getUserName() {
        return fu.getDisplayName();
    }

    @Override
    public String getUserId() {
        return fu.getUid();
    }

    @Override
    public Uri getPhoto() {
        return fu.getPhotoUrl();
    }
}
