package ch.epfl.sweng.swenggolf;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.TestMode;
import ch.epfl.sweng.swenggolf.database.user.UserLocal;
import ch.epfl.sweng.swenggolf.main.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class LogOutActivityTest {

    private static final String name = "Hello";
    private static final String mail = "Hello@World.ok";
    private static final String uid = "123456789009876543211234567890";
    private static final String preference = "Bananas";

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void setUp() {
        TestMode.goToTest();
        TestMode.setUser(new UserLocal(name, uid, mail,preference));
    }

    @Test
    public void canDisplay() {
        onView(ViewMatchers.withId(R.id.go_to_login_button)).perform(click());
        onView(withId(R.id.mail)).check(matches(withText(mail)));
        onView(withId(R.id.uid)).check(matches(withText(uid)));
        onView(withId(R.id.name)).check(matches(withText(name)));
    }

}
