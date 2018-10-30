package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;


public class ProfileActivityOtherUserTest {

    final User user = new User("Patrick", "Vetterli", "1234567890", "", "tea");
    final User otherUser = new User("other", "user", "is", "a", "placeholder");
    private FakeDatabase database = new FakeDatabase(true);

    @Rule
    public final IntentsTestRule<ProfileActivity> mActivityRule =
            new IntentsTestRule<>(ProfileActivity.class, false, false);

    /**
     * Set up a fake database and user.
     */
    @Before
    public void setUp() {
        Config.isTest();
        Config.setUser(user);
        Database.setDebugDatabase(database);
        mActivityRule.launchActivity(
                new Intent().putExtra("ch.epfl.sweng.swenggolf.user", otherUser));
    }

    @Test
    public void editButtonGone() {
        onView(withId(R.id.edit)).check(matches(withEffectiveVisibility(
                ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void canFollow() {
        testToast("You are now following ");

        ValueListener<String> listener = new ValueListener<String>() {
            @Override
            public void onDataChange(String value) {
                assertNotNull(value);
            }

            @Override
            public void onCancelled(DbError error) {
                fail();
            }
        };

        final String path = Database.FOLLOWERS_PATH + "/" + user.getUserId();

        final String userId = otherUser.getUserId();
        Database.getInstance().read(path, userId, listener, String.class);

        //Clean up database
        CompletionListener completionListener = new CompletionListener() {
            @Override
            public void onComplete(DbError error) {

            }
        };
        database.remove(path,userId,completionListener);
    }

    private void testToast(String s) {
        String match = s + otherUser.getUserName();
        onView(withId(R.id.follow)).perform(click());
        TestUtility.testToastShow(mActivityRule, match);
    }

    @Test
    public void canNotFollowShowError() {
        database.setWorking(false);
        testToast("Could not follow ");
        database.setWorking(true);
    }

    @Test
    public void unfollowShowEmptyStar() {

        ViewInteraction followButton = onView(withId(R.id.follow));
        followButton.perform(click());
        followButton.perform(click());
        followButton.check(matches(withTagValue(
                equalTo((Object) android.R.drawable.btn_star_big_off))));

    }

    @Test
    public void showEmptyStarWhenNotFollowing() {
        onView(withId(R.id.follow)).check(matches(withTagValue(
                equalTo((Object) android.R.drawable.btn_star_big_off))));
    }

}
