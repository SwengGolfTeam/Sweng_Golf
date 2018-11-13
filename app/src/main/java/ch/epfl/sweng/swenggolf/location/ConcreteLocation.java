package ch.epfl.sweng.swenggolf.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class ConcreteLocation extends AppLocation {

    private static final int GPS_INTERVAL = 10_000;
    private static final int GPS_FASTEST_INTERVAL = 5_000;

    /*
     * Builds the parameters used to fetch the location.
     */
    private static final LocationRequest mLocationRequest = new LocationRequest()
            .setInterval(GPS_INTERVAL).setFastestInterval(GPS_FASTEST_INTERVAL)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    private static final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            // Nothing to do here, done in the background
        }
    };

    private final FusedLocationProviderClient location;

    /**
     * Set the database to use. This should only be used when testing ConcreteLocation. If you just
     * want a test Database to use, you should use FakeDatabase instead.
     *
     * @param location the database
     */
    public ConcreteLocation(FusedLocationProviderClient location) {
        this.location = location;

    }

    @SuppressLint("MissingPermission")
    protected ConcreteLocation(Activity activity) {
        this.location = LocationServices.getFusedLocationProviderClient(activity);
        this.location.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    @Override
    public void getLocation(OnSuccessListener<Location> listener) throws SecurityException {
        location.getLastLocation().addOnSuccessListener(listener);
    }

}
