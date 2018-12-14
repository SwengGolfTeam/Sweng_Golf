package ch.epfl.sweng.swenggolf.database;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.profile.User;

/**
 * Activity which waits Firebase answer after
 * the sign-in.
 */
public class WaitingActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ch.epfl.sweng.swenggolf.R.layout.activity_waiting);
        user = Config.getUser();
        changeActivity();
    }

    /**
     * Switch the actual activity to the MainMenuActivity.
     */
    private void goToMainMenu() {
        startActivity(new Intent(WaitingActivity.this, MainMenuActivity.class));
    }

    /**
     * Switch the actual activity to CreateUserActivity.
     */
    private void goToCreate() {
        startActivity(new Intent(WaitingActivity.this, CreateUserActivity.class));
    }

    /**
     * Choose which activity to launch next.
     */
    public void changeActivity() {
        DatabaseUser.getUser(new ValueListener<User>() {

            @Override
            public void onDataChange(User value) {
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                if (value != null) {
                    Config.setUser((User) value);
                    goToMainMenu();
                }
                Config.getActivityCallback();
            }

            @Override
            public void onCancelled(DbError error) {
                if (error == DbError.DATA_DOES_NOT_EXIST) {
                    goToCreate();
                } else {
                    Toast.makeText(WaitingActivity.this,
                            "Error on Connection: " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, user);
    }

}
