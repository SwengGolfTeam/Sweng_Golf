package ch.epfl.sweng.swenggolf.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ch.epfl.sweng.swenggolf.database.CreateUserActivity;
import ch.epfl.sweng.swenggolf.database.FakeUserDatabase;
import ch.epfl.sweng.swenggolf.database.SignInActivity;
import ch.epfl.sweng.swenggolf.offer.CreateOfferActivity;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;
import ch.epfl.sweng.swenggolf.offer.ShowOfferActivity;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;
import ch.epfl.sweng.swenggolf.R;


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
    public void loadProfileActivity(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(EXTRA_USERID, uid);
        startActivity(intent);
    }

    /**
     * Launches the SigninActivity.
     *
     * @param view the current view
     */
    public void loadSigninActivity(View view) {
        startActivity(new Intent(this, CreateUserActivity.class));
    }


    /**
     * Launches the CreateOfferActivity.
     *
     * @param view the current view
     */
    public void loadCreateOfferActivity(View view) {
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
    public void loadShowOffersActivity(View view) {
        Intent intent = new Intent(this, ListOfferActivity.class);
        startActivity(intent);
    }

}

