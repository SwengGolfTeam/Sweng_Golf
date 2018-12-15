package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.v4.app.Fragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.notification.Notification;
import ch.epfl.sweng.swenggolf.notification.NotificationManager;
import ch.epfl.sweng.swenggolf.notification.NotificationType;
import ch.epfl.sweng.swenggolf.notification.NotificationsActivity;
import ch.epfl.sweng.swenggolf.notification.NotificationsAdapter;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.ShowOfferActivity;
import ch.epfl.sweng.swenggolf.offer.create.CreateOfferActivity;
import ch.epfl.sweng.swenggolf.profile.Badge;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

public class NotificationsTest {

    @Rule
    public IntentsTestRule<MainMenuActivity> activityTestRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);
    private User user1 = FilledFakeDatabase.getUser(2);
    private User user2 = FilledFakeDatabase.getUser(3);
    private Offer offer = FilledFakeDatabase.getOfferOfUser(user2.getUserId());
    private Notification notif = new Notification(NotificationType.ANSWER_POSTED, user1, offer);

    /**
     * Set up a fake database, a fake user, and launches activity.
     */
    @Before
    public void setUp() {
        Config.setUser(user1);
        Database.setDebugDatabase(FakeDatabase.fakeDatabaseCreator());
        activityTestRule.launchActivity(new Intent());
    }

    @Test
    public void refreshActuallyRefreshes() throws InterruptedException {
        NotificationsActivity notifications = new NotificationsActivity();
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment, notifications).commit();
        Thread.sleep(1000);
        NotificationManager.addPendingNotification(user1.getUserId(), notif);
        onView(withId(R.id.message_empty)).check(matches(isDisplayed()));
        onView(withId(R.id.refresh_notifications)).perform(swipeDown());
        onView(withId(R.id.message_empty)).check(matches(not(isDisplayed())));
    }

    @Test
    public void followNotifIsSentAndRedirectsToProfile() {
        postFollowNotification();
        String followMessage = activityTestRule.getActivity()
                .getString(R.string.notif_follow, user1.getUserName());
        ViewInteraction notification = onView(withText(followMessage));
        notification.check(matches(isDisplayed()));
        // check that it shows the profile when clicking on it
        notification.perform(click());
        checkThatWeAreAt(ProfileActivity.class.getName(), R.id.name, user1.getUserName());
    }

    @Test
    public void answerPostedNotifIsSentAndRedirectsToOffer() {
        goToOfferAndPostAnswer("I can help you!");

        // change user
        setUserAndGoToNotifications(user2);
        String answerMessage = activityTestRule.getActivity()
                .getString(R.string.notif_answer_posted, user1.getUserName(), offer.getTitle());
        checkNotificationIsThereAndLeadsToOffer(answerMessage, offer.getTitle());
    }

    @Test
    public void answerChosenNotifIsSentAndRedirectsToOffer() {
        goToOfferAndPostAnswer("I can help you!");
        //change user and go to offer
        Config.setUser(user2);
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment, FragmentConverter.createShowOfferWithOffer(offer))
                .commit();
        TestUtility.showOfferCustomScrollTo();
        onView(withContentDescription("fav0")).perform(click());
        try {
            onView(withContentDescription("fav0")).perform(click()); // try another time
        } catch (NoMatchingViewException e) {
            // do nothing
        }
        onView(withText(android.R.string.yes)).perform(click());
        // go back to user1 to check notification
        setUserAndGoToNotifications(user1);
        String answerChosenMessage = activityTestRule.getActivity()
                .getString(R.string.notif_answer_chosen, user2.getUserName(), offer.getTitle());
        checkNotificationIsThereAndLeadsToOffer(answerChosenMessage, offer.getTitle());
    }

    @Test
    public void displayMessageIfNoNotification() {
        setUserAndGoToNotifications(user1);
        onView(withId(R.id.message_empty)).check(matches(isDisplayed()));
    }

    @Test
    public void hasEmptyConstructorForFirebase() {
        new Notification();
    }

    @Test
    public void canDeleteNotification() {
        postFollowNotification();
        onView(withId(R.id.clear)).perform(click());
        Database.getInstance().readList(NotificationManager.getNotificationPath(user2.getUserId()),
                new ValueListener<List<Notification>>() {
                    @Override
                    public void onDataChange(List<Notification> value) {
                        assertTrue(value.isEmpty());
                    }

                    @Override
                    public void onCancelled(DbError error) {
                        // db should work
                    }
                }, Notification.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotCreateAdapterWithNullListener() {
        new NotificationsAdapter(null);
    }

    @Test
    public void levelUpNotificationWorks() {
        // reset count
        addPointsToUser(-user1.getPoints(), user1);
        addPointsToUser(Badge.LEVEL_SPACE, user1);
        onView(withText(activityTestRule.getActivity().getString(R.string.notif_level_gained)))
                .check(matches(isDisplayed())).perform(click());
        checkThatWeAreAt(ProfileActivity.class.getName(), R.id.name, user1.getUserName());
    }

    @Test
    public void getsNotificationWhenFriendPostsAnOffer() {
        // write info that user2 follows user1 into the database
        Database.getInstance().write(Database.FOLLOWERS_PATH, user2.getUserId(), user1.getUserId());

        // make user1 create an offer
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment, new CreateOfferActivity())
                .commit();
        String offerTitle = "This is an offer";
        onView(withId(R.id.offer_name)).perform(typeText(offerTitle))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.offer_description))
                .perform(typeText("I want my friends to see it!"))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.button_create_offer)).perform(scrollTo(), click());

        // check user2 received the correct notification
        setUserAndGoToNotifications(user2);
        String friendPostedOffer = activityTestRule.getActivity()
                .getString(R.string.notif_friend_posted, user1.getUserName());
        checkNotificationIsThereAndLeadsToOffer(friendPostedOffer, offerTitle);

    }

    private void addPointsToUser(int numPoints, User user) {
        user.addPoints(numPoints);
        setUserAndGoToNotifications(user);
        // let it sink in, otherwise it goes too fast
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void postFollowNotification() {
        ProfileActivity user2Profile = FragmentConverter.createShowProfileWithProfile(user2);
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment, user2Profile)
                .commit();
        // follow user2
        ViewInteraction followButton = onView(withId(R.id.follow));
        followButton.perform(click());
        assertTrue(user2Profile.isFollowing());

        // change to other user
        setUserAndGoToNotifications(user2);
    }

    private void setUserAndGoToNotifications(User otherUser) {
        Config.setUser(otherUser);
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment, new NotificationsActivity())
                .commit();
    }

    private void checkThatWeAreAt(String activityName, int contentId, String contentName) {
        Fragment fragment = activityTestRule.getActivity()
                .getSupportFragmentManager().getFragments().get(0);
        assertThat(fragment.getClass().getName(), is(activityName));
        onView(withId(contentId)).check(matches(withText(contentName)));
    }

    private void goToOfferAndPostAnswer(String message) {
        if (offer == null) {
            fail("Please choose a user that wrote an offer");
        }
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment, FragmentConverter.createShowOfferWithOffer(offer))
                .commit();
        TestUtility.addAnswer(message);
    }

    private void checkNotificationIsThereAndLeadsToOffer(String message, String offerTitle) {
        ViewInteraction notification = onView(withText(message));
        notification.check(matches(isDisplayed()));
        // check that it shows the offer when clicking on it
        notification.perform(click());
        checkThatWeAreAt(ShowOfferActivity.class.getName(),
                R.id.show_offer_title, offerTitle);
    }

}
