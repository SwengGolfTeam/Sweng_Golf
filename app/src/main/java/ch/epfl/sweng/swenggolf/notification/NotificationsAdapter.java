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

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.ThreeFieldsViewHolder;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {
    private List<Notification> notifications;

    public static class NotificationViewHolder extends ThreeFieldsViewHolder {

        public NotificationViewHolder(@NonNull View itemView, int notificationTextId, int notificationIconId, int crossId) {
            super(itemView, notificationTextId, notificationIconId, crossId);
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
        return new NotificationViewHolder(view, R.id.notification_text, R.id.notification_icon, R.id.clear);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int i) {
        final Notification notification = notifications.get(i);
        final User currentUser = Config.getUser();

        TextView text = (TextView) notificationViewHolder.getFieldOne();
        ImageView icon = (ImageView) notificationViewHolder.getFieldTwo();
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

        ImageButton close = (ImageButton) notificationViewHolder.getFieldThree();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager.removePendingNotification(currentUser.getUserId(), notification.getUuid());
                // TODO can we do this better?
                Notification concerned = null;
                for (Notification n : notifications) {
                    if (n.getUuid().equals(notification.getUuid())) {
                        concerned = n;
                    }
                }
                notifications.remove(concerned);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }


}
