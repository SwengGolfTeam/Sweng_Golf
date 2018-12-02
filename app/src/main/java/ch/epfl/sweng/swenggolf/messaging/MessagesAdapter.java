package ch.epfl.sweng.swenggolf.messaging;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.offer.answer.Answer;
import ch.epfl.sweng.swenggolf.offer.answer.Answers;
import ch.epfl.sweng.swenggolf.offer.answer.ListAnswerAdapter;
import ch.epfl.sweng.swenggolf.profile.User;

public class MessagesAdapter extends RecyclerView.Adapter<ListAnswerAdapter.AnswerViewHolder> {
    private Answers answers = new Answers(new ArrayList<Answer>(), -1);
    private User otherUser;

    public MessagesAdapter(User otherUser) {
        this.otherUser = otherUser;
    }

    /**
     * Gets the messages.
     *
     * @return the messages, as an Answers object
     */
    public Answers getAnswers() {
        return answers;
    }

    /**
     * Sets the answers field.
     *
     * @param answers the new answers
     */
    public void setAnswers(Answers answers) {
        this.answers = answers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListAnswerAdapter.AnswerViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.reactions_others, viewGroup, false);
        return new ListAnswerAdapter.AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ListAnswerAdapter.AnswerViewHolder answerViewHolder, int i) {
        final Answer answer = answers.getAnswerList().get(i);
        boolean isYou = answer.getUserId().equals(Config.getUser().getUserId());
        User thisUser = isYou ? Config.getUser() : otherUser;

        TextView description = (TextView) answerViewHolder.getFieldTwo();
        description.setText(answer.getDescription());

        TextView userName = (TextView) answerViewHolder.getFieldOne();
        userName.setText(thisUser.getUserName());
        ImageView userPic = (ImageView) answerViewHolder.getFieldThree();
        Picasso.with(userPic.getContext())
                .load(Uri.parse(thisUser.getPhoto()))
                .fit().into(userPic);
    }

    @Override
    public int getItemCount() {
        return answers.getAnswerList().size();
    }
}
