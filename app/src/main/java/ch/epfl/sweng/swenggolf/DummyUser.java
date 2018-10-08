package ch.epfl.sweng.swenggolf;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DummyUser { // has all the useful methods of FirebaseUser
    private String name;
    private String uid;

    // in an attempt to mimic a database
    private static Map<String, Map<String, String>> table;

    public DummyUser(String name, String uid) {
        this.name = name;
        this.uid = uid;
        table = new HashMap<>();
        table.put(uid, new HashMap<String, String>());
        table.get(uid).put("offers_posted", "0");
        table.get(uid).put("offers_answered", "0");
    }

    public String getDisplayName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public static String accessTable(String uid, String key) {
        return table.get(uid).get(key);
    }

    public static void setUsername(String uid, String username) {
        table.get(uid).put("username", username);
    }
}
