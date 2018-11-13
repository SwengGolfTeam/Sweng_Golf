package ch.epfl.sweng.swenggolf.offer;

import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
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
import ch.epfl.sweng.swenggolf.storage.Storage;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;
import ch.epfl.sweng.swenggolf.tools.ViewUserFiller;


public class ShowOfferActivity extends FragmentConverter {

    private boolean userIsCreator;
    private Offer offer;
    private final Answers defaultAnswers = new Answers(new ArrayList<Answer>(), -1);
    private ListAnswerAdapter mAdapter;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setToolbar(R.drawable.ic_baseline_arrow_back_24px, R.string.button_show_offers);
        assert getArguments() != null;
        View inflated = inflater.inflate(R.layout.activity_show_offer, container, false);
        userIsCreator = Config.getUser().getUserId().equals(offer.getUserId());
        setContents(inflated);
        setRecyclerView(inflated);
        fetchAnswers();
        setAnswerToPost(inflated);
        return inflated;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        offer = getArguments().getParcelable("offer");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText comment = mView.findViewById(R.id.answer_description);
        comment.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(Answer.COMMENT_MAX_LENGTH)});
    }

    private void setContents(View inflated) {
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
            Picasso.with(this.getContext())
                    .load(Uri.parse(offer.getLinkPicture()))
                    .into(offerPicture);
        } else {
            offerPicture.getLayoutParams().height = 0;
        }

    }

    private void fetchAnswers() {
        ValueListener<Answers> answerListener = new ValueListener<Answers>() {
            @Override
            public void onDataChange(Answers value) {
                if (value != null) {
                    mAdapter.setAnswers(value);
                } else {
                    mAdapter.setAnswers(defaultAnswers);
                }
            }

            @Override
            public void onCancelled(DbError error) {
                Log.d(error.toString(), "Unable to load answers from database");
            }
        };
        Database.getInstance().read("/answers", offer.getUuid(), answerListener, Answers.class);
    }

    private ValueListener<User> createFiller(final View inflated) {
        return new ValueListener<User>() {
            @Override
            public void onDataChange(User value) {
                TextView userName = inflated.findViewById(R.id.user_name_);
                userName.setText(value.getUserName());
                ImageView userPic = inflated.findViewById(R.id.user_pic_);
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

    private void setAnswerToPost(final View inflated) {
        LinearLayout mLayout = inflated.findViewById(R.id.list_answers);

        LayoutInflater mInflater = getLayoutInflater();
        mView = mInflater.inflate(R.layout.reaction_you, mLayout, false);
        mLayout.addView(mView);

        ValueListener<User> vlUser = createFiller(inflated);

        Button post = mView.findViewById(R.id.post_button);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postAnswer(v);
            }
        });

        DatabaseUser.getUser(vlUser, Config.getUser().getUserId());
    }

    /**
     * Adds a new answer to the list of answers of the offer.
     *
     * @param view the button that got clicked
     */
    public void postAnswer(View view) {
        EditText editText = findViewById(R.id.answer_description);
        Answers answers = mAdapter.getAnswers();
        answers.getAnswerList()
                .add(new Answer(Config.getUser().getUserId(), editText.getText().toString()));
        Database.getInstance().write(Database.ANSWERS_PATH, offer.getUuid(), answers);
        editText.getText().clear();
        mAdapter.notifyDataSetChanged();
    }

    private void setRecyclerView(View inflated) {
        RecyclerView mRecyclerView = inflated.findViewById(R.id.answers_recycler_view);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setNestedScrollingEnabled(false);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new ListAnswerAdapter(defaultAnswers, offer);
        // Add dividing line
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (userIsCreator) {
            inflater.inflate(R.menu.menu_show_offer, menu);
        } else {
            inflater.inflate(R.menu.menu_empty, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                getFragmentManager().beginTransaction()
                        .replace(R.id.centralFragment, new ListOfferActivity()).commit();
                return true;
            }
            case R.id.button_modify_offer: {
                replaceCentralFragment(createOfferActivityWithOffer(offer));
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

    /**
     * Display the Alert Dialog for the delete.
     */
    public void showDeleteAlertDialog() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this.getContext());
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
                    Toast.makeText(getContext(), R.string.offer_deleted,
                            Toast.LENGTH_SHORT).show();
                    replaceCentralFragment(new ListOfferActivity());
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

}
