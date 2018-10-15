package ch.epfl.sweng.swenggolf;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.WaitingActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class WaitingActivityTest {

    private static final String name = "Hello";
    private static final String namechanged = "Hello World";
    private static final String mail = "Hello@World.ok";
    private static final String mailchanged = "Hello@World.okapi";
    private static final String uid = "123456789009876543211234567890";

    @Rule
    public final ActivityTestRule<WaitingActivity> mActivityRule =
            new ActivityTestRule<>(WaitingActivity.class);

    @BeforeClass
    public static void setUp(){
        Config.goToTest();
        Config.setUser(new UserLocal(name, uid, mail,"Hello"));
    }

    @Test
    public void canDisplay() {
        onView(withId(R.id.waiting_msg)).check(matches(withText("Please wait.")));
    }

}

