package ch.epfl.sweng.swenggolf;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

import org.mockito.Mockito;


public class TestHelper {


    private static final String NAME = "Hello";
    private static final String MAIL = "Hello@World.ok";
    private static final String UID = "123456789009876543211234567890";
    private static final String PHOTO_FIREBASE = null;
    private static final String PHOTO = "PHOTO";

    public static FirebaseUser getFirebaseUser() {
        Uri uriphoto = Mockito.mock(Uri.class);
        Mockito.when(uriphoto.toString()).thenReturn(PHOTO_FIREBASE);
        FirebaseUser user = Mockito.mock(FirebaseUser.class);
        Mockito.when(user.getUid()).thenReturn(UID);
        Mockito.when(user.getEmail()).thenReturn(MAIL);
        Mockito.when(user.getDisplayName()).thenReturn(NAME);
        Mockito.when(user.getPhotoUrl()).thenReturn(uriphoto);
        return user;
    }

    public static User getUser(){
        User user = new User(NAME, UID, MAIL, PHOTO);
        return user;
    }

    public static String getPhoto() { return PHOTO; }

    public static String getName() {
        return NAME;
    }

    public static String getMail() {
        return MAIL;
    }

    public static String getUid() {
        return UID;
    }

    public static String getPhotoFirebase() {
        return PHOTO_FIREBASE;
    }
}
