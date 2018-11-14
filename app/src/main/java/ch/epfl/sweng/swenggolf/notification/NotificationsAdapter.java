package ch.epfl.sweng.swenggolf.notification;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.swenggolf.R;

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

    @Override
    public NotificationsAdapter.NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification, parent, false);
        return new NotificationViewHolder(view, R.id.notification_text, R.id.notification_icon);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int i) {
        final Notification notification = notifications.get(i);

        TextView text = notificationViewHolder.getNotificationText();
        ImageView icon = notificationViewHolder.getNotificationIcon();
        String message;
        switch (notification.getType()) {
            // TODO all cases and get the correct string from R.string + display the correct icon
        }


    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
