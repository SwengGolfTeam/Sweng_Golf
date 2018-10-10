package ch.epfl.sweng.swenggolf;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FakeUserDatabase {

    // in an attempt to mimic a database (which might be not at all like this)
    private static Map<String, Map<String, String>> table;

    public FakeUserDatabase() {
        this.table = new HashMap<>();
    }

    /**
     * Adds a new user to the fake database
     * @param name name of the user
     * @param uid a unique identifier
     */
    public void addNewUser(String name, String uid) {
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
