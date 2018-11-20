package ch.epfl.sweng.swenggolf.offer.create;

import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.UUID;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.location.AppLocation;
import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.storage.Storage;

import static ch.epfl.sweng.swenggolf.location.AppLocation.checkLocationPermission;
import static ch.epfl.sweng.swenggolf.offer.create.CreateOfferActivity.OFF;
import static ch.epfl.sweng.swenggolf.offer.create.CreateOfferActivity.ON;

class CreateHelper {

    private final CreateOfferActivity create;

    protected CreateHelper(CreateOfferActivity create) {
        this.create = create;
    }

    /**
     * Creates the offer and pushes it to the database.
     *
     * @param name        the title of the offer
     * @param description the description of the offer
     */
    void createOfferObject(String name, String description, Category tag) {
        String uuid;
        String link;
        if (create.offerToModify != null) {
            uuid = create.offerToModify.getUuid();
            link = create.offerToModify.getLinkPicture();
        } else {
            uuid = UUID.randomUUID().toString();
            link = "";
        }

        final Offer newOffer = new Offer(Config.getUser().getUserId(), name, description,
                link, uuid, tag, create.creationDate, create.endDate, create.location);


        if (create.filePath == null) {
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
        CompletionListener listener = create.createWriteOfferListener(offer);
        database.write(Database.OFFERS_PATH, offer.getUuid(), offer, listener);
    }

    void updateUserScore(Offer offerToModify, Offer offer) {
        int scoreToAdd = 0;
        if (offerToModify == null) {
            scoreToAdd += offer.offerValue();
        } else {
            scoreToAdd += offerToModify.offerValueDiff(offer);
        }
        DatabaseUser.addPointsToCurrentUser(scoreToAdd);
    }

    void attachLocation() {
        if (create.location.getLatitude() != 0.0 || create.location.getLongitude() != 0.0) {

            create.location = new Location("");
            create.setCheckbox(OFF);

            return;
        }

        if (checkLocationPermission(create.getActivity())) {
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
