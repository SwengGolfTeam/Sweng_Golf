package ch.epfl.sweng.swenggolf;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
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
import ch.epfl.sweng.swenggolf.main.MainActivity;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ProfileActivityTest {

    User user = new User("Patrick", "Vetterli", "1234567890", "");

    @Rule
    public final IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule<>(MainActivity.class);

    /**
     * Initialise the Config and the Database for tests.
     */
    @Before
    public void setUp(){
        Config.isTest();
        Config.setUser(new User(user));
        Database database = new FakeDatabase(true);
        Database.setDebugDatabase(database);
    }

    @Test
    public void canEditUserName() {
        String newName = "Anonymous";
        onView(withId(R.id.profileButton)).perform(click());
        onView(withId(R.id.edit)).perform(click());
        onView(withId(R.id.edit_name)).perform(typeText(newName)).perform(closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(click());
        user.setUserName(user.getUserName()+newName);
        ValueListener vl = new ValueListener() {
            @Override
            public void onDataChange(Object value) {
                assertEquals(user, value);
            }

            @Override
            public void onCancelled(DbError error) {
                //nothing to do
            }
        };
        DatabaseUser.getUser(vl, user);
    }

    @Test
    public void nameDisplayed() {
        onView(withId(R.id.profileButton)).perform(click());
        onView(withId(R.id.name)).check(matches(withText(user.getUserName())));
    }

    @Test
    public void goToMenu() {
        onView(withId(R.id.profileButton)).perform(click());
        onView(withContentDescription("abc_action_bar_up_description")).perform(click());
        intended(hasComponent(MainMenuActivity.class.getName()));
    }
}
