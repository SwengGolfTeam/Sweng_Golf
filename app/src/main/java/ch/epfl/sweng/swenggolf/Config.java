package ch.epfl.sweng.swenggolf;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Class Used to know if we are running a test or not.
 */
public class Config {

    public static final int PERMISSION_FINE_LOCATION = 66;

    private Config() {
    }

    /**
     * onTest must be true for tests and false otherwise.
     */
    private static boolean onTest = false;

    /**
     * The ActivityCallback set before.
     */
    private static ActivityCallback activityCallback = null;


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
        if (user == null) {
            return isTest() ? new User() : new User(FirebaseAuth.getInstance().getCurrentUser());
        } else {
            return user;
        }
    }

    /**
     * Set the actual user.
     *
     * @param newUser user to copy in the static one
     */
    public static void setUser(User newUser) {
        user = newUser;
    }

    /**
     * Set the ActivityCallback.
     *
     * @param activityCallback the ActivityCallback to set
     */
    public static void setActivityCallback(ActivityCallback activityCallback) {
        Config.activityCallback = activityCallback;
    }

    /**
     * Reset The ActivityCallback.
     */
    public static void resetActivityCallback() {
        Config.activityCallback = new ActivityCallback() {
            @Override
            public void isDone() {
                //Do nothing because we want to reset it!
            }
        };
    }

    /**
     * Get the ActivityCallback.
     */
    public static ActivityCallback getActivityCallback() {
        if (Config.activityCallback == null || !Config.isTest()) {
            Config.resetActivityCallback();
        }
        return Config.activityCallback;
    }

    public static boolean checkLocationPermission(Activity activity) {
        if (isAuthorizedToCheckLocation(activity)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_FINE_LOCATION);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return false;
        }
        return true;
    }

    private static boolean isAuthorizedToCheckLocation(Activity activity) {
        boolean isFineLocationGranted = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED;
        boolean isCoarseLocationGranted = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED;
        return isFineLocationGranted && isCoarseLocationGranted;

    }
}
