package ch.epfl.sweng.swenggolf.tools;

import android.util.Log;
import android.widget.TextView;

import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;

public abstract class ViewUserFiller {

    private ViewUserFiller() {}

    public static void fillWithUsername(final TextView view, String userId) {
        DatabaseUser.getUser(new ValueListener<User>() {
            @Override
            public void onDataChange(User value) {
                view.setText(value.getUserName());
            }

            @Override
            public void onCancelled(DbError error) {
                Log.d(error.toString(),"Failed to load user name");
            }
        },userId);
    }
}
