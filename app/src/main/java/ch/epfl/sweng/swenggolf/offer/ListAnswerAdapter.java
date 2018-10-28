package ch.epfl.sweng.swenggolf.offer;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.tools.ThreeFieldsViewHolder;

public class ListAnswerAdapter extends RecyclerView.Adapter<ListAnswerAdapter.MyViewHolder> {
    private List<Answer> answerList;

    public static class MyViewHolder extends ThreeFieldsViewHolder {

        public MyViewHolder(View view) {
            super(view, R.id.answer_description, R.id.user_name, R.id.user_pic);
        }
    }

    /**
     * Constructs a ListAnswerAdapter for the RecyclerView.
     *
     * @param answerList the list of offers to be displayed
     */
    public ListAnswerAdapter(List<Answer> answerList) {
        if (answerList == null) {
            throw new IllegalArgumentException();
        }
        this.answerList = answerList;
    }


    @Override
    public ListAnswerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reactions, parent, false);
        return new ListAnswerAdapter.MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ListAnswerAdapter.MyViewHolder holder, int position) {
        final Answer answer = answerList.get(position);

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

                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DbError error) {
                Log.d(error.toString(), "Unable to load user from database");
            }
        };
        DatabaseUser.getUser(vlUser, answer.getUserId());

        TextView description = (TextView) holder.getFieldTwo();
        description.setText(answer.getDescription());


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return answerList.size();
    }
}
