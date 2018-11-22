package ch.epfl.sweng.swenggolf.offer.create;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.storage.Storage;

import static android.provider.MediaStore.EXTRA_OUTPUT;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static ch.epfl.sweng.swenggolf.storage.Storage.CAPTURE_IMAGE_REQUEST;
import static ch.epfl.sweng.swenggolf.storage.Storage.PICK_IMAGE_REQUEST;

/**
 * Create Listeners to help the offer creation
 */
class CreateListeners {

    private CreateOfferActivity create;

    View.OnClickListener onTakePictureClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Intent takePictureIntent = Storage.takePicture(create.getActivity());

                if (takePictureIntent.resolveActivity(create.getActivity().getPackageManager())
                        != null) {
                    create.tempPicturePath = (Uri) takePictureIntent.getExtras().get(EXTRA_OUTPUT);
                    create.startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
                } else {
                    Toast.makeText(create.getContext(), "Cannot take a picture", LENGTH_SHORT)
                            .show();
                }
            } catch (IOException e) {
                Toast.makeText(create.getContext(), "Unable to create picture", LENGTH_LONG)
                        .show();
            }
        }
    };


    View.OnClickListener onCreateOfferClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            create.createOffer();
        }
    };

    /**
     * Create a completion listener based on an offer.
     * @param offer the corresponding offer
     * @return
     */
    CompletionListener createWriteOfferListener(final Offer offer) {
        return new CompletionListener() {
            @Override
            public void onComplete(@Nullable DbError databaseError) {
                if (databaseError == DbError.NONE) {
                    Toast.makeText(create.getContext(), "Offer created",
                            LENGTH_SHORT).show();
                    create.createHelper.updateUserScore(create.offerToModify, offer);
                    create.replaceCentralFragmentWithOffer(offer);
                } else {
                    create.errorMessage.setVisibility(View.VISIBLE);
                    create.errorMessage.setText(R.string.error_create_offer_database);
                }
            }
        };
    }

    CreateListeners(CreateOfferActivity createOfferActivity) {
        this.create = createOfferActivity;
    }

    /**
     * Set the listeners.
     */
    void setListeners() {

        create.inflated.findViewById(R.id.fetch_picture)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        create.startActivityForResult(Storage.choosePicture(), PICK_IMAGE_REQUEST);
                    }
                });

        create.inflated.findViewById(R.id.pick_date)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        create.showDatePickerDialog();
                    }
                });

        create.inflated.findViewById(R.id.offer_position_status)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        create.createHelper.attachLocation();
                    }
                });

        create.inflated.findViewById(R.id.take_picture)
                .setOnClickListener(onTakePictureClick);

        create.inflated.findViewById(R.id.button_create_offer)
                .setOnClickListener(onCreateOfferClick);
    }
}
