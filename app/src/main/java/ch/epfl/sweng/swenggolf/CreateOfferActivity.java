package ch.epfl.sweng.swenggolf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * The activity used to create offers. Note that the intent extras
 * must contain a string with key "username"
 */
public class CreateOfferActivity extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_offer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        username = getIntent().getExtras().getString("username");

        if (username == null) {
            throw new NullPointerException("No username given to CreateOfferActivity");
        }
    }

    public void createOffer(View view) {

        EditText nameText = findViewById(R.id.offer_name);
        EditText descriptionText = findViewById(R.id.offer_description);

        final String name = nameText.getText().toString();
        final String description = descriptionText.getText().toString();

        if (!name.isEmpty() && !description.isEmpty()) {
            final Offer newOffer = new Offer(username, name, description);
            //TODO: add code to add the offer in the database
            finish();
        } else {
            TextView errorMessage = findViewById(R.id.error_message);
            errorMessage.setVisibility(View.VISIBLE);
        }


    }
}
