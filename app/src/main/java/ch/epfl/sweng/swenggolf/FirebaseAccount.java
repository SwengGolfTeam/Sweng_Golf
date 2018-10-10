package ch.epfl.sweng.swenggolf;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAccount {

    private FirebaseUser user;

    public FirebaseAccount(){
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public String getId() {
        return user == null ? null : user.getUid();
    }

    public String getName() {
        return user == null ? null : user.getDisplayName();
    }

    public Uri getPhotoUrl() {
        return user == null ? null : user.getPhotoUrl();
    }

    public static FirebaseAccount getCurrentUserAccount(){
        return new FirebaseAccount();
    }

}
