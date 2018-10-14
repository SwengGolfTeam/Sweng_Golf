package ch.epfl.sweng.swenggolf;

import android.net.Uri;

/**
 * Class which represents an User.
 */
public interface User {

    /**
     * Get the User mail.
     * @return the corresponding mail
     */
    public String getEmail();


    /**
     * Get the User name.
     * @return the corresponding name
     */
    public String getUserName();

    /**
     * Get the User id.
     * @return the corresponding id
     */
    public String getUserId();

    /**
     * Get the User photo.
     * @return the corresponding photo URI
     */
    public String getPhoto();
}
