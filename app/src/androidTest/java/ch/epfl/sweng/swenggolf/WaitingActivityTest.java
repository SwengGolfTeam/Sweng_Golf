package ch.epfl.sweng.swenggolf;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.CreateUserActivity;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.WaitingActivity;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;


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
            new IntentsTestRule<>(WaitingActivity.class);


    /**
     * Initialise the Config and the Database for tests.
     */
    @Before
    public void setUp() {
        Config.goToTest();
        Database database = new FakeDatabase(true);
        Database.setDebugDatabase(database);
        DatabaseUser.addUser(USERDB);
    }

    @Test
    public void canGoToCreate() {
        Config.setUser(new User(USERNOTDB));
        Config.setActivityCallback(new ActivityCallback() {
            @Override
            public void isDone() {
                intended(hasComponent(CreateUserActivity.class.getName()));
            }
        });
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

}

