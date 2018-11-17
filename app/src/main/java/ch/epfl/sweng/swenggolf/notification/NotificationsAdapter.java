package ch.epfl.sweng.swenggolf.notification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.profile.User;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {
    private List<Notification> notifications;

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private TextView notificationText;
        private ImageView notificationIcon;

        public NotificationViewHolder(@NonNull View itemView, int notificationTextId, int notificationIconId) {
            super(itemView);
            this.notificationText = itemView.findViewById(notificationTextId);
            this.notificationIcon = itemView.findViewById(notificationIconId);
        }

        public TextView getNotificationText() {
            return notificationText;
        }

        public ImageView getNotificationIcon() {
            return notificationIcon;
        }
    }

    public NotificationsAdapter(List<Notification> notifications) {
        if (notifications == null) {
            throw new IllegalArgumentException();
        }
        this.notifications = new ArrayList<>(notifications);
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
        return new NotificationViewHolder(view, R.id.notification_text, R.id.notification_icon);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int i) {
        final Notification notification = notifications.get(i);

        User currentUser = Config.getUser();
        TextView text = notificationViewHolder.getNotificationText();
        ImageView icon = notificationViewHolder.getNotificationIcon();
        Context context = text.getContext();
        String message;
        int imageResource;
        switch (notification.getType()) {
            case ANSWER_CHOSEN:
                message = context.getString(R.string.notif_answer_chosen,
                        currentUser.getUserName(),
                        notification.getConcernedOfferName());
                imageResource = R.drawable.ic_favorite_black_24dp;
                break;
            case ANSWER_POSTED:
                message = context.getString(R.string.notif_answer_posted,
                        currentUser.getUserName(),
                        notification.getConcernedOfferName());
                imageResource = R.drawable.ic_comment_black_24dp;
                break;
            case FOLLOW:
                message = context.getString(R.string.notif_follow,
                        currentUser.getUserName());
                imageResource = R.drawable.ic_star_black_24dp;
                break;
            case POINTS_GAINED:
                // TODO
                message = "";
                imageResource = 0;
                break;
            default: // should never happen? -> throw an exception?
                message = "";
                imageResource = 0;
        }
        text.setText(message);
        icon.setImageResource(imageResource);



    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }


}
