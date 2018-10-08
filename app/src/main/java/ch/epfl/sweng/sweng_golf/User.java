package ch.epfl.sweng.sweng_golf;

// Just a temporary placeholder class in order to complete the Firebase Implementation
public class User {
    private final String username;
    private final String userID;
    private final String login;

    // Empty constructor for the listeners of Firebase
    public User(){
        this.username = "";
        this.userID = "";
        this.login = "";
    }

    public User(String username, String userID, String login){
        this.username = username;
        this.userID = userID;
        this.login = login;
    }

    public String getUserID(){
        return this.userID;
    }

    public String getUsername(){
        return this.username;
    }

    public String getLogin(){
        return this.login;
    }
}
