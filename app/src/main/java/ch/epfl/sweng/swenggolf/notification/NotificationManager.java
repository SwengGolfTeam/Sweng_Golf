package ch.epfl.sweng.swenggolf.notification;

import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;

public final class NotificationManager {

    private NotificationManager() {
    }

    public static void addPendingNotification(String destinatorId, Notification notification) {
        Database db = Database.getInstance();
        db.write(getNotificationPath(destinatorId), notification.getUuid(), notification);
    }

    public static void removePendingNotification(
            String destinatorId, String notificationId, CompletionListener listener) {
        Database db = Database.getInstance();
        db.remove(getNotificationPath(destinatorId), notificationId, listener);
    }

    public static String getNotificationPath(String userId) {
        return Database.NOTIFICATION_PATH + "/" + userId;
    }
}
