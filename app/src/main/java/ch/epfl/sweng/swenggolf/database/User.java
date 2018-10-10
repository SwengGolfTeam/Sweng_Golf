package ch.epfl.sweng.swenggolf.database;

import android.net.Uri;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseUser;

// Just a temporary placeholder class in order to complete the Firebase Implementation
public class User {
    private final String username;
    private final String userId;
    private final String email;
    private final Uri photo;

    /**
     * Empty constructor for the listeners of Firebase.
     */
    public User(){
        this.username = "";
        this.userId = "";
        this.email = "";
        this.photo = Uri.parse("");
    }

    public User(FirebaseUser fu){
        username = fu.getDisplayName();
        email = fu.getEmail();
        userId = fu.getUid();
        photo = fu.getPhotoUrl();
    }

    /**
     * Constructor for a user -> will change only placeholder.
     * @param username the usrename
     * @param userId a unique identifier
     * @param email the login method
     */
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public User(String username, String userId, String email){
        if (username.isEmpty() || userId.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid arguments for User");
        }
        this.username = username;
        this.userId = userId;
        this.email = email;
        this.photo = null;

    }

    /**
     * Geter function for the identifier.
     * @return the unique identifier
     */
    public String getUserId(){
        return this.userId;
    }

    /**
     * Getter function for the username.
     * @return the username of the user
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * Getter function for the login method.
     * @return the login method of the user
     */
    public String getEmail(){
        return this.email;
    }

    public Uri getPhoto() {
        return photo;
    }

}
