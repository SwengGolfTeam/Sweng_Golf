package ch.epfl.sweng.swenggolf.offer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.location.AppLocation;
import ch.epfl.sweng.swenggolf.notification.Notification;
import ch.epfl.sweng.swenggolf.notification.NotificationManager;
import ch.epfl.sweng.swenggolf.notification.NotificationType;
import ch.epfl.sweng.swenggolf.offer.answer.Answer;
import ch.epfl.sweng.swenggolf.offer.answer.Answers;
import ch.epfl.sweng.swenggolf.offer.answer.ListAnswerAdapter;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.storage.Storage;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;
import ch.epfl.sweng.swenggolf.tools.ViewUserFiller;

import static ch.epfl.sweng.swenggolf.Permission.GPS;
import static ch.epfl.sweng.swenggolf.location.AppLocation.checkLocationPermission;

/**
 * Fragment which shows an offer.
 */
public class ShowOfferActivity extends FragmentConverter {
    public static final int DISTANCE_GRANULARITY = 100;
    private static final int KILOMETER_SIZE = 1000;
    private final Answers defaultAnswers = new Answers(new ArrayList<Answer>(), -1);
    private boolean userIsCreator;
    private Offer offer;
    private ListAnswerAdapter listAnswerAdapter;
    private int fragmentsToSkip;

    private View inflated;
    private TextView errorMessage;
    private LinearLayout mLayout;
    private View newReaction;

    private ValueListener<Boolean> closingListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setToolbar(R.drawable.ic_baseline_arrow_back_24px, R.string.show_offer_title);
        assert getArguments() != null;
        inflated = inflater.inflate(R.layout.activity_show_offer, container, false);
        userIsCreator = Config.getUser().getUserId().equals(offer.getUserId());
        setButtonCloseOffer();
        errorMessage = inflated.findViewById(R.id.error_message);
        mLayout = inflated.findViewById(R.id.list_answers);
        LayoutInflater mInflater = getLayoutInflater();
        newReaction = mInflater.inflate(R.layout.reaction_you, mLayout, false);
        setContents();
        setAnswersRecyclerView();
        if (offer.getIsClosed()) {
            hideReactButton();
            listAnswerAdapter.closeAnswers();
        } else {
            setClosingListener();
            setAnswerToPost();
        }
        fetchAnswers();
        return inflated;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Database.getInstance().deafen(Database.ANSWERS_PATH, offer.getUuid(),
                listAnswerAdapter.getUpdateListener());
        listAnswerAdapter.setUpdateListener(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = getArguments();
        offer = bundle.getParcelable(Offer.OFFER);
        if (bundle.containsKey(FRAGMENTS_TO_SKIP)) {
            fragmentsToSkip = bundle.getInt(FRAGMENTS_TO_SKIP);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (userIsCreator && !offer.getIsClosed()) {
            inflater.inflate(R.menu.menu_show_offer, menu);
        } else {
            inflater.inflate(R.menu.menu_empty, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                getActivity().onBackPressed();
                return true;
            }
            case R.id.button_modify_offer: {
                replaceCentralFragment(createOfferActivityWithOffer(offer, fragmentsToSkip));
                return true;
            }
            case R.id.button_delete_offer: {
                showDeleteAlertDialog();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (Config.onRequestPermissionsResult(requestCode, grantResults) == GPS) {
            setLocation();
        }
    }

    /* methods to fill the offer content */

    private void setContents() {
        TextView offerTitle = inflated.findViewById(R.id.show_offer_title);
        offerTitle.setText(offer.getTitle());

        TextView offerDate = inflated.findViewById(R.id.show_offer_date);
        offerDate.setText("Valid until : " + Offer.dateFormat()
                .format(offer.getEndDate()));

        final TextView offerAuthor = inflated.findViewById(R.id.show_offer_author);
        ViewUserFiller.fillWithUsername(offerAuthor, offer.getUserId());
        offerAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserProfile(v);
            }
        });

        TextView offerDescription = inflated.findViewById(R.id.show_offer_description);
        TextView offerTag = inflated.findViewById(R.id.show_offer_tag);
        offerTag.setText(offer.getTag().toString());
        offerDescription.setText(offer.getDescription());
        ImageView offerPicture = inflated.findViewById(R.id.show_offer_picture);
        if (!offer.getLinkPicture().isEmpty()) {

            Picasso.with(this.getContext()).load(Uri.parse(offer.getLinkPicture()))
                    .into(offerPicture);
            offerPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                    View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                    PhotoView photoView = mView.findViewById(R.id.imageView);
                    Picasso.with(getContext()).load(Uri.parse(offer.getLinkPicture()))
                            .into(photoView);
                    mBuilder.setView(mView);
                    mBuilder.setNegativeButton("quit",  new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // user cancelled the dialog
                        }
                    });
                    AlertDialog mDialog = mBuilder.create();
                    mDialog.show();
                }
            });
        } else {
            offerPicture.getLayoutParams().height = 0;
        }

        if (offer.getLongitude() != 0.0 || offer.getLatitude() != 0.0) {
            setLocation();
        }

    }

    private ValueListener<User> createFiller(final View inflated) {
        return new ValueListener<User>() {
            @Override
            public void onDataChange(User value) {
                TextView userName = inflated.findViewById(R.id.your_user_name);
                userName.setText(value.getUserName());
                ImageView userPic = inflated.findViewById(R.id.your_user_pic);
                Picasso.with(userPic.getContext())
                        .load(Uri.parse(value.getPhoto()))
                        .placeholder(R.drawable.gender_neutral_user1)
                        .fit().into(userPic);
            }

            @Override
            public void onCancelled(DbError error) {
                Log.d(error.toString(), "Unable to load user from database");
            }
        };
    }

    /**
     * Open the user profile when we click on his name.
     *
     * @param v the view
     */
    public void openUserProfile(View v) {

        DatabaseUser.getUser(new ValueListener<User>() {
                                 @Override
                                 public void onDataChange(User user) {
                                     Fragment fragment =
                                             createShowProfileWithProfile(user);
                                     replaceCentralFragment(fragment);
                                 }

                                 @Override
                                 public void onCancelled(DbError error) {
                                     Toast.makeText(ShowOfferActivity.this.getContext(),
                                             R.string.error_load_user, Toast.LENGTH_LONG).show();
                                 }
                             },
                offer.getUserId());
    }

    /* methods for the answers */

    private void setAnswersRecyclerView() {
        RecyclerView mRecyclerView = inflated.findViewById(R.id.answers_recycler_view);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setNestedScrollingEnabled(false);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        listAnswerAdapter = new ListAnswerAdapter(defaultAnswers, offer, this);
        // Add dividing line
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(listAnswerAdapter);

    }

    private void fetchAnswers() {
        ValueListener<Answers> answerListener = new ValueListener<Answers>() {
            @Override
            public void onDataChange(Answers value) {
                if (value != null) {
                    listAnswerAdapter.setAnswers(value);
                    if (offer.getIsClosed()) {
                        offerAccessToDiscussion();
                    }
                }
            }

            @Override
            public void onCancelled(DbError error) {
                errorMessage.setVisibility(View.VISIBLE);
            }
        };
        Database.getInstance().listen(Database.ANSWERS_PATH, offer.getUuid(),
                answerListener, Answers.class);
        listAnswerAdapter.setUpdateListener(answerListener);
    }


    private void setAnswerToPost() {
        final Button reactButton = inflated.findViewById(R.id.react_button);
        reactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View buttons = inflated.findViewById(R.id.offer_buttons);
                mLayout.removeView(buttons);
                mLayout.addView(newReaction);


                ValueListener<User> vlUser = createFiller(inflated);

                Button post = newReaction.findViewById(R.id.post_button);
                post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postAnswer(v);
                    }
                });

                EditText comment = newReaction.findViewById(R.id.your_answer_description);
                comment.setFilters(new InputFilter[]{
                        new InputFilter.LengthFilter(Answer.COMMENT_MAX_LENGTH)});

                DatabaseUser.getUser(vlUser, Config.getUser().getUserId());

            }
        });
    }

    /**
     * Adds a new answer to the list of answers of the offer.
     *
     * @param view the button that got clicked
     */
    public void postAnswer(View view) {
        EditText editText = findViewById(R.id.your_answer_description);
        if (editText.length() < Answer.COMMENT_MIN_LENGTH) {
            editText.setError(getString(R.string.answer_limit, Answer.COMMENT_MIN_LENGTH));
        } else {
            Answers answers = listAnswerAdapter.getAnswers();
            answers.getAnswerList()
                    .add(new Answer(Config.getUser().getUserId(), editText.getText().toString()));
            Database.getInstance().write(Database.ANSWERS_PATH, offer.getUuid(), answers);
            InputMethodManager imm = (InputMethodManager) getActivity()
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(newReaction.getWindowToken(), 0);
            mLayout.removeView(newReaction);

            if (!userIsCreator) {
                NotificationManager.addPendingNotification(offer.getUserId(),
                        new Notification(NotificationType.ANSWER_POSTED,
                                Config.getUser(), offer));
            }
            listAnswerAdapter.notifyDataSetChanged();
        }

    }

    /* methods for the location */

    private void setLocation() {
        if (checkLocationPermission(getActivity())) {
            AppLocation currentLocation = AppLocation.getInstance(getActivity());

            currentLocation.getLocation(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Location offerLocation = new Location(LocationManager.GPS_PROVIDER);
                        offerLocation.setLatitude(offer.getLatitude());
                        offerLocation.setLongitude(offer.getLongitude());

                        int distance = (int) offerLocation.distanceTo(location)
                                / DISTANCE_GRANULARITY * DISTANCE_GRANULARITY;
                        String toPrompt;
                        if (distance >= KILOMETER_SIZE) {
                            distance /= KILOMETER_SIZE;
                            toPrompt = distance + " km";
                        } else if (distance == 0) {
                            toPrompt = "Near";
                        } else {
                            toPrompt = distance + " m";
                        }
                        writeLocationToPage(toPrompt);
                    }
                }
            });

        }
    }

    private void writeLocationToPage(String toWrite) {
        TextView distanceText = inflated.findViewById(R.id.saved_location_offer);
        distanceText.setText(toWrite);
        final Context context = getActivity();
        distanceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + offer.getLatitude()
                        + "," + offer.getLongitude() + "(" + offer.getTitle() + ")");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    startActivity(mapIntent);
                }

            }
        });
    }

    /* methods to delete an offer */

    /**
     * Display the Alert Dialog for the delete.
     */
    public void showDeleteAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this offer?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteOfferInDatabase();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // user cancelled the dialog
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        Dialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Delete the offer in the database.
     */
    private void deleteOfferInDatabase() {
        if (!offer.getLinkPicture().isEmpty()) {
            Storage storage = Storage.getInstance();
            storage.remove(offer.getLinkPicture());
        }
        Database database = Database.getInstance();

        database.remove(Database.OFFERS_PATH, offer.getUuid(), getRemoveOfferListerner(true));
        database.remove(Database.ANSWERS_PATH, offer.getUuid(), getRemoveOfferListerner(false));
        DatabaseUser.addPointsToCurrentUser(-offer.offerValue());
    }

    private CompletionListener getRemoveOfferListerner(final boolean showToast) {
        return new CompletionListener() {
            @Override
            public void onComplete(@Nullable DbError databaseError) {
                if (databaseError == DbError.NONE && showToast) {
                    Toast.makeText(getContext(), R.string.offer_deleted,
                            Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }

        };
    }


    /* methods to close an offer */

    private void setButtonCloseOffer() {
        if (userIsCreator && !offer.getIsClosed()) {
            final Button closeButton = inflated.findViewById(R.id.close_offer_button);
            closeButton.setVisibility(View.VISIBLE);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeOffer();
                }
            });
        }
    }

    /**
     * Close the offer. This include writing the modifications on the database, disable the
     * reaction button and disable the possibility to add answers.
     */
    public void closeOffer() {
        offer = new Offer.Builder(offer).setIsClosed(true).build();
        Database.getInstance().deafen(Database.OFFERS_PATH + "/" + offer.getUuid(),
                "isClosed", closingListener);
        Database.getInstance().write(Database.OFFERS_PATH, offer.getUuid(), offer);
        hideReactButton();
        Button closeButton = inflated.findViewById(R.id.close_offer_button);
        closeButton.setVisibility(View.GONE);
        getActivity().invalidateOptionsMenu();
        offerAccessToDiscussion();
        listAnswerAdapter.closeAnswers();
    }


    private void setClosingListener() {
        closingListener = new ValueListener<Boolean>() {
            @Override
            public void onDataChange(Boolean value) {
                if (value != null && value) {
                    hideReactButton();
                }
            }

            @Override
            public void onCancelled(DbError error) {
                //No notification or errored notifications, as such no modification on display.
            }
        };
        Database.getInstance().listen(Database.OFFERS_PATH + "/" + offer.getUuid(),
                "isClosed", closingListener, Boolean.class);
    }

    private void hideReactButton() {
        Button reactButton = inflated.findViewById(R.id.react_button);
        if (reactButton != null) {
            reactButton.setVisibility(View.GONE);
        }
        TextView closedMessage = inflated.findViewById(R.id.offer_is_closed);
        closedMessage.setVisibility(View.VISIBLE);
    }

    private void offerAccessToDiscussion() {
        Answers answers = listAnswerAdapter.getAnswers();
        if (answers.getFavoritePos() != Answers.NO_FAVORITE) {
            final String chosenUserId = answers.getUserOfPosition(answers.getFavoritePos());
            if (userIsCreator || chosenUserId.equals(Config.getUser().getUserId())) {
                Button discussionButton = inflated.findViewById(R.id.open_discussion);
                discussionButton.setVisibility(View.VISIBLE);
                discussionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToMessagingWithUser(chosenUserId);


                    }
                });
            }
        }
    }

    private void goToMessagingWithUser(String chosenUserId) {
        String otherUserId = userIsCreator ? chosenUserId : offer.getUserId();
        Database.getInstance().read(Database.USERS_PATH, otherUserId,
                new ValueListener<User>() {
                    @Override
                    public void onDataChange(User value) {
                        replaceCentralFragment(
                                FragmentConverter.createMessagingActivityWithOfferAndUser(
                                        offer, value));
                    }

                    @Override
                    public void onCancelled(DbError error) {
                        // do nothing
                    }
                }, User.class);
    }
}
