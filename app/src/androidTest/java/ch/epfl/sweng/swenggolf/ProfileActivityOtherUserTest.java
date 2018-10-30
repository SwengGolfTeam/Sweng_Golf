package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import ch.epfl.sweng.swenggolf.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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

        Database.getInstance().read("/followers/" + user.getUserId(), otherUser.getUserId(), listener, String.class);
    }

    private void testToast(String s) {
        String match = s  + otherUser.getUserName();
        onView(withId(R.id.follow)).perform(click());
        TestUtility.testToastShow(mActivityRule,  match);
    }

    @Test
    public void canNotFollowShowError() {
        database.setWorking(false);
        testToast("Could not follow ");
        database.setWorking(true);
    }

}
