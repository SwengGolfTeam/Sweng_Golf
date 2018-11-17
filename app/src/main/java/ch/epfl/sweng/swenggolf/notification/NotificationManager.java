package ch.epfl.sweng.swenggolf.notification;

import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;

public final class NotificationManager {

    private NotificationManager() {}

    public static void addPendingNotification(String destinatorId, Notification notification) {
        // TODO add a listener ?
        Database db = Database.getInstance();
        db.write(getNotificationPath(destinatorId), notification.getUuid(), notification);
    }

    public static void removePendingNotification(String destinatorId, String notificationId) {
        Database db = Database.getInstance();
        CompletionListener listener = new CompletionListener() {
            @Override
            public void onComplete(DbError error) {
                // TODO do what ??
            }
        };
        db.remove(getNotificationPath(destinatorId), notificationId, listener);
    }

    public static String getNotificationPath(String userId) {
        return Database.NOTIFICATION_PATH + "/" + userId;
    }
}
