package ch.epfl.sweng.swenggolf.notification;

import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;

/**
 * Manager of the notifications.
 */
public final class NotificationManager {

    private NotificationManager() {
    }

    /**
     * Add pending Notification.
     * @param destinatorId the id of the destinator
     * @param notification the corresponding notification
     */
    public static void addPendingNotification(String destinatorId, Notification notification) {
        Database db = Database.getInstance();
        db.write(getNotificationPath(destinatorId), notification.getUuid(), notification);
    }

    /**
     * Remove the pending Notification.
     * @param destinatorId the id of the destinator
     * @param notificationId the id of the notification
     * @param listener the listener
     */
    public static void removePendingNotification(
            String destinatorId, String notificationId, CompletionListener listener) {
        Database db = Database.getInstance();
        db.remove(getNotificationPath(destinatorId), notificationId, listener);
    }

    /**
     * Get the path of the Notification
     * @param userId the user id
     * @return the corresponding path
     */
    public static String getNotificationPath(String userId) {
        return Database.NOTIFICATION_PATH + "/" + userId;
    }
}
