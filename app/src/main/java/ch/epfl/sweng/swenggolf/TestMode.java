package ch.epfl.sweng.swenggolf;

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
    private static UserFirebase user = null;

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
    public static UserFirebase getUser(){
        return user;
    }

    /**
     * Method used to set a mocked firebaseUser.
     */
    public static void setUser(UserFirebase user){
        TestMode.user = user;
    }



}
