package ch.epfl.sweng.swenggolf.offer;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.tools.ThreeFieldsViewHolder;

public class ListAnswerAdapter extends RecyclerView.Adapter<ListAnswerAdapter.AnswerViewHolder> {
    private Answers answers;
    private Offer offer;
    private Map<String, User> dictionary; // to limit accesses to the database

    public static class AnswerViewHolder extends ThreeFieldsViewHolder {

        public AnswerViewHolder(View view) {
            super(view, R.id.user_name, R.id.answer_description, R.id.user_pic);

            //TODO test
            ImageButton favButton = view.findViewById(R.id.favorite);
            favButton.setTag(this);
            /*favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    offer.setPositionFavorite(getLayoutPosition());
                    Database.getInstance().write("/offers", offer.getUuid(), offer);
                    //notifyDataSetChanged();
                }
            });*/
        }
    }

    /**
     * Constructs a ListAnswerAdapter for the RecyclerView.
     *
     * @param answers the objet containing the list of answers to be displayed
     */
    public ListAnswerAdapter(Answers answers, Offer offer) {
        if (answers == null || answers.getAnswers() == null || offer == null) {
            throw new IllegalArgumentException();
        }
        this.answers = answers;
        this.offer = offer;
        dictionary = new HashMap<>();
    }

    // TODO c'est vraiment super moche...
    public void setAnswers(Answers answers) {
        this.answers = answers;
        notifyDataSetChanged();
    }

    public Answers getAnswers() {
        return answers;
    }


    @Override
    public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reactions_others, parent, false);
        return new AnswerViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final AnswerViewHolder holder, int position) {
        final Answer answer = answers.getAnswers().get(position);

        // get the user data from database
        ValueListener<User> vlUser = new ValueListener<User>() {
            @Override
            public void onDataChange(User value) {
                TextView userName = (TextView) holder.getFieldOne();
                userName.setText(value.getUserName());
                ImageView userPic = (ImageView) holder.getFieldThree();
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
        DatabaseUser.getUser(vlUser, answer.getUserId());

        TextView description = (TextView) holder.getFieldTwo();
        description.setText(answer.getDescription());

        ImageButton favButton = holder.getContainer().findViewById(R.id.favorite);

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                if (answers.getFavoritePos() != pos) {
                    answers.setFavoritePos(pos);
                } else {
                    answers.setFavoritePos(-1);
                }
                Database.getInstance().write("/answers", offer.getUuid(), answers);
                notifyDataSetChanged();
            }
        });

        boolean isAuthor = offer.getUserId().equals(Config.getUser().getUserId());
        if (!isAuthor) {
            favButton.setClickable(false);
        }
        if (answers.getFavoritePos() == position) {
            favButton.setImageResource(R.drawable.ic_favorite);
        } else if (isAuthor) {
            favButton.setImageResource(R.drawable.ic_favorite_border);
        }




        Log.d("OFFER", "notify");


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return answers.getAnswers().size();
    }
}
