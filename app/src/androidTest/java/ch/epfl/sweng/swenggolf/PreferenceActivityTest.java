package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.firebase.database.DatabaseError;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;
import ch.epfl.sweng.swenggolf.preference.ListPreferenceAdapter;
import ch.epfl.sweng.swenggolf.preference.ListPreferencesActivity;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class PreferenceActivityTest {

    @Rule
    public IntentsTestRule preferenceRule =
            new IntentsTestRule(ListPreferencesActivity.class, false, false);

    /**
     * Enters adapter debug mode.
     */
    @Before
    public void setUp() {
        Database fake = FakeDatabase.fakeDatabaseCreator();
        Database.setDebugDatabase(fake);
        preferenceRule.launchActivity(new Intent());
    }

    /**
     * Scrolls to last element and check that it is displayed.
     */
    @Test
    public void scrollingWorks(){
        int userLength = FilledFakeDatabase.FAKE_USERS.length-1;
        ViewAction scrollToLast =
                RecyclerViewActions
                        .<ListPreferenceAdapter.PreferenceViewHolder>scrollToPosition(userLength);
        onView(ViewMatchers.withId(R.id.preference_list)).perform(scrollToLast);
    }

    @Test
    public void testListSize(){
        ListPreferenceAdapter adapter = new ListPreferenceAdapter();
        assertThat(adapter.getItemCount(),is(FilledFakeDatabase.FAKE_USERS.length));
    }

    @Test
    public void showProfileWhenClickOnView() {
        User user = FilledFakeDatabase.FAKE_USERS[0];
        onView(withId(R.id.preference_list)).perform(actionOnItem(hasDescendant(
                ViewMatchers.withText(user.getUserName())), click()));
        intended(allOf(hasComponent(ProfileActivity.class.getName()),
        hasExtra("ch.epfl.sweng.swenggolf.user", user)));
    }
}
