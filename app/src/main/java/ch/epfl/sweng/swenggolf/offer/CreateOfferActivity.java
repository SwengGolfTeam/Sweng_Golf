package ch.epfl.sweng.swenggolf.offer;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.location.AppLocation;
import ch.epfl.sweng.swenggolf.storage.Storage;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

import static android.provider.MediaStore.EXTRA_OUTPUT;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static ch.epfl.sweng.swenggolf.Permission.GPS;
import static ch.epfl.sweng.swenggolf.storage.Storage.CAPTURE_IMAGE_REQUEST;
import static ch.epfl.sweng.swenggolf.storage.Storage.PICK_IMAGE_REQUEST;

/**
 * The fragment used to create offers. Note that the extras
 * must contain a string with key "username".
 */
public class CreateOfferActivity extends FragmentConverter {

    private TextView errorMessage;
    private Offer offerToModify;
    private boolean creationAsked;
    private Spinner categorySpinner;
    private Uri filePath = null;

    private Location location = new Location("default");

    private Uri photoDestination = null;
    private Uri tempPicturePath = null;

    private static final boolean ON = true;
    private static final boolean OFF = false;

    private View.OnClickListener onTakePictureClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Intent takePictureIntent = Storage.takePicture(getActivity());

                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    tempPicturePath = (Uri) takePictureIntent.getExtras().get(EXTRA_OUTPUT);
                    startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
                } else {
                    Toast.makeText(getContext(), "Cannot take a picture", LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Toast.makeText(getContext(), "Unable to create picture", LENGTH_LONG).show();
            }
        }
    };

    private View.OnClickListener onCreateOfferClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createOffer(v);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflated = inflater.inflate(R.layout.activity_create_offer,
                container, false);
        setToolbar(R.drawable.ic_baseline_arrow_back_24px, R.string.create_offer);
        errorMessage = inflated.findViewById(R.id.error_message);
        setupSpinner(inflated);
        preFillFields(inflated);
        inflated.findViewById(R.id.fetch_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(Storage.choosePicture(), PICK_IMAGE_REQUEST);
            }
        });
        inflated.findViewById(R.id.offer_position_status).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachLocation();
            }
        });
        inflated.findViewById(R.id.take_picture).setOnClickListener(onTakePictureClick);
        inflated.findViewById(R.id.button).setOnClickListener(onCreateOfferClick);
        return inflated;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        creationAsked = false;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EditText title = findViewById(R.id.offer_name);
        title.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(Offer.TITLE_MAX_LENGTH)});
        EditText description = findViewById(R.id.offer_description);
        description.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(Offer.DESCRIPTION_MAX_LENGTH)});
    }

    private void setupSpinner(View v) {
        categorySpinner = v.findViewById(R.id.category_spinner);
        categorySpinner.setAdapter(new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_list_item_1, Category.values()));
    }

    private void preFillFields(View inflated) {
        if (getArguments() != null
                && (offerToModify = getArguments().getParcelable("offer")) != null) {

            EditText title = inflated.findViewById(R.id.offer_name);
            title.setText(offerToModify.getTitle(), TextView.BufferType.EDITABLE);

            EditText description = inflated.findViewById(R.id.offer_description);
            description.setText(offerToModify.getDescription(), TextView.BufferType.EDITABLE);

            categorySpinner.setSelection(offerToModify.getTag().toInt());

            location = new Location("");
            location.setLatitude(offerToModify.getLatitude());
            location.setLongitude(offerToModify.getLongitude());

            ImageView picture = inflated.findViewById(R.id.offer_picture);
            String link = offerToModify.getLinkPicture();

            if (!link.isEmpty() && !Config.isTest()) {
                Picasso.with(this.getContext()).load(Uri.parse(link)).into(picture);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Storage.conditionActivityResult(requestCode, resultCode, data)) {
            removeStalledPicture();
            switch (requestCode) {
                case CAPTURE_IMAGE_REQUEST: {
                    photoDestination = tempPicturePath;
                    File cacheDirectory = getActivity().getCacheDir();
                    File photo = new File(cacheDirectory, photoDestination.getLastPathSegment());
                    filePath = FileProvider.getUriForFile(getContext(),
                            "ch.epfl.sweng.swenggolf.fileprovider", photo);
                    photoDestination = null;
                    break;
                }
                case PICK_IMAGE_REQUEST: {
                    filePath = data.getData();
                    break;
                }
                default: {
                    return;
                }
            }
            ImageView imageView = findViewById(R.id.offer_picture);
            Picasso.with(this.getContext()).load(filePath).fit().into(imageView);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (Config.onRequestPermissionsResult(requestCode, grantResults) == GPS) {
            attachLocation();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        removeStalledPicture();
    }

    private void removeStalledPicture() {
        if (photoDestination != null) {
            File previous = new File(getContext().getCacheDir(),
                    photoDestination.getLastPathSegment());
            if (!previous.delete()) {
                Toast.makeText(this.getContext(),
                        "Previous picture couldn't be removed", LENGTH_LONG).show();
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

        final String title = nameText.getText().toString();
        final String description = descriptionText.getText().toString();
        final Category category = Category.valueOf(categorySpinner.getSelectedItem().toString());

        if (title.length() < Offer.TITLE_MIN_LENGTH || description.length()
                < Offer.DESCRIPTION_MIN_LENGTH) {
            errorMessage.setText(R.string.error_create_offer_invalid);
            errorMessage.setVisibility(View.VISIBLE);
        } else {
            createOfferObject(title, description, category);
        }

    }

    /**
     * Creates the offer and pushes it to the database.
     *
     * @param name        the title of the offer
     * @param description the description of the offer
     */
    protected void createOfferObject(String name, String description, Category tag) {
        String uuid;
        String link;
        if (offerToModify != null) {
            uuid = offerToModify.getUuid();
            link = offerToModify.getLinkPicture();
        } else {
            uuid = UUID.randomUUID().toString();
            link = "";
        }

        final Offer newOffer = new Offer(Config.getUser().getUserId(), name, description,
                link, uuid, tag, location);

        if (filePath == null) {
            writeOffer(newOffer);
        } else {
            uploadImage(newOffer);
        }
    }

    private void uploadImage(final Offer offer) {
        OnCompleteListener<Uri> listener = new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    String link = task.getResult().toString();
                    Offer newOffer = offer.updateLinkToPicture(link);
                    writeOffer(newOffer);
                } else {
                    // TODO Handle failures
                }
            }
        };
        Storage.getInstance().write(filePath, "images/" + offer.getUuid(), listener);
    }

    /**
     * Write an offer into the database.
     *
     * @param offer offer to be written
     */
    private void writeOffer(final Offer offer) {
        creationAsked = true;
        Database database = Database.getInstance();
        CompletionListener listener = new CompletionListener() {
            @Override
            public void onComplete(@Nullable DbError databaseError) {
                if (databaseError == DbError.NONE) {
                    Toast.makeText(CreateOfferActivity.this.getContext(), "Offer created",
                            LENGTH_SHORT).show();
                    replaceCentralFragment(FragmentConverter.createShowOfferWithOffer(offer));
                } else {
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText(R.string.error_create_offer_database);
                }
            }

        };
        database.write(Database.OFFERS_PATH, offer.getUuid(), offer, listener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                InputMethodManager manager =
                        (InputMethodManager) getActivity()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                Fragment backFrag;
                if (offerToModify == null) {
                    backFrag = new ListOfferActivity();
                } else {
                    backFrag = FragmentConverter.createShowOfferWithOffer(offerToModify);
                }
                replaceCentralFragment(backFrag);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void attachLocation() {

        if (location.getLatitude() != 0.0 || location.getLongitude() != 0.0) {

            location = new Location("");
            setCheckbox(OFF);

            return;
        }

        if (Config.checkLocationPermission(getActivity())) {
            AppLocation currentLocation = AppLocation.getInstance(getActivity());
            currentLocation.getLocation(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location l) {
                    saveLocation(l);
                }
            });
        }
    }

    private void saveLocation(Location location) {
        this.location = location;
        setCheckbox(ON);
    }

    private void setCheckbox(boolean on) {
        String uri = on ? "@android:drawable/checkbox_on_background"
                : "@android:drawable/checkbox_off_background";
        int uncheckResource = getResources().getIdentifier(uri, null,
                getActivity().getPackageName());
        ImageView check = findViewById(R.id.offer_position_status);
        Drawable uncheck = getResources().getDrawable(uncheckResource);
        check.setImageDrawable(uncheck);
    }

}
