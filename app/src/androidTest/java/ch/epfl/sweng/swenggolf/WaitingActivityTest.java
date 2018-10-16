package ch.epfl.sweng.swenggolf;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import ch.epfl.sweng.swenggolf.database.CreateUserActivity;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseConnection;
import ch.epfl.sweng.swenggolf.database.DatabaseError;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.database.WaitingActivity;
import ch.epfl.sweng.swenggolf.main.MainActivity;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.ShowOfferActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class WaitingActivityTest {

    private static final String name = "Hello";
    private static final String mail = "Hello@World.ok";
    private static final String uid1 = "1234";
    private static final String uid2 = "5678";

    private static final String photo = "photo";


    private static final User userdb = new User(name, uid1 , mail, photo);
    private static final User usernotdb = new User(name, uid2,mail, photo);

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);


    @BeforeClass
    public static void setUp(){
        Config.goToTest();
        Config.setUser(new User(usernotdb));
        Database database = new FakeDatabase(true);
        database.write("/users", uid1, userdb);
        Database.setDebugDatabase(database);
    }

    @Test
    public void canGoToCreate() {
        onView(withId(R.id.go_to_login_button)).perform(click());
        DatabaseUser.getUser(new ValueListener() {
            @Override
            public void onDataChange(Object value) {
                assertNull(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        }, Config.getUser());
    // intended(hasComponent(CreateUserActivity.class.getName()));
    }

    @Test
    public void canGoToMenu() {
        Config.setUser(userdb);
        onView(withId(R.id.go_to_login_button)).perform(click());
        DatabaseUser.getUser(new ValueListener() {
            @Override
            public void onDataChange(Object value) {
                assertTrue(Config.getUser().equals(((User)value)));
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        }, Config.getUser());
    //intended(hasComponent(MainMenuActivity.class.getName()));
    }
}

