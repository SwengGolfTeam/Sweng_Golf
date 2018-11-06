package ch.epfl.sweng.swenggolf;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;

public class User implements Parcelable {

    private static final String DEFAULT_PREFERENCE = "";
    public static final String DEFAULT_DESCRIPTION = "Hello, I'm new Here!";
    private String userName;
    private String userId;
    private String email;
    private String photo;
    private String preference;
    private String description;


    /**
     * Construct an empty local user (used for json).
     */
    public User() {
        userName = "";
        email = "";
        userId = "";
        photo = "";
        preference = "";
        description = "";
    }


    /**
     * Construct a  user from FirebaseUser.
     *
     * @param fu the FirebaseUser
     */
    public User(FirebaseUser fu) {
        userName = fu.getDisplayName();
        email = fu.getEmail();
        userId = fu.getUid();
        photo = fu.getPhotoUrl().toString();
        preference = DEFAULT_PREFERENCE;
        description = DEFAULT_DESCRIPTION;
    }

    /**
     * Construct a user from another user.
     *
     * @param u the other user
     */
    public User(User u) {
        this(u.userName, u.userId, u.email, u.photo, u.preference, u.description);
    }

    /**
     * Constructor for a user with empty preference.
     *
     * @param username the username
     * @param userId   a unique identifier
     * @param email    the user mail
     * @param photo    user photo
     */
    public User(String username, String userId, String email, String photo) {
        this(username, userId, email, photo, DEFAULT_PREFERENCE, DEFAULT_DESCRIPTION);
    }

    /**
     * Constructor for a user.
     *
     * @param username   the username
     * @param userId     a unique identifier
     * @param email      the user mail
     * @param photo      the user photo
     * @param preference the user preference
     */
    public User(String username, String userId, String email, String photo, String preference) {
        this(username, userId, email, photo, preference, DEFAULT_DESCRIPTION);
    }

    /**
     * Constructor for a user.
     *
     * @param username   the username
     * @param userId     a unique identifier
     * @param email      the user mail
     * @param photo      the user photo
     * @param preference the user preference
     */
    public User(String username, String userId, String email,
                String photo, String preference, String description) {
        if (username.isEmpty() || userId.isEmpty() || email.isEmpty()
                || photo == null || preference == null) {
            throw new IllegalArgumentException("Invalid arguments for User");
        }
        this.userName = username;
        this.userId = userId;
        this.email = email;
        this.photo = photo;
        this.preference = preference;
        this.description = description;
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
    // TODO: check if need Uri for photo in Ugo's part

    /**
     * Get the User description.
     *
     * @return the corresponding description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the user preference.
     *
     * @return the user preference
     */
    public String getPreference() {
        return preference;
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

    /**
     * Set the user description.
     *
     * @param description the corresponding description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the user preference.
     *
     * @param preference the corresponding preference
     */
    public void setPreference(String preference) {
        this.preference = preference;
    }

    /**
     * Methat which checks if two users have the same login account.
     *
     * @param user the user to compare
     * @return true if they have the same uid, false otherwise
     */
    public boolean sameAccount(User user) {
        return this.userId.equals(user.getUserId());
    }

    /**
     * Methat which checks if two users have the same informations.
     *
     * @param user the user to compare
     * @return true if they have the same info, false otherwise
     */
    public boolean sameInformation(User user) {
        return this.userName.equals(user.userName)
                && this.email.equals(user.email)
                && this.photo.equals(user.photo)
                && this.preference.equals(user.preference)
                && this.description.equals(user.description);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User user = (User) obj;
            return sameAccount(user) && sameInformation(user);
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userName);
        dest.writeString(this.userId);
        dest.writeString(this.email);
        dest.writeString(this.photo);
        dest.writeString(this.preference);
        dest.writeString(this.description);
    }

    /**
     * Create a User from a Parcel.
     *
     * @param in the parcel
     */
    public User(Parcel in) {
        this.userName = in.readString();
        this.userId = in.readString();
        this.email = in.readString();
        this.photo = in.readString();
        this.preference = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}