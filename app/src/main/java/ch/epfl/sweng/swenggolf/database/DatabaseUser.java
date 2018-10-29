package ch.epfl.sweng.swenggolf.database;

import ch.epfl.sweng.swenggolf.User;

public class DatabaseUser {

    private DatabaseUser() {
    }




    public static void addUser(User user) {
        Database.getInstance().write("/users", user.getUserId(), user);
    }


    public static void getUser(final ValueListener<User> listener, User user) {
        getUser(listener, user.getUserId());
    }

    public static void getUser(final ValueListener<User> listener, String userId) {
        Database.getInstance().read("/users", userId, listener, User.class);
    }

}
