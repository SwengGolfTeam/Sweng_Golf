package ch.epfl.sweng.swenggolf;

import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ch.epfl.sweng.swenggolf.location.ConcreteLocation;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConcreteLocationTest {

    private static final double lat = 46;
    private static final double lon = 7;

    @Test
    public void givesCorrectLocation() {
        ConcreteLocation l = setupLocation();
        final Location expectedLocation = new Location("");
        expectedLocation.setLatitude(lat);
        expectedLocation.setLongitude(lon);

        l.getLocation(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                assertEquals(expectedLocation.getLatitude(), location.getLatitude(), 1e-10);
                assertEquals(expectedLocation.getLongitude(), location.getLatitude(), 1e-10);
            }
        });
    }

    private ConcreteLocation setupLocation() {
        FusedLocationProviderClient location = mock(FusedLocationProviderClient.class);

        Task<Location> task = mock(Task.class);
        when(location.getLastLocation()).thenReturn(task);

        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                Location l = new Location("");
                l.setLatitude(lat);
                l.setLongitude(lon);

                OnSuccessListener<Location> success = invocation.getArgument(0);

                success.onSuccess(l);

                return null;
            }
        };

        doAnswer(answer).when(task).addOnSuccessListener(any(OnSuccessListener.class));

        return new ConcreteLocation(location);
    }
}
