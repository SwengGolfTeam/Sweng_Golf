package ch.epfl.sweng.swenggolf.notification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.ThreeFieldsViewHolder;

/**
 * Adapter of the Notification recyclerview.
 */
public class NotificationsAdapter
        extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {
    private List<Notification> notifications;
    private ItemClickListener viewHolderOnClickListener;

    /**
     * Constructs an adapter for the RecyclerView in NotificationsActivity.
     *
     * @param viewHolderOnClickListener a listener indicating what to do when clicking
     *                                  on items of the recycler view
     */
    public NotificationsAdapter(ItemClickListener viewHolderOnClickListener) {
        if (viewHolderOnClickListener == null) {
            throw new IllegalArgumentException();
        }
        this.notifications = new ArrayList<>();
        this.viewHolderOnClickListener = viewHolderOnClickListener;
    }

    /**
     * Sets the notifications field.
     *
     * @param notifications the new notifications
     */
    public void setNotifications(List<Notification> notifications) {
        this.notifications = new ArrayList<>(notifications);
        notifyDataSetChanged();
    }

    /**
     * Returns the list of notifications.
     *
     * @return the list of notifications, immutable
     */
    public List<Notification> getNotifications() {
        return Collections.unmodifiableList(this.notifications);
    }

    @Override
    public NotificationsAdapter.NotificationViewHolder onCreateViewHolder(ViewGroup parent,
                                                                          int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification, parent, false);
        return new NotificationViewHolder(view, R.id.notification_text,
                R.id.notification_icon, R.id.clear);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int i) {
        final Notification notification = notifications.get(i);
        final User currentUser = Config.getUser();

        TextView text = (TextView) notificationViewHolder.getFieldOne();
        ImageView icon = (ImageView) notificationViewHolder.getFieldTwo();
        final Context context = text.getContext();

        setContent(notification, text, icon, context);

        ImageButton close = (ImageButton) notificationViewHolder.getFieldThree();
        setCloseClickListener(notification, currentUser, context, close);

        notificationViewHolder.setItemClickListener(viewHolderOnClickListener);
    }

    private void setCloseClickListener(final Notification notification, final User currentUser,
                                       final Context context, ImageButton close) {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompletionListener removeListener = new CompletionListener() {
                    @Override
                    public void onComplete(DbError error) {
                        if (error == DbError.NONE) {
                            notifications.remove(notification);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, context.getResources()
                                    .getString(R.string.notif_delete_error), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                };
                NotificationManager.removePendingNotification(currentUser.getUserId(),
                        notification.getUuid(), removeListener);
            }
        });
    }

    private void setContent(Notification notification, TextView text,
                            ImageView icon, Context context) {
        switch (notification.getType()) {
            case ANSWER_CHOSEN:
                text.setText(getAnswerText(true, context, notification));
                icon.setImageResource(R.drawable.ic_favorite_black_24dp);
                break;
            case ANSWER_POSTED:
                text.setText(getAnswerText(false, context, notification));
                icon.setImageResource(R.drawable.ic_comment_black_24dp);
                break;
            case FOLLOW:
                text.setText(context.getString(R.string.notif_follow,
                        notification.getUserName()));
                icon.setImageResource(R.drawable.ic_star_black_24dp);
                break;
            case LEVEL_GAINED:
                text.setText(context.getString(R.string.notif_level_gained));
                icon.setImageResource(R.drawable.ic_exposure_plus_1_black_24dp);
                break;
            default:
                text.setText("TEST");
        }
    }

    @NonNull
    private String getAnswerText(boolean answerChosen, Context context, Notification notification) {
        int id = answerChosen ? R.string.notif_answer_chosen : R.string.notif_answer_posted;
        return context.getString(id,
                notification.getUserName(),
                notification.getOfferName());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    /**
     * Representation of a row of the recyclerview.
     */
    public static class NotificationViewHolder extends ThreeFieldsViewHolder
            implements View.OnClickListener {
        private ItemClickListener listener;

        /**
         * Create a new ThreeFieldsViewHolder.
         *
         * @param itemView           the view of the item
         * @param notificationTextId the text id of the notification
         * @param notificationIconId the icon id of the notification
         * @param crossId            the cross id
         */
        public NotificationViewHolder(@NonNull View itemView, int notificationTextId,
                                      int notificationIconId, int crossId) {
            super(itemView, notificationTextId, notificationIconId, crossId);
            itemView.setOnClickListener(this);
        }

        /**
         * Set the ItemClickListener.
         *
         * @param listener the corresponding ItemclickListener
         */
        public void setItemClickListener(ItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }


}
