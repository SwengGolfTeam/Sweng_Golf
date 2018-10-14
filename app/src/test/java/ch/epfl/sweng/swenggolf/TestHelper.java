package ch.epfl.sweng.swenggolf;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

public class TestHelper {



    private static final String name = "Hello";
    private static final String mail = "Hello@World.ok";
    private static final String uid = "123456789009876543211234567890";
    private static final String photo = null;

    public static FirebaseUser getUser() {
        Uri uriphoto = Mockito.mock(Uri.class);
        Mockito.when(uriphoto.toString()).thenReturn(photo);
        FirebaseUser user = Mockito.mock(FirebaseUser.class);
        Mockito.when(user.getUid()).thenReturn(uid);
        Mockito.when(user.getEmail()).thenReturn(mail);
        Mockito.when(user.getDisplayName()).thenReturn(name);
        Mockito.when(user.getPhotoUrl()).thenReturn(uriphoto);
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

    public static String getPhoto() {
        return photo;
    }
}
