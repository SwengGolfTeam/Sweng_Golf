package ch.epfl.sweng.swenggolf;

import com.google.firebase.auth.FirebaseUser;

// Just a temporary placeholder class in order to complete the Firebase Implementation
public class User {

    private String userName;
    private String userId;
    private String email;
    private String photo;


    /**
     * Construct an empty local user.
     * It is actually used for json read user.
     */
    public User() {
        userName = "";
        email = "";
        userId = "";
        photo = "";
    }


    /**
     * Construct a user from FirebaseUser.
     *
     * @param fu the FirebaseUser
     */
    public User(FirebaseUser fu) {
        userName = fu.getDisplayName();
        email = fu.getEmail();
        userId = fu.getUid();
        photo = fu.getPhotoUrl().toString();
    }

    /**
     * Construct a user from another user.
     *
     * @param u the other user
     */
    public User(User u) {
        this(u.userName, u.userId, u.email, u.photo);
    }

    /**
     * Constructor for a user.
     *
     * @param username the username
     * @param userId   a unique identifier
     * @param email    the login method
     * @param photo    user photo
     */
    public User(String username, String userId, String email, String photo) {
        if (username.isEmpty() || userId.isEmpty() || email.isEmpty() || photo == null) {
            throw new IllegalArgumentException("Invalid arguments for User");
        }
        this.userName = username;
        this.userId = userId;
        this.email = email;
        this.photo = photo;
    }


    /**
     * Create an user with an existed user but with different name and different mail.
     *
     * @param user     the original user
     * @param username the to be changed name
     * @param email    the to be changed email
     * @return the changed user
     */
    public static User userChanged(User user, String username, String email) {
        return new User(username, user.getUserId(), email, user.getPhoto());
    }

    /**
     * Get the User id.
     *
     * @return the corresponding id
     */
    public String getUserId() {
        return this.userId;
    }

    /**
     * Get the User name.
     *
     * @return the corresponding name
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Get the User mail.
     *
     * @return the corresponding mail
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Get the User photo.
     *
     * @return the corresponding photo Uri string
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * Set the userName.
     *
     * @param userName the corresponding username
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Set the user ID.
     *
     * @param userId the corresponding user ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Set the user email.
     *
     * @param email the corresponding user email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set the user photo.
     *
     * @param photo the corresponding photo
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean sameAccount(User user){
        return this.userId == user.getUserId();
    }

    @Override
    public boolean equals(Object obj) {

        final User other = (User) obj;

        boolean isTheSame = obj != null
                && obj instanceof User 
                && this.userName.equals(other.userName)
                && this.email.equals(other.email)
                && this.userId.equals(other.userId)
                && this.photo.equals(other.photo);

        return isTheSame;

    }
}
