package ch.epfl.sweng.swenggolf;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static ch.epfl.sweng.swenggolf.Permission.GPS;
import static ch.epfl.sweng.swenggolf.Permission.NONE;

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

    /**
     * Parses the answer from Android regarding the permission to give a simple output to the
     * activity.
     *
     * @param requestCode  the request code
     * @param grantResults the results granted
     * @return which Permission was given
     */
    public static Permission onRequestPermissionsResult(int requestCode,
                                                        @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_FINE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PERMISSION_GRANTED) {
                    return GPS;
                }
                return NONE;
            default:
                return NONE;
        }
    }
}
