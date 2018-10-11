package ch.epfl.sweng.swenggolf;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestHelper {



    private static final String name = "Hello";
    private static final String mail = "Hello@World.ok";
    private static final String uid = "123456789009876543211234567890";
    private static final Uri photo = null;

    public static FirebaseUser getUser() {
        FirebaseUser user = mock(FirebaseUser.class);
        when(user.getUid()).thenReturn(uid);
        when(user.getEmail()).thenReturn(mail);
        when(user.getDisplayName()).thenReturn(name);
        when(user.getPhotoUrl()).thenReturn(photo);
        return user;
    }

    public static String getName() {
        return name;
    }

    public static String getMail() {
        return mail;
    }

    public static String getUid() {
        return uid;
    }

    public static Uri getPhoto() {
        return photo;
    }
}
