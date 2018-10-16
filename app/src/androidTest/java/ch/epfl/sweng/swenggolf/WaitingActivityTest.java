package ch.epfl.sweng.swenggolf;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseConnection;
import ch.epfl.sweng.swenggolf.database.DatabaseError;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.database.WaitingActivity;
import ch.epfl.sweng.swenggolf.main.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class WaitingActivityTest {

    private static final String name = "Hello";
    private static final String mail = "Hello@World.ok";
    private static final String uid = "1234";

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp(){
        Config.goToTest();
        Config.setUser(new User(name, uid, mail,"Hello"));
        DatabaseConnection.setDebugDatabase(FakeFirebaseDatabase.firebaseDatabaseOffers());
    }

    @Test
    public void canDisplay() {
        onView(withId(R.id.go_to_login_button)).perform(click());
        onView(withId(R.id.waiting_msg)).check(matches(withText("Please wait.")));
        DatabaseUser.getUser(new ValueListener() {
            @Override
            public void onDataChange(Object value) {
                assertNull(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        }, Config.getUser());
        assertEquals("mail test", Config.getUser().getEmail(), mail);
        assertEquals("name test", Config.getUser().getUserName(), name);
    }

}

