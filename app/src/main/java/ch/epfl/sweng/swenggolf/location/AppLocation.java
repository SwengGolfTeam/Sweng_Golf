package ch.epfl.sweng.swenggolf.location;

import android.app.Activity;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;

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
}
