package ch.epfl.sweng.swenggolf;


import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.preference.ListPreferenceAdapter;
import ch.epfl.sweng.swenggolf.preference.ListPreferencesActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PreferenceAtivityTest {
    @Rule
    public ActivityTestRule preferenceRule =
            new ActivityTestRule<ListPreferencesActivity>(ListPreferencesActivity.class,false,false);

    /**
     * Enters adapter debug mode
     */
    @Before
    public void setUp() throws InterruptedException{
        ListPreferenceAdapter.debug = true;
        preferenceRule.launchActivity(new Intent());
    }

    /**
     * Scrolls to last element and check that it is displayed
     */
    @Test
    public void scrollingWorks(){
        int userLength = ListPreferenceAdapter.usersInitial.length-1;
        ViewAction scrollToLast = RecyclerViewActions.<ListPreferenceAdapter.PreferenceViewHolder>scrollToPosition(userLength);
        onView(withId(R.id.preference_list)).perform(scrollToLast);
        onView(withText(ListPreferenceAdapter.usersInitial[userLength].getUserName())).check(matches(isDisplayed()));
    }

    /**
     * Checks that element with username of the first user is in the list
     */
    @Test
    public void firstElementIsDiplayed(){
        String firstUserName = ListPreferenceAdapter.usersInitial[0].getUserName();
        onView(withId(R.id.preference_list)).check(matches(hasDescendant(withText(firstUserName))));
    }
}
