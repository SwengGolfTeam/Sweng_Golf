package ch.epfl.sweng.swenggolf;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

/**
 * Just a temporary placeholder class in order to complete the Firebase Implementation.
 */
public class UserLocal implements User {
    private final String userName;
    private final String userId;
    private final String email;
    private final Uri photo;
    private final String preference;

    /**
     * Empty constructor for the listeners of Firebase.
     */
    public UserLocal() {
        this.userName = "";
        this.userId = "";
        this.email = "";
        this.photo = null;
        this.preference = "";
    }

    /**
     * Construct a local user from FirebaseUser.
     *
     * @param fu the FirebaseUser
     */
    public UserLocal(FirebaseUser fu) {
        userName = fu.getDisplayName();
        email = fu.getEmail();
        userId = fu.getUid();
        photo = fu.getPhotoUrl();
        preference = "";
    }

    /**
     * Constructor for a user.
     *
     * @param username the username
     * @param userId   a unique identifier
     * @param email    the login method
     * @param photo    user photo
     */
    public UserLocal(String username, String userId, String email, Uri photo, String preference){
        Boolean pho = (photo==null);
        if(TestMode.isTest()) {
            pho = false;
        }
        if (username.isEmpty() || userId.isEmpty() || email.isEmpty() || pho) {
            throw new IllegalArgumentException("Invalid arguments for UserLocal");
        }
        this.userName = username;
        this.userId = userId;
        this.email = email;
        this.photo = photo;
        this.preference = preference;
    }


    /**
     * Constructor for a user with a null photo-> will change only placeholder.
     *
     * @param username the username
     * @param userId   a unique identifier
     * @param email    the login method
     */
    public UserLocal(String username, String userId, String email, String preference){
        if (username.isEmpty() || userId.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid arguments for UserLocal");
        }
        this.userName = username;
        this.userId = userId;
        this.email = email;
        this.photo = null;
        this.preference = preference;
    }

    /**
     * Create a user with null photo and empty preference.
     * @param username the username
     * @param userId a unique identifier
     * @param email the login method
     */
    public UserLocal(String username, String userId, String email){
        this(username,userId,email,"");
    }

    @Override
    public String getUserId() {
        return this.userId;
    }

    @Override
    public String getUserName() {
        return this.userName;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public Uri getPhoto() {
        return photo;
    }

    @Override
    public String getPreference() {
        return preference;
    }
}
