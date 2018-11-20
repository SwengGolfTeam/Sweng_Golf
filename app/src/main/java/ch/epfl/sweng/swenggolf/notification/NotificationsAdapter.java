package ch.epfl.sweng.swenggolf.notification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.ThreeFieldsViewHolder;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {
    private List<Notification> notifications;
    private ItemClickListener viewHolderOnClickListener;

    public static class NotificationViewHolder extends ThreeFieldsViewHolder implements View.OnClickListener {
        private ItemClickListener listener;

        public NotificationViewHolder(@NonNull View itemView, int notificationTextId, int notificationIconId, int crossId) {
            super(itemView, notificationTextId, notificationIconId, crossId);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    public NotificationsAdapter(List<Notification> notifications, ItemClickListener viewHolderOnClickListener) {
        if (notifications == null || viewHolderOnClickListener == null) {
            throw new IllegalArgumentException();
        }
        this.notifications = new ArrayList<>(notifications);
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

    @Override
    public NotificationsAdapter.NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification, parent, false);
        return new NotificationViewHolder(view, R.id.notification_text, R.id.notification_icon, R.id.clear);
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

        notificationViewHolder.setItemClickListener(viewHolderOnClickListener);
    }

    private void setContent(Notification notification, TextView text, ImageView icon, Context context) {
        String message;
        int imageResource;
        switch (notification.getType()) {
            case ANSWER_CHOSEN: // TODO too similar to ANSWER_POSTED ?!
                message = context.getString(R.string.notif_answer_chosen,
                        notification.getUserName(),
                        notification.getOfferName());
                imageResource = R.drawable.ic_favorite_black_24dp;
                break;
            case ANSWER_POSTED:
                message = context.getString(R.string.notif_answer_posted,
                        notification.getUserName(),
                        notification.getOfferName());
                imageResource = R.drawable.ic_comment_black_24dp;
                break;
            case FOLLOW:
                message = context.getString(R.string.notif_follow,
                        notification.getUserName());
                imageResource = R.drawable.ic_star_black_24dp;
                break;
            case LEVEL_GAINED:
                message = context.getString(R.string.notif_level_gained);
                imageResource = R.drawable.ic_exposure_plus_1_black_24dp;
                break;
            default:
                message = "TEST";
                imageResource = 0;
        }
        text.setText(message);
        icon.setImageResource(imageResource);
    }


    @Override
    public int getItemCount() {
        Log.d("NOTIF", Integer.toString(notifications.size()));
        return notifications.size();
    }


}
