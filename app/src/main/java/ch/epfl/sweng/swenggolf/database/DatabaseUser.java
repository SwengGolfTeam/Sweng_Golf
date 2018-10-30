package ch.epfl.sweng.swenggolf.database;

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

}
