package ch.epfl.sweng.swenggolf.database;

import ch.epfl.sweng.swenggolf.User;

public class DatabaseUser {


    private DatabaseUser() {
    }

    private static Database db = Database.getInstance();


    public static void addUser(User user) {
        db.write("/users", user.getUserId(), user);
    }


    public static void getUser(final ValueListener listener, User user) {
        db.read("/users", user.getUserId(), listener, User.class);
    }
}
