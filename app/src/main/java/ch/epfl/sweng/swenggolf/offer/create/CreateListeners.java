package ch.epfl.sweng.swenggolf.offer.create;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.storage.Storage;

import static android.provider.MediaStore.EXTRA_OUTPUT;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static ch.epfl.sweng.swenggolf.storage.Storage.CAPTURE_IMAGE_REQUEST;
import static ch.epfl.sweng.swenggolf.storage.Storage.PICK_IMAGE_REQUEST;

/**
 * Create Listeners to help the offer creation.
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

    CreateListeners(CreateOfferActivity createOfferActivity) {
        this.create = createOfferActivity;
    }

    /**
     * Create a completion listener based on an offer.
     *
     * @param offer the corresponding offer
     * @return the correspondig CompletionListener
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
                    Database.getInstance().remove(Database.OFFERS_SAVED_PATH,
                            Config.getUser().getUserId(), new CompletionListener() {
                                @Override
                                public void onComplete(DbError error) {
                                    //If we fail to remove the saved offer, there is a problem on
                                    // the server. Thus we can't do anything here
                                }
                            });
                } else {
                    create.errorMessage.setVisibility(View.VISIBLE);
                    create.errorMessage.setText(R.string.error_create_offer_database);
                }
            }
        };
    }

    /**
     * Create a listener that looks for saved offer in the database. If it found one offer, it will
     * ask the user whether he wants to restore it or not.
     *
     * @return a listener
     */
    ValueListener<Offer.Builder> restoreOfferListener() {
        return new ValueListener<Offer.Builder>() {
            @Override
            public void onDataChange(final Offer.Builder value) {
                if (value != null) {
                    showAlertRestoreOffer(value);
                }
            }

            @Override
            public void onCancelled(DbError error) {
                //Do nothing
            }
        };
    }

    /**
     * If the offer is not empty, ask him to enter a name for the new pattern.
     *
     * @return the listener
     */
    View.OnClickListener savePatternListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Offer.Builder offerBuilder = create.getOfferBuilder();
                if (CreateHelper.isOfferEmpty(offerBuilder)) {
                    Toast.makeText(create.getContext(), R.string.create_pattern_error_empty,
                            Toast.LENGTH_LONG).show();
                } else {
                    showSavePatternDialog(offerBuilder);
                }
            }
        };
    }

    /**
     * Ask the user to choose a pattern to load.
     *
     * @return the listener
     */
    ValueListener<List<String>> loadPattern() {
        return new ValueListener<List<String>>() {
            @Override
            public void onDataChange(List<String> value) {
                if (!value.isEmpty()) {
                    final String[] s = value.toArray(new String[0]);
                    android.app.AlertDialog.Builder mBuilder =
                            new android.app.AlertDialog.Builder(create.getContext());
                    mBuilder.setTitle(R.string.choose_a_template);
                    mBuilder.setPositiveButton(R.string.accept, acceptLoadPatternListener(s));
                    mBuilder.setNegativeButton(R.string.cancel, null);
                    mBuilder.setSingleChoiceItems(s, -1, null);
                    mBuilder.show();

                } else {
                    Toast.makeText(create.getContext(), R.string.error_no_pattern_saved,
                            Toast.LENGTH_LONG)
                            .show();
                }

            }

            @Override
            public void onCancelled(DbError error) {
                Toast.makeText(create.getContext(), R.string.can_not_load_pattern_list,
                        Toast.LENGTH_LONG).show();
            }
        };
    }

    @NonNull
    private DialogInterface.OnClickListener acceptLoadPatternListener(final String[] s) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.app.AlertDialog dialog1 = (android.app.AlertDialog) dialog;
                String patternSelected = s[dialog1.getListView().getCheckedItemPosition()];
                Database database = Database.getInstance();
                database.read(Database.OFFERS_PATTERN_PATH + "/"
                                + Config.getUser().getUserId(), patternSelected,
                        new ValueListener<Offer.Builder>() {
                            @Override
                            public void onDataChange(Offer.Builder value) {
                                create.offerBuilder = value;
                                create.createHelper.loadCreateOfferFields();
                            }

                            @Override
                            public void onCancelled(DbError error) {
                                Toast.makeText(create.getContext(), R.string.can_not_load_pattern,
                                        Toast.LENGTH_LONG).show();
                            }
                        }, Offer.Builder.class);
            }
        };
    }

    private void showSavePatternDialog(final Offer.Builder offerBuilder) {
        android.app.AlertDialog.Builder mBuilder =
                new android.app.AlertDialog.Builder(create.getContext());
        final View edit = create.getLayoutInflater()
                .inflate(R.layout.save_offer_pattern, null);
        final EditText text = edit.findViewById(R.id.dialog_choose_offer_name_edit);
        mBuilder.setView(edit)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String patternName = text.getText().toString();
                        Database.getInstance().write(Database.OFFERS_PATTERN_PATH
                                + "/" + Config.getUser().getUserId(), patternName, offerBuilder);
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        mBuilder.setMessage(R.string.choose_name_pattern);
        final android.app.AlertDialog dialog = mBuilder.show();
        final Button acceptButton = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
        acceptButton.setEnabled(false);
        text.addTextChangedListener(patternNameTextWatcher(acceptButton));
    }

    @NonNull
    private TextWatcher patternNameTextWatcher(final Button acceptButton) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    acceptButton.setEnabled(false);
                } else {
                    acceptButton.setEnabled(true);
                }
            }
        };
    }

    private void showAlertRestoreOffer(final Offer.Builder value) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(create.getActivity());

        dialogBuilder.setTitle(R.string.old_offer_found)
                .setMessage(R.string.restore_offer_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        create.offerBuilder = value;
                        create.createHelper.loadCreateOfferFields();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Database database = Database.getInstance();
                        database.remove(Database.OFFERS_SAVED_PATH, Config.getUser().getUserId(),
                                new CompletionListener() {
                                    @Override
                                    public void onComplete(DbError error) {
                                        //Do nothing
                                    }
                                });
                        // user cancelled the dialog
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        Dialog alertDialog = dialogBuilder.create();
        alertDialog.show();
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
