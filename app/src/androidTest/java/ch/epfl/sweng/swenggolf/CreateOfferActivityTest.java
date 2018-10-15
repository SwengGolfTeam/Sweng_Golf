package ch.epfl.sweng.swenggolf;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.DatabaseConnection;
import ch.epfl.sweng.swenggolf.main.MainActivity;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;
import ch.epfl.sweng.swenggolf.offer.ShowOfferActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static android.support.test.espresso.intent.VerificationModes.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CreateOfferActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Before
    public void init() {
        DatabaseConnection.setDebugDatabase(FakeFirebaseDatabase.firebaseDatabaseOffers());
        TestMode.goToTest();
    }


    @Test
    public void errorMessageDisplayed() {
        onView(withId(R.id.create_offer_button)).perform(click());
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.error_message))
                .check(matches(withText(R.string.error_create_offer_invalid)));
    }

    private void fillOffer() {
        onView(withId(R.id.offer_name)).perform(typeText("title test"))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.offer_description)).perform(typeText("description test"))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.offer_picture)).perform(click());
        onView(withId(R.id.button)).perform(click());
    }

    @Test
    public void createOfferShowOfferWhenValidInput() {
        onView(withId(R.id.create_offer_button)).perform(click());
        fillOffer();
        intended(hasComponent(ShowOfferActivity.class.getName()));
    }

    @Test
    public void showMessageErrorWhenCantCreateOffer() {
        DatabaseConnection.setDebugDatabase(FakeFirebaseDatabase.firebaseDatabaseOffers(false));
        onView(withId(R.id.create_offer_button)).perform(click());
        fillOffer();
        onView(withId(R.id.error_message))
                .check(matches(withText(R.string.error_create_offer_database)));
    }

    @Test
    public void modifyingOfferViaShowOfferWorks() {
        onView(withId(R.id.show_offers_button)).perform(click());

        onView(withId(R.id.offers_recycler_view)).perform(actionOnItem(
                hasDescendant(withText(ListOfferActivity.offerList.get(0).getTitle())), click()));

        onView(withId(R.id.button_modify_offer)).perform(click());

        fillOffer();

        intended(hasComponent(ShowOfferActivity.class.getName()), times(2));
    }
}
