package ch.epfl.sweng.swenggolf.offer.create;

import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.location.AppLocation;
import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.statistics.OfferStats;
import ch.epfl.sweng.swenggolf.storage.Storage;

import static ch.epfl.sweng.swenggolf.location.AppLocation.checkLocationPermission;
import static ch.epfl.sweng.swenggolf.offer.create.CreateOfferActivity.OFF;
import static ch.epfl.sweng.swenggolf.offer.create.CreateOfferActivity.ON;
import static ch.epfl.sweng.swenggolf.offer.create.CreateOfferActivity.SEPARATION;

/**
 * Helps the CreateOfferactivity to set some fields.
 */
class CreateHelper {

    private final CreateOfferActivity create;
    private final CreateListeners listeners;

    /**
     * Initialize the create helper.
     *
     * @param create    the CreateOfferActivity to help
     * @param listeners the CreateListeners
     */
    protected CreateHelper(CreateOfferActivity create, CreateListeners listeners) {
        this.create = create;
        this.listeners = listeners;
    }

    /**
     * In this case, we consider the offer to be empty if the title and the description are empty
     * and the category is the default one.
     * @param builder the builder of the offer
     * @return true if the offer is empty, false otherwise
     */
    static boolean isOfferEmpty(Offer.Builder builder) {
        return builder.getTitle().isEmpty() && builder.getDescription().isEmpty()
                && builder.getTag() == Category.OTHER;
    }

    /**
     * Pre-fill the date, category, location fields of the CreateOfferActivity.
     */
    void preFillFields() {

        setupSpinner();

        create.now = new GregorianCalendar(create.now.get(Calendar.YEAR),
                create.now.get(Calendar.MONTH), create.now.get(Calendar.DATE));

        create.creationDate = create.now.getTimeInMillis();
        create.endDate = create.now.getTimeInMillis() + SEPARATION;

        if (create.offerToModify != null) {

            loadCreateOfferFields();
            loadImage();

        }
    }

    void loadCreateOfferFields() {
        EditText title = create.inflated.findViewById(R.id.offer_name);
        title.setText(create.offerBuilder.getTitle(), TextView.BufferType.EDITABLE);

        EditText description = create.inflated.findViewById(R.id.offer_description);
        description.setText(create.offerBuilder.getDescription(),
                TextView.BufferType.EDITABLE);

        create.categorySpinner.setSelection(create.offerBuilder.getTag().ordinal());

        create.location = new Location("");
        create.location.setLatitude(create.offerBuilder.getLatitude());
        create.location.setLongitude(create.offerBuilder.getLongitude());

        create.creationDate = create.offerBuilder.getCreationDate();
        create.endDate = create.offerBuilder.getEndDate();

        checkFillConditions();
    }

    private void setupSpinner() {
        create.categorySpinner = create.inflated.findViewById(R.id.category_spinner);
        create.categorySpinner.setAdapter(new ArrayAdapter<>(create.getContext(),
                android.R.layout.simple_list_item_1, Category.values()));
    }

    private void checkFillConditions() {
        if (create.location.getLatitude() == 0.0 && create.location.getLongitude() == 0.0) {
            create.setCheckbox(ON);
        }
    }

    private void loadImage() {
        ImageView picture = create.inflated.findViewById(R.id.offer_picture);
        String link = create.offerToModify.getLinkPicture();

        if (!link.isEmpty() && !Config.isTest()) {
            Picasso.with(create.getContext()).load(Uri.parse(link)).into(picture);
        }
    }

    /**
     * Creates the offer and pushes it to the database.
     *
     * @param name        the title of the offer
     * @param description the description of the offer
     */
    void createOfferObject(String name, String description, Category tag) {
        Offer.Builder builder = getOfferBuilder(name, description, tag);
        final Offer newOffer = builder.build();
        if (create.filePath == null) {
            writeOffer(newOffer);
            OfferStats.initializeNbViews(newOffer);
        } else {
            uploadImage(newOffer);
        }
    }

    @NonNull
    protected Offer.Builder getOfferBuilder(String name, String description, Category tag) {

        Offer.Builder builder = create.offerBuilder;
        builder.setUserId(Config.getUser().getUserId())
                .setTitle(name).setDescription(description)
                .setTag(tag).setCreationDate(create.creationDate)
                .setEndDate(create.endDate).setLocation(create.location);
        return builder;
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
        Storage.getInstance().write(create.filePath, "images/" + offer.getUuid(), listener);
    }

    /**
     * Write an offer into the database.
     *
     * @param offer offer to be written
     */
    private void writeOffer(final Offer offer) {
        create.creationAsked = true;
        Database database = Database.getInstance();
        CompletionListener listener = listeners.createWriteOfferListener(offer);
        database.write(Database.OFFERS_PATH, offer.getUuid(), offer, listener);
    }

    /**
     * Update the User score.
     *
     * @param offerToModify the offer to modify
     * @param offer         the offer
     */
    void updateUserScore(Offer offerToModify, Offer offer) {
        int scoreToAdd = 0;
        if (offerToModify == null) {
            scoreToAdd += offer.offerValue();
        } else {
            scoreToAdd += offerToModify.offerValueDiff(offer);
        }
        DatabaseUser.addPointsToCurrentUser(scoreToAdd);
    }

    /**
     * Attach location to the offer.
     */
    void attachLocation() {
        if (create.location.getLatitude() != 0.0 || create.location.getLongitude() != 0.0) {

            create.location = new Location("");
            create.setCheckbox(OFF);

            return;
        }

        if (checkLocationPermission(create.getActivity())
                & AppLocation.checkLocationActive(create.getContext())) {
            create.location = new Location(""); // temporary location to avoid crash
            AppLocation currentLocation = AppLocation.getInstance(create.getActivity());
            currentLocation.getLocation(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location l) {
                    saveLocation(l);
                }
            });
        }
    }

    private void saveLocation(Location location) {
        create.location = location;
        create.setCheckbox(ON);
    }
}
