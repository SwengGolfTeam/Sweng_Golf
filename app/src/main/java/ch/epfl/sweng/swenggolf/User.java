package ch.epfl.sweng.swenggolf;

// Just a temporary placeholder class in order to complete the Firebase Implementation
public class User {
    private final String username;
    private final String userId;
    private final String login;

    /**
     * Empty constructor for the listeners of Firebase.
     */
    public User(){
        this.username = "";
        this.userId = "";
        this.login = "";
    }

    /**
     * Constructor for a user -> will change only placeholder.
     * @param username the usrename
     * @param userId a unique login method
     */
    public User(String username, String userId, String login){
        this.username = username;
        this.userId = userId;
        this.login = login;
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
    public String getLogin(){
        return this.login;
    }
}
