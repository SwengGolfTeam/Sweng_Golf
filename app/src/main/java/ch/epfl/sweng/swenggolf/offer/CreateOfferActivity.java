package ch.epfl.sweng.swenggolf.offer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.DatabaseConnection;

/**
 * The activity used to create offers. Note that the intent extras
 * must contain a string with key "username".
 */
public class CreateOfferActivity extends AppCompatActivity {

    private String username;
    private TextView errorMessage;
    private Offer offerToModify;

    private ImageView imageView;

    private Uri filePath;

    private static final int PICK_IMAGE_REQUEST = 71;

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
            Picasso.with(this).load(Uri.parse(offerToModify.getLinkPicture())).into(picture);
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
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_REQUEST);
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

        EditText nameText = findViewById(R.id.offer_name);
        EditText descriptionText = findViewById(R.id.offer_description);

        final String name = nameText.getText().toString();
        final String description = descriptionText.getText().toString();

        if (!name.isEmpty() && !description.isEmpty() && filePath != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            uploadImage(storageReference, name, description);
        } else {
            errorMessage.setText(R.string.error_create_offer_invalid);
            errorMessage.setVisibility(View.VISIBLE);
        }


    }

    private void uploadImage(StorageReference storageReference,
                             final String name, final String description) {
        final StorageReference ref =
                storageReference.child("images/" + UUID.randomUUID().toString());
        ref.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    String link = task.getResult().toString();
                    String uuid = UUID.randomUUID().toString();
                    if (offerToModify != null) {
                        uuid = offerToModify.getUuid();
                    }
                    final Offer newOffer = new Offer(username, name, description, link, uuid);
                    DatabaseConnection db = DatabaseConnection.getInstance();
                    writeOffer(newOffer, db);
                } else {
                    // TODO Handle failures
                }
            }
        });
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
                    Toast.makeText(CreateOfferActivity.this, "Offer created",
                            Toast.LENGTH_SHORT).show();
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
        db.writeObject("offers", offer.getUuid(), offer, listener);
    }
}
