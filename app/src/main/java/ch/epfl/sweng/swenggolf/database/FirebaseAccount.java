package ch.epfl.sweng.swenggolf.database;

import android.net.Uri;

public class FirebaseAccount {

    private final Uri photoUrl;
    private final String userId;
    private final String username;
    public static boolean test = false;

    /**
     * Creates a new account representing a user data.
     * If test is set to true returns a user with initialized attributes.
     * Otherwise returns a user with empty attributes.
     */
    public FirebaseAccount() {
        if (test) {
            photoUrl = Uri.parse("http://www.waytoblue.com/wp-content/uploads/2015/04/Care-Bear-4.png");
            userId = "userIdValid";
            username = "usernameValid";
        } else {
            photoUrl = Uri.EMPTY;
            userId = "";
            username = "";
        }
    }

    public String getId() {
        return userId;
    }

    public String getName() {
        return username;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public static FirebaseAccount getCurrentUserAccount() {
        return new FirebaseAccount();
    }

}
