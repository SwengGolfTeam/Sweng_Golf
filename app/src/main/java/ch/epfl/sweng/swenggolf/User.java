package ch.epfl.sweng.swenggolf;

import android.net.Uri;

/**
 * Class which represents an User.
 */
public interface User {

    /**
     * Get the User mail.
     *
     * @return the corresponding mail
     */
    String getEmail();


    /**
     * Get the User name.
     *
     * @return the corresponding name
     */
    String getUserName();

    /**
     * Get the User id.
     *
     * @return the corresponding id
     */
    String getUserId();

    /**
     * Get the User photo.
     *
     * @return the corresponding photo URI
     */
    Uri getPhoto();

    /**
     * Get the user preference.
     * @return the user preference
     */
    String getPreference();
}
