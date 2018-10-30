package ch.epfl.sweng.swenggolf.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import ch.epfl.sweng.swenggolf.User;

public abstract class ProfileUtils {

    public static Fragment createShowProfileWithProfile(User user) {
            ProfileActivity showPro = new ProfileActivity();
            Bundle profileBundle = new Bundle();
            profileBundle.putParcelable("ch.epfl.sweng.swenggolf.user", user);
            showPro.setArguments(profileBundle);
            return showPro;
        }
}
