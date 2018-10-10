package ch.epfl.sweng.swenggolf;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseAccount {
    private final String login;
    private final Uri photoUrl;
    private final String userID;
    private final String username;
    public static boolean test = false;

    public FirebaseAccount(){
        if(test){
            login = "";
            photoUrl = Uri.parse("http://www.waytoblue.com/wp-content/uploads/2015/04/Care-Bear-4.png");
            userID = "userIdValid";
            username = "usernameValid";
        }else{
            login = "";
            photoUrl = Uri.EMPTY;
            userID = "";
            username = "";
        }
    }

    public String getId() {
        return userID;
    }

    public String getName() {
        return username;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public static FirebaseAccount getCurrentUserAccount(){
        return new FirebaseAccount();
    }

}
