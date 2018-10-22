package ch.epfl.sweng.swenggolf.offer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.StorageConnection;

/**
 * The activity used to create offers. Note that the intent extras
 * must contain a string with key "username".
 */
public class CreateOfferActivity extends AppCompatActivity {

    private TextView errorMessage;
    private Offer offerToModify;
    private boolean creationAsked;

    private ImageView imageView;

    private Uri filePath;

    private static final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        creationAsked = false;
        setContentView(R.layout.activity_create_offer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        errorMessage = findViewById(R.id.error_message);

        offerToModify = getIntent().getParcelableExtra("offer");
        preFillFields();
    }

    private void preFillFields() {
        if (offerToModify != null) {
            EditText title = findViewById(R.id.offer_name);
            title.setText(offerToModify.getTitle(), TextView.BufferType.EDITABLE);
            EditText description = findViewById(R.id.offer_description);
            description.setText(offerToModify.getDescription(), TextView.BufferType.EDITABLE);
            ImageView picture = findViewById(R.id.offer_picture);
            String link = offerToModify.getLinkPicture();
            if (!link.isEmpty() && !Config.isTest()) {
                Picasso.with(this).load(Uri.parse(link)).into(picture);
            }
        }
    }

    /**
     * Allows the user to choose a picture from his gallery.
     *
     * @param view the view
     */
    public void choosePicture(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            imageView = findViewById(R.id.offer_picture);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates an Offer by parsing the contents given by the user.
     *
     * @param view the view
     */
    public void createOffer(View view) {
        if (creationAsked) {
            return;
        }
        EditText nameText = findViewById(R.id.offer_name);
        EditText descriptionText = findViewById(R.id.offer_description);

        final String name = nameText.getText().toString();
        final String description = descriptionText.getText().toString();

        if (name.isEmpty() || description.isEmpty()) {
            errorMessage.setText(R.string.error_create_offer_invalid);
            errorMessage.setVisibility(View.VISIBLE);
        } else if (filePath != null) {
            uploadImage(name, description);
        } else {
            createOfferObject(name, description, "");
        }


    }

    private void uploadImage(final String name, final String description) {
        StorageConnection storage = StorageConnection.getInstance();

        storage.writeFile(filePath)
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            String link = task.getResult().toString();
                            createOfferObject(name, description, link);
                        } else {
                            // TODO Handle failures
                        }
                    }
                });

        if (Config.isTest()) {
            createOfferObject(name, description, "");
        }

    }

    /**
     * Creates the offer and pushes it to the database.
     *
     * @param name        the title of the offer
     * @param description the description of the offer
     * @param link        the link of the offer's picture
     */
    protected void createOfferObject(String name, String description, String link) {
        String uuid = UUID.randomUUID().toString();
        if (offerToModify != null) {
            uuid = offerToModify.getUuid();
            if (link.isEmpty()) {
                link = offerToModify.getLinkPicture();
            }
        }
        final Offer newOffer =
                new Offer(Config.getUser().getUserId(), name, description, link, uuid);
        writeOffer(newOffer);
    }

    /**
     * Write an offer into the database.
     *
     * @param offer offer to be written
     */
    private void writeOffer(final Offer offer) {
        creationAsked = true;
        Database database = Database.getInstance();
        final Intent intent =
                new Intent(CreateOfferActivity.this,
                        ShowOfferActivity.class);
        CompletionListener listener = new CompletionListener() {
            @Override
            public void onComplete(@Nullable DbError databaseError) {
                if (databaseError == DbError.NONE) {
                    Toast.makeText(CreateOfferActivity.this, "Offer created",
                            Toast.LENGTH_SHORT).show();
                    intent.putExtra("offer", offer);
                    startActivity(intent);
                } else {
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText(R.string.error_create_offer_database);
                }
            }

        };
        database.write("/offers", offer.getUuid(), offer, listener);
    }
}
