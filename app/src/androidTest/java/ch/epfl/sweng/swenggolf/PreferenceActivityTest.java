package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.preference.ListPreferenceAdapter;
import ch.epfl.sweng.swenggolf.preference.ListPreferencesActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class PreferenceActivityTest {
    @Rule
    public ActivityTestRule preferenceRule =
            new ActivityTestRule<>(ListPreferencesActivity.class,false,false);

    /**
     * Enters adapter debug mode.
     */
    @Before
    public void setUp() {
        ListPreferenceAdapter.debug = true;
        preferenceRule.launchActivity(new Intent());
    }

    /**
     * Scrolls to last element and check that it is displayed.
     */
    @Test
    public void scrollingWorks(){
        int userLength = ListPreferenceAdapter.USERS_INITIAL.length-1;
        ViewAction scrollToLast =
                RecyclerViewActions
                        .<ListPreferenceAdapter.PreferenceViewHolder>scrollToPosition(userLength);
        onView(ViewMatchers.withId(R.id.preference_list)).perform(scrollToLast);
        String lastUserName = ListPreferenceAdapter
                .USERS_INITIAL[userLength]
                .getUserName();
        onView(withText(lastUserName)).check(matches(isDisplayed()));
    }

    /**
     * Checks that element with username of the first user is in the list.
     */
    @Test
    public void firstElementIsDiplayed(){
        String firstUserName = ListPreferenceAdapter.USERS_INITIAL[0].getUserName();
        RecyclerViewMatcher rm = new RecyclerViewMatcher(R.id.preference_list);
        onView(rm.atPosition(0)).check(matches(hasDescendant(withText(firstUserName))));
    }
}