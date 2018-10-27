package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.widget.ImageButton;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


public class ProfileActivityOtherUserTest {

    final User user = new User("Patrick", "Vetterli", "1234567890", "", "tea");
    final User otherUser = new User("other", "user", "is", "a", "placeholder");

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
        Database.setDebugDatabase(new FakeDatabase(true));
        mActivityRule.launchActivity(
                new Intent().putExtra("ch.epfl.sweng.swenggolf.user", otherUser));
    }

    @Test
    public void editButtonGone() {
        onView(withId(R.id.edit)).check(matches(withEffectiveVisibility(
                ViewMatchers.Visibility.GONE)));
    }

}
