package ch.epfl.sweng.swenggolf.notification;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.profile.User;

public final class NotificationManager {

    public static void addPendingNotification(String destinatorId, Notification notification) {
        // TODO add a listener ?
        Database db = Database.getInstance();
        db.write(getNotificationPath(destinatorId), notification.getUuid(), notification);
    }

    public static void removePendingNotification(String destinatorId, String notificationId) {
        // TODO how can we keep track of the notification Id ???
    }

    public static String getNotificationPath(String userId) {
        return Database.NOTIFICATION_PATH + "/" + userId;
    }
}
