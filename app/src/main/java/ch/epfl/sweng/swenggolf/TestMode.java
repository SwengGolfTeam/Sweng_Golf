package ch.epfl.sweng.swenggolf;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Class Used to know if we are running a test or not.
 */
public class TestMode {

    private TestMode(){}

    /*
    *   TEST_MODE must be true for tests and false otherwise.
    */
    private static boolean onTest = false;

    /*
     *   user is used to mock a UserFirebase during a test.
     */
    private static User user = null;

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
        if (!TestMode.isTest()) {
            user = new UserFirebase(FirebaseAuth.getInstance().getCurrentUser());
        }
        return user;
    }


    /**
     * Method to get the DatabaseReference.
     * @return the corresponding DatabaseReference
     */
    public static DatabaseReference getRef(){
        DatabaseReference myRef;
        if (TestMode.isTest()) {

            myRef = null;
        } else {
            myRef = FirebaseDatabase.getInstance().getReference();
        }
        return myRef;
    }



    /**
     * Method used to set a mocked firebaseUser.
     */
    public static void setUser(User user){
        TestMode.user = user;
    }



}
