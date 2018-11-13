package ch.epfl.sweng.swenggolf.location;

import android.app.Activity;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static ch.epfl.sweng.swenggolf.Config.PERMISSION_FINE_LOCATION;

public abstract class AppLocation {

    private static AppLocation location = null;

    /**
     * Use the given AppLocation as the return of the getInstance() method.
     *
     * @param l the fake AppLocation
     */
    public static void setDebugLocation(AppLocation l) {
        location = l;
    }

    /**
     * Returns the singleton instance of the AppLocation. Return a fake AppLocation if given.
     *
     * @param activity the activity in which it was called
     * @return the AppLocation
     */
    public static AppLocation getInstance(Activity activity) {
        if (location == null) {
            location = new ConcreteLocation(activity);
        }
        return location;
    }

    /**
     * Fetches the location from Android and outputs it in the given listener.
     *
     * @param listener the listener which handles a case of success
     */
    public abstract void getLocation(@NonNull OnSuccessListener<Location> listener);

    /**
     * Checks if the app is allowed to check the location. If not, requests it to Android.
     *
     * @param activity the current activity in which we ask for the permission
     * @return whether we are authorized or not
     */
    public static boolean checkLocationPermission(Activity activity) {
        if (checkSelfPermission(activity, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
            return false;
        }
        return true;
    }
}