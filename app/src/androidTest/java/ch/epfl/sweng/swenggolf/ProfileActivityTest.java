package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ProfileActivityTest {

    final User user = new User("Patrick", "Vetterli", "1234567890", "", "tea");
    User newUser;

    @Rule
    public final IntentsTestRule<ProfileActivity> mActivityRule =
            new IntentsTestRule<>(ProfileActivity.class, false, false);

    /**
     * Initialise the Config and the Database for tests.
     */
    @Before
    public void setUp(){
        Config.isTest();
        Config.setUser(new User(user));
        newUser = new User(user);
        Database database = new FakeDatabase(true);
        Database.setDebugDatabase(database);
        mActivityRule.launchActivity(new Intent().putExtra("ch.epfl.sweng.swenggolf.user", user));
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
    public void canGoToMenu() {
        onView(withContentDescription("abc_action_bar_up_description")).perform(click());
        intended(hasComponent(MainMenuActivity.class.getName()));
    }

    private void canEditField(int editTextId, final User newUser, String newText) {
        onView(withId(R.id.edit)).perform(click());
        onView(withId(editTextId)).perform(replaceText(newText)).perform(closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(click());
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
