package ch.epfl.sweng.swenggolf;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.main.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CreateUserActivityTest {

    private static final String name = "Hello";
    private static final String namechanged = "Hello World";
    private static final String mail = "Hello@World.ok";
    private static final String mailchanged = "Hello@World.okapi";
    private static final String uid = "123456789009876543211234567890";

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void setUp(){
        TestMode.goToTest();
        TestMode.setUser(new UserLocal(name, uid, mail));
    }

    @Test
    public void canDisplay() {
        onView(withId(R.id.go_to_login_button)).perform(click());
        onView(withId(R.id.mail)).check(matches(withText(mail)));
        onView(withId(R.id.mail)).perform(typeText(" World"));
        onView(withId(R.id.name)).check(matches(withText(name)));
        onView(withId(R.id.name)).perform(typeText("api"));
        onView(withId(R.id.create_account)).perform(click());
        TestMode.getDatabase().containsUser(new ExistsOnData() {
            @Override
            public void onSuccess(Boolean exists) {
                assertTrue(true);
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {
                assertTrue(false);
            }
        }, TestMode.getUser());

    }

}
