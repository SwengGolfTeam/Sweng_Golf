package ch.epfl.sweng.swenggolf.location;

import android.app.Activity;
import android.location.Location;

import com.google.android.gms.tasks.OnSuccessListener;

public abstract class AppLocation {

    private static AppLocation location = null;

    public static void setDebugLocation(AppLocation l) {
        location = l;
    }

    public static AppLocation getInstance(Activity activity) {
        if (location == null) {
            location = new ConcreteLocation(activity);
        }
        return location;
    }

    public abstract void getLocation(OnSuccessListener<Location> listener);
}
