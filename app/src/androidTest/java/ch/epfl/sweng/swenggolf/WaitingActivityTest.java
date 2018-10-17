package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
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
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.database.WaitingActivity;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


@RunWith(AndroidJUnit4.class)
public class WaitingActivityTest {

    private static final String NAME = "Hello";
    private static final String MAIL = "Hello@World.ok";
    private static final String UID_1 = "1234";
    private static final String UID_2 = "5678";

    private static final String photo = "photo";


    private static final User USERDB = new User(NAME, UID_1, MAIL, photo);
    private static final User USERNOTDB = new User(NAME, UID_2, MAIL, photo);

    @Rule
    public final ActivityTestRule<WaitingActivity> mActivityRule =
            new ActivityTestRule<>(WaitingActivity.class);


    /**
     * Initialise the Config and the Database for tests.
     */
    @Before
    public void setUp() {
        Config.goToTest();
        Database database = new FakeDatabase(true);
        database.write("/users", UID_1, USERDB);
        Database.setDebugDatabase(database);
    }

    @Test
    public void canGoToCreate() {
        Intents.init();
        mActivityRule.launchActivity(new Intent());
        Config.setUser(new User(USERNOTDB));
        DatabaseUser.getUser(new ValueListener() {
            @Override
            public void onDataChange(Object value) {
                assertNull(value);
            }

            @Override
            public void onCancelled(DbError error) {

            }
        }, Config.getUser());
        intended(hasComponent(CreateUserActivity.class.getName()), times(0));
        Intents.release();
    }

    @Test
    public void canGoToMenu() {
        Intents.init();
        mActivityRule.launchActivity(new Intent());
        Config.setUser(USERDB);
        assertEquals(USERDB, Config.getUser());
        DatabaseUser.addUser(USERDB);
        DatabaseUser.getUser(new ValueListener() {
            @Override
            public void onDataChange(Object value) {
                assertEquals(USERDB, value);
            }

            @Override
            public void onCancelled(DbError error) {

            }
        }, USERDB);
        intended(hasComponent(MainMenuActivity.class.getName()), times(0));
        Intents.release();
    }
}

