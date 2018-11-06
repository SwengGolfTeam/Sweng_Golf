package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.view.MenuItem;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public class ProfileActivityOtherUserTest {

    final User user = new User("Patrick", "Vetterli", "1234567890", "", "tea");
    final User otherUser = new User("other", "user", "is", "a", "placeholder");
    private FakeDatabase database = (FakeDatabase) FakeDatabase.fakeDatabaseCreator();
    private ProfileActivity profile;

    @Rule
    public final IntentsTestRule<MainMenuActivity> mActivityRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    /**
     * Set up a fake database and user.
     */
    @Before
    public void setUp() {
        Config.setUser(user);
        Database.setDebugDatabase(database);
        mActivityRule.launchActivity(new Intent());
        profile = FragmentConverter.createShowProfileWithProfile(otherUser);
        mActivityRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment, profile).commit();
    }

    @Test
    public void editButtonGone() {
        onView(withId(R.id.edit_profile)).check(doesNotExist());
    }

    @Test
    public void canFollow() throws InterruptedException {
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
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        assertFalse(profile.isFollowing());

    }

    @Test
    public void showEmptyStarWhenNotFollowing() {
        assertFalse(profile.isFollowing());
    }

}
