package ch.epfl.sweng.swenggolf;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Class Used to know if we are running a test or not.
 */
public class Config {

    private Config(){}

    /*
    *   TEST_MODE must be true for tests and false otherwise.
    */
    private static boolean onTest = false;

    /*
     *   user is used to mock a UserFirebase during a test.
     */
    private static User user = null;

    private static Database db = new DatabaseLocal(new ArrayList<User>());
    /**
     * Method to know if we are in test or not.
     * @return the corresponding boolean
     */
    public static boolean isTest(){
        return onTest;
    }

    /**
     * Method used to begin a Test.
     */
    public static void goToTest(){
        onTest = true;
    }

    /**
     * Method used to quit a Test.
     */
    public   static void quitTest(){
        onTest = false;
    }

    /**
     * Method to get the UserFirebase.
     * @return the corresponding UserFirebase
     */
    public static User getUser(){
        return user == null ? new UserFirebase(FirebaseAuth.getInstance().getCurrentUser()) : user;
    }

    /**
     * Set the actual user.
     * @param newUser user to copy in the static one
     */
    public static void setUser(User newUser){
        user = newUser;
    }

    /**
     * Method to get the DatabaseReference.
     * @return the corresponding DatabaseReference
     */
    public static DatabaseReference getRef(){
        DatabaseReference myRef;
        if (Config.isTest()) {
            myRef = FirebaseDatabase.getInstance().getReference();
        } else {
            myRef = FirebaseDatabase.getInstance().getReference();
        }
        return myRef;
    }


    /**
     * Method to get the DatabaseReference.
     * @return the corresponding DatabaseReference
     */
    public static Database getDatabase(){
        Database dat;
        if (Config.isTest()) {
            dat = db;
        } else {
            dat = new DatabaseFirebase(FirebaseDatabase.getInstance().getReference());
        }
        return dat;
    }

    /**
     * Method used to add an user to a mocked database list of users.
     */
    public static void addUser(User user){
        db.addUser(user);
    }


}
