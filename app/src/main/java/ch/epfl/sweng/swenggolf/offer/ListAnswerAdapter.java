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
import java.util.LinkedList;
import java.util.List;
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
    private List<Answer> answerList;
    private Map<String, User> dictionary;
    private Offer offer;

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
     * @param answerList the list of offers to be displayed
     */
    public ListAnswerAdapter(List<Answer> answerList, Offer offer) {
        if (answerList == null || offer == null) {
            throw new IllegalArgumentException();
        }
        this.answerList = answerList;
        dictionary = new HashMap<>();

        for (Answer answer : answerList) {
            // get the user data from database
            ValueListener<User> vlUser = new ValueListener<User>() {
                @Override
                public void onDataChange(User value) {
                    dictionary.put(value.getUserId(), value);
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DbError error) {
                    Log.d(error.toString(), "Unable to load user from database");
                }
            };
            DatabaseUser.getUser(vlUser, answer.getUserId());

        }

        this.offer = offer;
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
        final Answer answer = answerList.get(position);
        User user = dictionary.get(answer.getUserId());

        if (user != null) {
            TextView userName = (TextView) holder.getFieldOne();
            userName.setText(user.getUserName());
            ImageView userPic = (ImageView) holder.getFieldThree();
            Picasso.with(userPic.getContext())
                    .load(Uri.parse(user.getPhoto()))
                    .placeholder(R.drawable.gender_neutral_user1)
                    .fit().into(userPic);
        }

        TextView description = (TextView) holder.getFieldTwo();
        description.setText(answer.getDescription());

        ImageButton favButton = holder.getContainer().findViewById(R.id.favorite);
        boolean isAuthor = offer.getUserId().equals(Config.getUser().getUserId());
        if (!isAuthor) {
            favButton.setClickable(false);
        }
        if (offer.getPositionFavorite() == position) {
            favButton.setImageResource(R.drawable.ic_favorite);
        } else if (isAuthor) {
            favButton.setImageResource(R.drawable.ic_favorite_border);
        }


        Log.d("OFFER", "notify");


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return answerList.size();
    }
}
