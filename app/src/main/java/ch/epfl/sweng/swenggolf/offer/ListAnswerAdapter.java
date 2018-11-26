package ch.epfl.sweng.swenggolf.offer;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.notification.Notification;
import ch.epfl.sweng.swenggolf.notification.NotificationManager;
import ch.epfl.sweng.swenggolf.notification.NotificationType;
import ch.epfl.sweng.swenggolf.profile.Badge;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.ThreeFieldsViewHolder;

import static ch.epfl.sweng.swenggolf.offer.Answers.NO_FAVORITE;
import static ch.epfl.sweng.swenggolf.profile.PointType.RESPOND_OFFER;

/**
 * Adapter of the RecyclerVew of the AnswerList.
 */
public class ListAnswerAdapter extends RecyclerView.Adapter<ListAnswerAdapter.AnswerViewHolder> {
    private static final int HEART_FULL = R.drawable.ic_favorite;
    private static final int HEART_EMPTY = R.drawable.ic_favorite_border;
    private Answers answers;
    private Offer offer;
    private boolean isClosed;

    /**
     * Constructs a ListAnswerAdapter for the RecyclerView.
     *
     * @param answers the objet containing the list of answers to be displayed
     */
    public ListAnswerAdapter(Answers answers, Offer offer) {
        if (answers == null || answers.getAnswerList() == null || offer == null) {
            throw new IllegalArgumentException();
        }
        this.answers = answers;
        this.offer = offer;
        this.isClosed = false;
    }

    /**
     * Gets the answers.
     *
     * @return the answers
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

    /**
     * Close the answers so that we can't choose a favorite anymore.
     */
    public void closeAnswers() {
        isClosed = true;
        notifyDataSetChanged();
        Log.d("ANSWERS", "Answers are now closed");
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
        final Answer answer = answers.getAnswerList().get(position);

        // get the user data from database
        ValueListener<User> vlUser = new ValueListener<User>() {
            @Override
            public void onDataChange(User value) {
                setUserData(value, holder);
            }

            @Override
            public void onCancelled(DbError error) {
                Log.d(error.toString(), "Unable to load user from database");
            }
        };
        DatabaseUser.getUser(vlUser, answer.getUserId());

        TextView description = (TextView) holder.getFieldTwo();
        description.setText(answer.getDescription());
        description.setContentDescription("description" + Integer.toString(position));

        setupFavorite(holder, position);

    }

    private void setUserData(User value, AnswerViewHolder holder) {
        TextView userName = (TextView) holder.getFieldOne();
        userName.setText(value.getUserName());
        userName.setContentDescription(
                "username" + Integer.toString(holder.getAdapterPosition()));
        ImageView userPic = (ImageView) holder.getFieldThree();
        Picasso.with(userPic.getContext())
                .load(Uri.parse(value.getPhoto()))
                .placeholder(R.drawable.gender_neutral_user1)
                .fit().into(userPic);
        userPic.setContentDescription("pic"
                + Integer.toString(holder.getAdapterPosition()));
        holder.itemView.setBackgroundColor(Color.parseColor(Badge.getColor(value.getPoints())));
    }

    private void setupFavorite(final AnswerViewHolder holder, int position) {
        ImageButton favButton = holder.getContainer().findViewById(R.id.favorite);
        favButton.setContentDescription("fav" + Integer.toString(position));
        if (!isClosed) {
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Context context = holder.getContainer().getContext();
                    int pos = holder.getAdapterPosition();
                    Dialog alertDialog = answers.getFavoritePos() != pos
                            ? acceptAnswerDialog(context, pos) : stepBackDialog(context);
                    alertDialog.show();
                }
            });
        }

        boolean isAuthor = offer.getUserId().equals(Config.getUser().getUserId());
        if (!isAuthor || isClosed) {
            favButton.setClickable(false);
        }

        if (answers.getFavoritePos() == position) {
            favButton.setImageResource(HEART_FULL);
            favButton.setTag(HEART_FULL);
        } else if (isAuthor && !isClosed) {
            favButton.setImageResource(HEART_EMPTY);
            favButton.setTag(HEART_EMPTY);
        } else {
            favButton.setImageResource(android.R.color.transparent);
            //TODO : maybe we need to add a tag
        }

        ownAnswerNotClickable(favButton, position);
    }


    private void ownAnswerNotClickable(ImageButton favButton, int position) {
        if (answers.getUserOfPosition(position).equals(offer.getUserId())) {
            favButton.setVisibility(View.INVISIBLE);
            favButton.setClickable(false);
        }
    }

    private Dialog createFavoriteDialog(Context context, final int pos, String title, String hint) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title)
                .setMessage(hint)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        writeFavPos(pos);

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // user cancelled the dialog
                    }
                });
        return builder.create();
    }

    private Dialog acceptAnswerDialog(Context context, final int pos) {
        return createFavoriteDialog(context, pos, context.getString(R.string.accept_favorite),
                context.getString(R.string.accept_favorite_question));
    }

    private Dialog stepBackDialog(Context context) {
        return createFavoriteDialog(context, NO_FAVORITE,
                context.getString(R.string.remove_favorite),
                context.getString(R.string.remove_favorite_question));
    }

    private void writeFavPos(final int pos) {
        final int previousFavorite = answers.getFavoritePos();
        answers.setFavoritePos(pos);

        Database.getInstance().write(Database.ANSWERS_PATH, offer.getUuid(), answers,
                new CompletionListener() {
                    @Override
                    public void onComplete(DbError error) {
                        if (error == DbError.NONE) {
                            if (previousFavorite != NO_FAVORITE) {
                                DatabaseUser.addPointsToUserId(-RESPOND_OFFER.getValue(),
                                        answers.getAnswerList().get(previousFavorite).getUserId());
                            }
                            if (pos != NO_FAVORITE) {
                                DatabaseUser.addPointsToUserId(RESPOND_OFFER.getValue(),
                                        answers.getAnswerList().get(pos).getUserId());
                                NotificationManager.addPendingNotification(
                                        answers.getAnswerList().get(pos).getUserId(),
                                        new Notification(NotificationType.ANSWER_CHOSEN,
                                                Config.getUser(), offer));
                            }
                        }
                    }
                });
        notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return answers.getAnswerList().size();
    }

    public static class AnswerViewHolder extends ThreeFieldsViewHolder {

        public AnswerViewHolder(View view) {
            super(view, R.id.user_name, R.id.answer_description, R.id.user_pic);
        }
    }
}
