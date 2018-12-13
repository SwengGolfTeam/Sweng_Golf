package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.CreateUserActivity;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.database.WaitingActivity;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.statistics.UserStats;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class WaitingActivityTest {

    private static final String NAME = "Hello";
    private static final String MAIL = "Hello@World.ok";
    private static final String UID_1 = "1234";
    private static final String UID_2 = "5678";
    private static final String PHOTO = "PHOTO";


    private static final User USERDB = new User(NAME, UID_1, MAIL, PHOTO);
    private static final User USERNOTDB = new User(NAME, UID_2, MAIL, PHOTO);

    @Rule
    public final IntentsTestRule<WaitingActivity> mActivityRule =
            new IntentsTestRule<>(WaitingActivity.class, false, false);


    /**
     * Initialise the Config and the Database for tests.
     */
    @Before
    public void setUp() {
        Config.goToTest();
        Database database = new FakeDatabase(true);
        Database.setDebugDatabase(database);
        DatabaseUser.addUser(USERDB);
        Config.setUser(USERDB);
        UserStats initStats = new UserStats();
        initStats.write(USERDB.getUserId());
        initStats.write(USERNOTDB.getUserId());
    }

    @Test
    public void canGoToCreate() {
        Config.setUser(USERNOTDB);
        mActivityRule.launchActivity(new Intent());

        //elements from CreateUserActivity
        onView(withId(R.id.name_field)).check(matches(isDisplayed()));
        onView(withId(R.id.presentation)).check(matches(isDisplayed()));
    }

    @Test
    public void canGoToMenu() {
        Config.setUser(USERDB);
        Config.setActivityCallback(new ActivityCallback() {
            @Override
            public void isDone() {
                intended(hasComponent(MainMenuActivity.class.getName()));
            }
        });
    }

    @Test
    public void databaseNotWorking() {
        Database.setDebugDatabase(new FakeDatabase(false));
        Config.setUser(USERDB);
        mActivityRule.launchActivity(new Intent());
        TestUtility.testToastShow(mActivityRule, "Error on Connection: "
                + DbError.UNKNOWN_ERROR.toString());
        mActivityRule.finishActivity();
    }
}

