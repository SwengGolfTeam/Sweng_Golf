package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.notification.Notification;
import ch.epfl.sweng.swenggolf.notification.NotificationManager;
import ch.epfl.sweng.swenggolf.notification.NotificationType;
import ch.epfl.sweng.swenggolf.notification.NotificationsActivity;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.statistics.UserStats;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class NotificationsDbNotWorkingTest {
    @Rule
    public IntentsTestRule<MainMenuActivity> activityTestRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);
    private User user = FilledFakeDatabase.getUser(0);

    /**
     * Sets the user and wait a moment.
     */
    @Before
    public void setUser() {
        Config.setUser(user);
        UserStats initStats = new UserStats();
        initStats.write(user.getUserId());
        // wait for last toast to disappear
        waitFor(1000);
    }

    @Test
    public void firebaseshowsToastIfErrorLoadingNotifications() {
        Database.setDebugDatabase(new FakeDatabase(false));
        goToNotifications();
        TestUtility.testToastShow(activityTestRule, R.string.notif_error);
    }

    @Test
    public void showsToastIfErrorDeletingNotification() {
        FakeDatabase db = new FakeDatabase(true);
        Database.setDebugDatabase(db);
        UserStats initStats = new UserStats();
        initStats.write(user.getUserId());
        // write a notification for the user
        NotificationManager.addPendingNotification(user.getUserId(),
                new Notification(NotificationType.TEST, null, null));
        goToNotifications();
        waitFor(500);
        db.setWorking(false);
        onView(withId(R.id.clear)).perform(click());
        TestUtility.testToastShow(activityTestRule, R.string.notif_delete_error);
    }

    private void goToNotifications() {
        activityTestRule.launchActivity(new Intent());
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment, new NotificationsActivity())
                .commit();
    }

    private void waitFor(int miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
