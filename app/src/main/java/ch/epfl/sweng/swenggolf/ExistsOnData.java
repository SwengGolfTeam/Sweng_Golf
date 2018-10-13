package ch.epfl.sweng.swenggolf;

import com.google.firebase.database.DataSnapshot;

public interface ExistsOnData {
    //this is for callbacks
    void onSuccess(Boolean exists);
    void onStart();
    void onFailure();
}
