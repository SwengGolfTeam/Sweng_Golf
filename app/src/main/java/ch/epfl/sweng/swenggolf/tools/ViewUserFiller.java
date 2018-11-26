package ch.epfl.sweng.swenggolf.tools;

import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.profile.User;

/**
 * Tool class which enables to fill a view with data.
 */
public class ViewUserFiller {

    private static Map<String, String> userMap = new HashMap<>();

    private ViewUserFiller() {
    }

    /**
     * Clears the local map to reaload all usernames. Usually called when reloading the list of
     * offers.
     */
    public static void clearMap() {
        userMap.clear();
    }

    /**
     * Fills a TextView with the username of a given user.
     * This is done asynchronously.
     *
     * @param view   the TextView to fill.
     * @param userId the user to retrieve the username from.
     */
    public static void fillWithUsername(final TextView view, final String userId) {
        String userName = userMap.get(userId);

        if (userName == null) {
            DatabaseUser.getUser(new ValueListener<User>() {
                @Override
                public void onDataChange(User value) {
                    String username = value == null ? "" : value.getUserName();
                    view.setText(username);
                    userMap.put(userId, username);
                    Log.d("TEST ADAPTER", username);
                }

                @Override
                public void onCancelled(DbError error) {
                    Log.d(error.toString(), "Failed to load user name");
                }
            }, userId);
        } else {
            view.setText(userName);
        }
    }
}
