package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_USERID = "ch.epfl.sweng.swenggolf.USERID";
    private final String uid = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FakeUserDatabase.addNewUser("Hervé Bogoss", uid);
    }

    /**
     * Launches the ProfileActivity.
     *
     * @param view the current view
     */
    public void loadProfileActivity(MenuItem item) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(EXTRA_USERID, uid);
        startActivity(intent);
    }

    /**
     * Launches the SigninActivity.
     *
     * @param view the current view
     */
    public void loadSigninActivity(MenuItem item) {
        startActivity(new Intent(this, SignInActivity.class));
    }


    /**
     * Launches the CreateOfferActivity.
     *
     * @param view the current view
     */
    public void loadCreateOfferActivity(MenuItem item) {
        Intent intent = new Intent(this, CreateOfferActivity.class);
        // TODO implement username when login effective
        intent.putExtra("username", "God");
        startActivity(intent);
    }

    /**
     * Launches the ShowOffersActivity.
     *
     * @param view the current view
     */
    public void loadShowOffersActivity(MenuItem item) {
        Intent intent = new Intent(this, ListOfferActivity.class);
        startActivity(intent);
    }

}

