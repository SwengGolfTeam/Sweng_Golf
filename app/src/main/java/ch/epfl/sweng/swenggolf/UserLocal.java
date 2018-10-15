package ch.epfl.sweng.swenggolf;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

// Just a temporary placeholder class in order to complete the Firebase Implementation
public class UserLocal implements User{

    private  String userName;
    private  String userId;
    private  String email;
    private  String photo;




    /**
     * Construct an empty local user.
     * It is actually used for json read user.
     */
    private UserLocal(){
        userName = "";
        email = "";
        userId = "";
        photo = "";
    }


    /**
     * Construct a local user from FirebaseUser.
     * @param fu the FirebaseUser
     */
    public UserLocal(FirebaseUser fu){
        userName = fu.getDisplayName();
        email = fu.getEmail();
        userId = fu.getUid();
        photo = fu.getPhotoUrl().toString();
    }

    /**
     * Constructor for a user.
     * @param username the username
     * @param userId a unique identifier
     * @param email the login method
     * @param photo user photo
     */
    public UserLocal(String username, String userId, String email, String photo){
        if (username.isEmpty() || userId.isEmpty() || email.isEmpty() || photo == null) {
            throw new IllegalArgumentException("Invalid arguments for UserLocal");
        }
        this.userName = username;
        this.userId = userId;
        this.email = email;
        this.photo = photo;
    }



    /**
     * Create an user with an existed user but with different name and different mail.
     * @param user the original user
     * @param username the to be changed name
     * @param email the to be changed email
     * @return the changed user
     */
    public static UserLocal userChanged(User user, String username, String email){
        return new UserLocal(username, user.getUserId(),email, user.getPhoto());
    }

    @Override
    public String getUserId(){
        return this.userId;
    }

    @Override
    public String getUserName(){
        return this.userName;
    }

    @Override
    public String getEmail(){
        return this.email;
    }

    @Override
    public String getPhoto() {
        return photo;
    }


    /**
     * Set the userName.
     * @param userName the corresponding username
     */
    private void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Set the user ID.
     * @param userId the corresponding user ID
     */
    private void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Set the user email.
     * @param email the corresponding user email
     */
    private void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set the user photo.
     * @param photo the corresponding photo
     */
    private void setPhoto(String photo) {
        this.photo = photo;
    }

}
