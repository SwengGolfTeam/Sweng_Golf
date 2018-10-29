package ch.epfl.sweng.swenggolf;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.storage.FakeStorage;
import ch.epfl.sweng.swenggolf.storage.Storage;
import ch.epfl.sweng.swenggolf.main.MainActivity;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;
import ch.epfl.sweng.swenggolf.offer.ShowOfferActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.VerificationModes.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
public class CreateOfferActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Before
    public void setTest() {
        Config.goToTest();
    }

    /**
     * Sets up a fake database and a fake storage, and enables TestMode.
     */
    private void initDatabse() {
        ListOfferActivityTest.setUpFakeDatabase();
        Storage.setDebugStorage(new FakeStorage(true));
    }


    @Test
    public void errorMessageDisplayed() {
        onView(withId(R.id.create_offer_button)).perform(click());
        onView(withId(R.id.offer_name)).perform(closeSoftKeyboard());
        onView(withId(R.id.button)).perform(scrollTo(),click());
        onView(withId(R.id.error_message)).perform(scrollTo())
                .check(matches(withText(R.string.error_create_offer_invalid)));
    }

    private void fillOffer() {
        onView(withId(R.id.offer_name)).perform(typeText("title test"))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.offer_description)).perform(typeText("description test"))
                .perform(closeSoftKeyboard());

        // Answer to gallery intent
        Intent resultData = new Intent();
        resultData.setData(Uri.parse("drawable://" + R.drawable.img));
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(not(isInternal())).respondWith(result);

        onView(withId(R.id.offer_picture)).perform(click());
        onView(withId(R.id.button)).perform(click());
    }

    @Test
    public void createOfferShowOfferWhenValidInput() {
        initDatabse();
        onView(withId(R.id.create_offer_button)).perform(click());
        fillOffer();
        intended(hasComponent(ShowOfferActivity.class.getName()));
    }

    @Test
    public void showMessageErrorWhenCantCreateOffer() {
        Database.setDebugDatabase(new FakeDatabase(false));
        onView(withId(R.id.create_offer_button)).perform(click());
        fillOffer();
        onView(withId(R.id.error_message))
                .check(matches(withText(R.string.error_create_offer_database)));
    }

    private void goToShowOffer(boolean setToOtherThanOwner) {
        initDatabse();
        if (setToOtherThanOwner) {
            User u = new User("username",
                    "id" + Config.getUser().getUserId(), "username@example.com", "nophoto");
            Config.setUser(u);
            DatabaseUser.addUser(u);
        }

        onView(withId(R.id.show_offers_button)).perform(click());
        onView(withId(R.id.offers_recycler_view)).perform(actionOnItem(
                hasDescendant(
                        ViewMatchers
                                .withText(
                                        ListOfferActivity.offerList.get(0).getTitle())), click()));
    }

    @Test
    public void modifyingOfferViaShowOfferWorks() {
        goToShowOffer(false);
        onView(withId(R.id.button_modify_offer)).perform(click());
        fillOffer();
        intended(hasComponent(ShowOfferActivity.class.getName()), times(2));
    }

    @Test
    public void modifyingOfferViaShowOfferWorksOnlyOnCreator() {
        goToShowOffer(true);
        onView(withId(R.id.button_modify_offer)).check(matches(not(isDisplayed())));
        onView(withId(R.id.button_modify_offer)).check(matches(not(isClickable())));
    }
}