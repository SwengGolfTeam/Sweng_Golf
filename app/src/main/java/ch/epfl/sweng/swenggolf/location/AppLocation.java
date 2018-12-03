package ch.epfl.sweng.swenggolf.location;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static ch.epfl.sweng.swenggolf.Config.PERMISSION_FINE_LOCATION;

/**
 * Abstract class which represents a Location.
 */
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

    public static boolean checkLocationActive(Context context){
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return true;
        } else {
            showGoToSettingsDialog(context);
            return false; // must retry
        }
    }

    /**
     * Display the Alert Dialog for the redirection to settings
     */
    public static void showGoToSettingsDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Location disabled")
                .setMessage("Do you want to go to the settings to enable it?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent GpsIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(GpsIntent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // user cancelled the dialog
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        Dialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Fetches the location from Android and outputs it in the given listener.
     *
     * @param listener the listener which handles a case of success
     */
    public abstract void getLocation(@NonNull OnSuccessListener<Location> listener);
}
