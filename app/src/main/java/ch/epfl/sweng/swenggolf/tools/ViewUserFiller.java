package ch.epfl.sweng.swenggolf.tools;

import android.util.Log;
import android.widget.TextView;

import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.profile.User;

/**
 * Tool class which enables to fill a view with data.
 */
public abstract class ViewUserFiller {

    private ViewUserFiller() {
    }

    /**
     * Fills a TextView with the username of a given user.
     * This is done asynchronously.
     *
     * @param view   the TextView to fill.
     * @param userId the user to retrieve the username from.
     */
    public static void fillWithUsername(final TextView view, String userId) {
        DatabaseUser.getUser(new ValueListener<User>() {
            @Override
            public void onDataChange(User value) {
                view.setText(value == null ? "" : value.getUserName());
            }

            @Override
            public void onCancelled(DbError error) {
                Log.d(error.toString(), "Failed to load user name");
            }
        }, userId);
    }
}
