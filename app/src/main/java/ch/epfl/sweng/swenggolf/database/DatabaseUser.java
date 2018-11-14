package ch.epfl.sweng.swenggolf.database;

import android.provider.ContactsContract;

import com.google.android.gms.flags.Singletons;

import ch.epfl.sweng.swenggolf.User;

public class DatabaseUser {

    private DatabaseUser() {
    }

    public static void addUser(User user) {
        Database.getInstance().write(Database.USERS_PATH, user.getUserId(), user);
    }


    public static void getUser(final ValueListener<User> listener, User user) {
        getUser(listener, user.getUserId());
    }

    public static void getUser(final ValueListener<User> listener, String userId) {
        Database.getInstance().read(Database.USERS_PATH, userId, listener, User.class);
    }

    public static void addPoints(final int scoredPoints, final String userId) {
        Database.getInstance().read(Database.USERS_PATH + "/" + userId, "score",
                new ValueListener<Integer>() {
            @Override
            public void onDataChange(Integer value) {
                Database.getInstance().write(Database.USERS_PATH + "/" + userId, "score", value + scoredPoints);
            }

            @Override
            public void onCancelled(DbError error) {
                //TODO what to do when score fails to update ?
            }
        }, Integer.class);
    }
}
