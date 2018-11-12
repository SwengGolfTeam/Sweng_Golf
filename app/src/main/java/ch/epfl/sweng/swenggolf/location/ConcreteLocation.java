package ch.epfl.sweng.swenggolf.location;

import android.app.Activity;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class ConcreteLocation extends AppLocation {

    private final FusedLocationProviderClient location;

    /**
     * Set the database to use. This should only be used when testing ConcreteLocaiton. If you just
     * want a test Database to use, you should use FakeDatabase instead.
     *
     * @param location the database
     */
    public ConcreteLocation(FusedLocationProviderClient location) {
        this.location = location;
    }

    protected ConcreteLocation(Activity activity) {
        this.location = LocationServices.getFusedLocationProviderClient(activity);
    }

    @Override
    public void getLocation(OnSuccessListener<Location> listener) throws SecurityException {
        location.getLastLocation().addOnSuccessListener(listener);
    }

}
