package ch.epfl.sweng.swenggolf.database;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;

import ch.epfl.sweng.swenggolf.DatabaseFirebase;
import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;

public class WaitingActivity extends AppCompatActivity {

    User user;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ch.epfl.sweng.swenggolf.R.layout.activity_waiting);
        user = Config.getUser();
        myRef = Config.getRef();
        changeActivity();
    }

    /**
     * Switch the actual activity to the MainMenuActivity.
     */
    private void goToMainMenu(){
        startActivity(new Intent(WaitingActivity.this, MainMenuActivity.class));
    }

    /**
     * Switch the actual activity to CreateUserActivity.
     */
    private void goToCreate(){
        startActivity(new Intent(WaitingActivity.this, CreateUserActivity.class));
    }

    /**
     * Choose which activity to launch next.
     */
    public void changeActivity(){
        DatabaseFirebase.getUser(new ValueListener() {
            @Override
            public void onDataChange(Object value) {
                if (value != null) {
                    Config.setUser((User) value);
                    goToMainMenu();
                }
                else{
                    goToCreate();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        }, user);
    }

}
