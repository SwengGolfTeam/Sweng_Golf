package ch.epfl.sweng.swenggolf;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Class Used to know if we are running a test or not.
 */
public class Config {

    private Config() {
    }

    /**
     * onTest must be true for tests and false otherwise.
     */
    private static boolean onTest = false;

    /**
     * user is used to mock a Firebase user during a test.
     */
    private static User user = null;

    /**
     * Method to know if we are in test or not.
     *
     * @return the corresponding boolean
     */
    public static boolean isTest() {
        return onTest;
    }

    /**
     * Method used to begin a Test.
     */
    public static void goToTest() {
        onTest = true;
    }

    /**
     * Method used to quit a Test.
     */
    public static void quitTest() {
        onTest = false;
    }

    /**
     * Method to get the Firebase user.
     *
     * @return the corresponding User
     */

    public static User getUser() {
        return user == null ? new User(FirebaseAuth.getInstance().getCurrentUser()) : user;
    }

    /**
     * Set the actual user.
     *
     * @param newUser user to copy in the static one
     */
    public static void setUser(User newUser) {
        user = newUser;
    }
}
