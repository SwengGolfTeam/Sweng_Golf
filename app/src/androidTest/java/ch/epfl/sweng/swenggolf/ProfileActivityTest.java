package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ProfileActivityTest {

    private static final User user = new User("Patrick", "Vetterli", "1234567890", "", "tea");
    private User newUser;

    @Rule
    public final IntentsTestRule<MainMenuActivity> mActivityRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    /**
     * Initialise the Config and the Database for tests.
     */
    @Before
    public void setUp(){
        Config.setUser(new User(user));
        newUser = new User(user);
        Database database = new FakeDatabase(true);
        Database.setDebugDatabase(database);
        mActivityRule.launchActivity(new Intent());
        mActivityRule.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.centralFragment, TestUtility.userFragment(new ProfileActivity(), user)).commit();
    }

    @Test
    public void canEditUserName() {
        String newName = "Jean-Jacques";
        newUser.setUserName(newName);
        canEditField(R.id.edit_name, newUser, newName);
    }

    @Test
    public void canEditPreferences() {
        String newPref = "coffee";
        newUser.setPreference(newPref);
        canEditField(R.id.edit_pref, newUser, newPref);

    }

    @Test
    public void nameDisplayed() {
        onView(withId(R.id.name)).check(matches(withText(user.getUserName())));
    }

    @Test
    public void canGoToMenu() throws InterruptedException {
        onView(withContentDescription("abc_action_bar_home_description")).perform(click());
        onView(withId(R.id.side_menu)).check(matches(isOpen()));
    }

    private void canEditField(int editTextId, final User newUser, String newText) {
        onView(withId(R.id.edit_profile)).perform(click());
        Espresso.closeSoftKeyboard();
        onView(withId(editTextId)).perform(replaceText(newText)).perform(closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(scrollTo(), click());
        ValueListener vl = new ValueListener() {
            @Override
            public void onDataChange(Object value) {
                assertEquals(newUser, value);
            }

            @Override
            public void onCancelled(DbError error) {
                //nothing to do
            }
        };
        DatabaseUser.getUser(vl, user);

    }
}
