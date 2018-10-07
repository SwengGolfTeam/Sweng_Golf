package ch.epfl.sweng.swenggolf;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CreateOfferActivityTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void errorMessageDisplayed() {
        onView(withId(R.id.create_offer_button)).perform(click());
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.error_message)).check(matches(isDisplayed()));
    }

    @Test
    public void createOfferDoesNothing() {
        // TODO to modify once the database is here
        onView(withId(R.id.create_offer_button)).perform(click());
        onView(withId(R.id.offer_name)).perform(typeText("title test"))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.offer_description)).perform(typeText("description test"))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.create_offer_button)).check(matches(isDisplayed()));
        // TODO check here if a message appears to confirm the creation
    }
}
