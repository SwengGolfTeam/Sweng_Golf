package ch.epfl.sweng.swenggolf.database;

import java.util.HashMap;
import java.util.Map;

public abstract class FakeUserDatabase {

    // in an attempt to mimic a database (which might be not at all like this)
    private static final Map<String, Map<String, String>> table = new HashMap<>();

    private FakeUserDatabase() {
    }


    /**
     * Adds a new user to the fake database.
     *
     * @param name name of the user
     * @param uid  a unique identifier
     */
    public static void addNewUser(String name, String uid) {
        table.put(uid, new HashMap<String, String>());
        table.get(uid).put("name", name);
        table.get(uid).put("offers_posted", "0");
        table.get(uid).put("offers_answered", "0");
    }

    public static String accessTable(String uid, String key) {
        return table.get(uid).get(key);
    }

    public static void setUsername(String uid, String username) {
        table.get(uid).put("username", username);
    }

}
