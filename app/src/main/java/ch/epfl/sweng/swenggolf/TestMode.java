package ch.epfl.sweng.swenggolf;

/**
 * Class Used to know if we are running a test or not.
 */
public class TestMode {

    private TestMode(){}

    /*
    *   TEST_MODE must be true for tests and false otherwise.
    */
    private static boolean TEST_MODE = false;

    /*
     *   user is used to mock a UserFirebase during a test.
     */
    private static User user = null;

    /**
     * Method to know if we are in test or not.
     * @return the corresponding boolean
     */
    protected static boolean isTest(){
        return TEST_MODE;
    }

    /**
     * Method used to begin a Test.
     */
    protected static void goToTest(){
        TEST_MODE = true;
    }

    /**
     * Method to get the UserFirebase.
     * @return the corresponding UserFirebase
     */
    protected static User getUser(){
        return user;
    }

    /**
     * Method used to set a mocked firebaseUser.
     */
    protected static void setUser(User user){
        TestMode.user = user;
    }



}
