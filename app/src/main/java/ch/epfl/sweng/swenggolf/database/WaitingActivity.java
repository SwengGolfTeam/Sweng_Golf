package ch.epfl.sweng.swenggolf.database;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import ch.epfl.sweng.swenggolf.Database;
import ch.epfl.sweng.swenggolf.DatabaseFirebase;
import ch.epfl.sweng.swenggolf.DataUser;
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
        Database d = new DatabaseFirebase(myRef.child("users").child(user.getUserId()));
        d.containsUser(new DataUser() {
            @Override
            public void onSuccess(Boolean exists, User user) {
                if(exists) {
                    Config.setUser(user);
                    goToMainMenu();
                } else {
                    goToCreate();
                }
            }
            @Override
            public void onFailure() {
                Toast.makeText(WaitingActivity.this, "Cannot reach database", Toast.LENGTH_SHORT).show();
            }
        }, user);
    }

}
