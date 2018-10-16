package ch.epfl.sweng.swenggolf;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

import org.mockito.Mockito;


public class TestHelper {


    private static final String name = "Hello";
    private static final String mail = "Hello@World.ok";
    private static final String uid = "123456789009876543211234567890";
    private static final String photoFirebase = null;
    private static final String photo = "photo";

    public static FirebaseUser getFirebaseUser() {
        Uri uriphoto = Mockito.mock(Uri.class);
        Mockito.when(uriphoto.toString()).thenReturn(photoFirebase);
        FirebaseUser user = Mockito.mock(FirebaseUser.class);
        Mockito.when(user.getUid()).thenReturn(uid);
        Mockito.when(user.getEmail()).thenReturn(mail);
        Mockito.when(user.getDisplayName()).thenReturn(name);
        Mockito.when(user.getPhotoUrl()).thenReturn(uriphoto);
        return user;
    }

    public static User getUser(){
        User user = new User(name, uid, mail, photo);
        return user;
    }

    public static String getPhoto() { return photo; }

    public static String getName() {
        return name;
    }

    public static String getMail() {
        return mail;
    }

    public static String getUid() {
        return uid;
    }

    public static String getPhotoFirebase() {
        return photoFirebase;
    }
}
