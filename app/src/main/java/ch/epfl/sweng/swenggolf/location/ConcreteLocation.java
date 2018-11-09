package ch.epfl.sweng.swenggolf.location;

import android.app.Activity;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class ConcreteLocation extends AppLocation {

    private final FusedLocationProviderClient location;

    protected ConcreteLocation(Activity activity) {
        this.location = LocationServices.getFusedLocationProviderClient(activity);
    }

    @Override
    public void getLocation(OnSuccessListener<Location> listener) throws SecurityException {
        location.getLastLocation().addOnSuccessListener(listener);
    }

}
