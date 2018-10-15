package ch.epfl.sweng.swenggolf;

import com.google.firebase.database.DataSnapshot;

public interface UserListener {
    //this is for callbacks
    void onSuccess(Boolean exists, User user);
    void onFailure();
}
