package ch.epfl.sweng.swenggolf.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.SignInActivity;
import ch.epfl.sweng.swenggolf.offer.CreateOfferActivity;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /**
     * Launches the SigninActivity.
     *
     * @param view the current view
     */
    public void loadSigninActivity(View view) {
        startActivity(new Intent(this, SignInActivity.class));
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

