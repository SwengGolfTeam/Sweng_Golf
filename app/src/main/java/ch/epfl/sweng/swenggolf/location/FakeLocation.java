package ch.epfl.sweng.swenggolf.location;

import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Mocked Location for tests purposes.
 */
public class FakeLocation extends AppLocation {

    public static final double LATITUDE = 44.02;
    public static final double LONGITUDE = 1.36;
    private final Location location;

    private FakeLocation(double latitude, double longitude) {
        location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
    }

    /**
     * Create a Fake location with predefined fields.
     *
     * @return the corresponding FakeLocation
     */
    public static FakeLocation fakeLocationCreator() {
        return new FakeLocation(LATITUDE, LONGITUDE);
    }

    @Override
    public void getLocation(@NonNull OnSuccessListener<Location> listener) {
        listener.onSuccess(location);
    }
}
