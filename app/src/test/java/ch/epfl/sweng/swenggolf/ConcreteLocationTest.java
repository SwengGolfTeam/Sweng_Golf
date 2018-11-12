package ch.epfl.sweng.swenggolf;

import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ch.epfl.sweng.swenggolf.location.ConcreteLocation;
import ch.epfl.sweng.swenggolf.storage.FireStorage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConcreteLocationTest {

    private static final double lat = 46;
    private static final double lon = 7;

    @Test
    public void givesCorrectLocation() {
        ConcreteLocation l = setupLocation();
        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lon);
    }

    private ConcreteLocation setupLocation() {
        FusedLocationProviderClient location = mock(FusedLocationProviderClient.class);

        Task<Location> task = mock(Task.class);
        when(location.getLastLocation()).thenReturn(task);

        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
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
