package ch.epfl.sweng.swenggolf.offer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.DatabaseConnection;

/**
 * The activity used to create offers. Note that the intent extras
 * must contain a string with key "username".
 */
public class CreateOfferActivity extends AppCompatActivity {

    private String username;
    private TextView errorMessage;

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
        errorMessage = findViewById(R.id.error_message);
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

        if (!name.isEmpty() && !description.isEmpty()) {
            final Offer newOffer = new Offer(username, name, description);
            DatabaseConnection db = DatabaseConnection.getInstance();
            writeOffer(newOffer, db);
        } else {
            errorMessage.setText(R.string.error_create_offer_invalid);
            errorMessage.setVisibility(View.VISIBLE);
        }


    }

    /**
     * Write an offer into the database.
     *
     * @param offer offer to be written
     * @param db    the database
     */
    private void writeOffer(final Offer offer, DatabaseConnection db) {
        DatabaseReference.CompletionListener listener = new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError,
                                   @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {

                    Intent intent =
                            new Intent(CreateOfferActivity.this,
                                    ShowOfferActivity.class);
                    intent.putExtra("offer", offer);
                    startActivity(intent);

                } else {
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText(R.string.error_create_offer_database);
                }
            }
        };
        db.writeObject("offers", offer.getTitle(), offer, listener);
    }
}
