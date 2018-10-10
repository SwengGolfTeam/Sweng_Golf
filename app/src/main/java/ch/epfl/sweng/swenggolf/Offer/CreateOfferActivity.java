package ch.epfl.sweng.swenggolf.Offer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ch.epfl.sweng.swenggolf.Database.DatabaseConnection;
import ch.epfl.sweng.swenggolf.R;

/**
 * The activity used to create offers. Note that the intent extras
 * must contain a string with key "username".
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

    /**
     * Creates an Offer by parsing the contents given by the user.
     *
     * @param view the view
     */
    public void createOffer(View view) {

        EditText nameText = findViewById(R.id.offer_name);
        EditText descriptionText = findViewById(R.id.offer_description);

        final String name = nameText.getText().toString();
        final String description = descriptionText.getText().toString();

        if(!name.isEmpty() && !description.isEmpty()){
            final Offer newOffer = new Offer(username, name, description);
            DatabaseConnection db = new DatabaseConnection();
            db.writeObject("offers", "id_"+newOffer.getTitle(), newOffer);
            finish();
        } else {
            TextView errorMessage = findViewById(R.id.error_message);
            errorMessage.setVisibility(View.VISIBLE);
        }


    }
}
