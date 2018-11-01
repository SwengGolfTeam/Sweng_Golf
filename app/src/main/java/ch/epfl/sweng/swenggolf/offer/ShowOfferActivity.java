package ch.epfl.sweng.swenggolf.offer;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;
import ch.epfl.sweng.swenggolf.storage.Storage;
import ch.epfl.sweng.swenggolf.tools.ViewUserFiller;


public class ShowOfferActivity extends AppCompatActivity {

    private Offer offer;
    private static final Answers DEFAULT_ANSWERS = new Answers(new ArrayList<Answer>(), -1);
    private ListAnswerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_offer);
        offer = getIntent().getParcelableExtra("offer");
        if (!Config.getUser().getUserId().equals(offer.getUserId())) {
            ImageView buttonModify = findViewById(R.id.button_modify_offer);
            hideButton(buttonModify);
            ImageView buttonDelete = findViewById(R.id.button_delete_offer);
            hideButton(buttonDelete);
        }
        setContents();
        setRecyclerView();
        fetchAnswers();
        setAnswerToPost();
    }

    private void hideButton(ImageView button) {
        button.setVisibility(View.INVISIBLE);
        button.setClickable(false);
    }

    private void setContents() {
        TextView offerTitle = findViewById(R.id.show_offer_title);
        offerTitle.setText(offer.getTitle());

        final TextView offerAuthor = findViewById(R.id.show_offer_author);
        ViewUserFiller.fillWithUsername(offerAuthor, offer.getUserId());

        TextView offerTag = findViewById(R.id.show_offer_tag);
        offerTag.setText(offer.getTag().toString());


        TextView offerDescription = findViewById(R.id.show_offer_description);
        offerDescription.setText(offer.getDescription());

        if (!offer.getLinkPicture().isEmpty()) {
            ImageView offerPicture = findViewById(R.id.show_offer_picture);
            Picasso.with(this).load(Uri.parse(offer.getLinkPicture())).into(offerPicture);
        }

    }

    private void fetchAnswers() {
        ValueListener<Answers> answerListener = new ValueListener<Answers>() {
            @Override
            public void onDataChange(Answers value) {
                if (value != null) {
                    mAdapter.setAnswers(value);
                } else {
                    mAdapter.setAnswers(DEFAULT_ANSWERS);
                }
            }

            @Override
            public void onCancelled(DbError error) {
                Log.d(error.toString(), "Unable to load answers from database");
            }
        };
        Database.getInstance().read("/answers", offer.getUuid(), answerListener, Answers.class);
    }

    private void setAnswerToPost() {
        LinearLayout mLayout = findViewById(R.id.list_answers);

        LayoutInflater mInflater = getLayoutInflater();
        View mView = mInflater.inflate(R.layout.reaction_you, mLayout, false);
        mLayout.addView(mView);

        ValueListener<User> vlUser = new ValueListener<User>() {
            @Override
            public void onDataChange(User value) {
                TextView userName = findViewById(R.id.user_name_);
                userName.setText(value.getUserName());
                ImageView userPic = findViewById(R.id.user_pic_);
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
        DatabaseUser.getUser(vlUser, Config.getUser().getUserId());
    }

    /**
     * Adds a new answer to the list of answers of the offer.
     * @param view the button that got clicked
     */
    public void postAnswer(View view) {
        EditText editText = findViewById(R.id.answer_description_);
        Answers answers = mAdapter.getAnswers();
        answers.getAnswerList()
                .add(new Answer(Config.getUser().getUserId(), editText.getText().toString()));
        Database.getInstance().write(Database.ANSWERS_PATH, offer.getUuid(), answers);
        editText.getText().clear();
        mAdapter.notifyDataSetChanged();
    }

    private void setRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.answers_recycler_view);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setNestedScrollingEnabled(false);

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new ListAnswerAdapter(DEFAULT_ANSWERS, offer);
        // Add dividing line
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

    }

    /**
     * Launches the CreateOfferActivity using the current offer, which will trigger
     * subsequent parameters that will be used to modify it.
     *
     * @param view the view
     */
    public void modifyOffer(View view) {
        Intent intent = new Intent(this, CreateOfferActivity.class);
        intent.putExtra("offer", offer);
        startActivity(intent);
    }

    /**
     * Launches the DeleteOfferActivity using the current offer.
     *
     * @param view the view
     */
    public void deleteOffer(View view) {
        showDeleteAlertDialog();
    }

    /**
     * Display the Alert Dialog for the delete.
     */
    public void showDeleteAlertDialog() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this offer?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteOfferInDatabase();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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
        CompletionListener listener = new CompletionListener() {
            @Override
            public void onComplete(@Nullable DbError databaseError) {
                if (databaseError == DbError.NONE) {
                    Toast.makeText(ShowOfferActivity.this, R.string.offer_deleted,
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ShowOfferActivity.this, ListOfferActivity.class));
                    finish();
                }
            }

        };
        database.remove(Database.OFFERS_PATH, offer.getUuid(), listener);
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
                                     Intent intent = new Intent(ShowOfferActivity.this,
                                             ProfileActivity.class);
                                     intent.putExtra("ch.epfl.sweng.swenggolf.user", user);
                                     startActivity(intent);
                                 }

                                 @Override
                                 public void onCancelled(DbError error) {
                                     Toast.makeText(ShowOfferActivity.this,
                                             R.string.error_load_user, Toast.LENGTH_LONG).show();
                                 }
                             },
                offer.getUserId());

    }

}
