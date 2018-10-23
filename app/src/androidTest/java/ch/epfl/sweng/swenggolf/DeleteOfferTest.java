package ch.epfl.sweng.swenggolf;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.TestCase;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Not;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.StorageConnection;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.main.MainActivity;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.ShowOfferActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.Intents.release;
import static android.support.test.espresso.intent.VerificationModes.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.swenggolf.ListOfferActivityTest.withRecyclerView;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
public class DeleteOfferTest {

    private static final String ID1 = "idoftheoffer1";
    private static final String ID2 = "idoftheoffer2";
    private static final String TITLE1 = "This is a title";

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    /**
     * Configures a fake database and enables TestMode.
     */
    @Before
    public void init() {
        setUpFakeDatabase();
        Config.goToTest();
    }

    private static Database database = new FakeDatabase(true);

    /**
     * Set up a fake database with two offers.
     */
    protected static void setUpFakeDatabase() {
        Offer offer1 = new Offer("user_id", TITLE1, "Hello");
        Offer offer2 = new Offer("user_id", "This is a title 2", "LOREM");
        database.write("/offers", ID1, offer1);
        database.write("/offers", ID2, offer2);
        Database.setDebugDatabase(database);
        Config.setUser(new User("aaa", "user_id", "ccc", "ddd"));
        database.write("/users", Config.getUser().getUserId(), Config.getUser());
    }

    /**
     * Opens the list activity.
     */
    public void openListActivity() {
        onView(withId(R.id.show_offers_button)).perform(click());
    }

    @Test
    public void cancelDialog() {
        openListActivity();
        onView(withId(R.id.offers_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.button_delete_offer)).perform(click());
        onView(withText(android.R.string.no)).perform(click());
        onView(withId(R.id.button_delete_offer)).perform(click());
        onView(withText("Delete entry")).check(matches(isDisplayed()));
    }

    @Test
    public void deleteElementTest() {
        openListActivity();
        onView(withId(R.id.offers_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.show_offer_title)).check(matches(withText(TITLE1)));
        onView(withId(R.id.button_delete_offer)).perform(click());
        onView(withText(android.R.string.yes)).perform(click());
        ValueListener vl = new ValueListener() {
            @Override
            public void onDataChange(Object value) {
                //assertNull(value);
            }

            @Override
            public void onCancelled(DbError error) {
                fail();
            }
        };
        database.read("/offers", ID1, vl, Offer.class);
    }

}