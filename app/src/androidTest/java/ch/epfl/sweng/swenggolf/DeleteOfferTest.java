package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;

import ch.epfl.sweng.swenggolf.offer.Offer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
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
    public final ActivityTestRule<MainMenuActivity> mActivityRule =
            new ActivityTestRule<>(MainMenuActivity.class, false, false);

    /**
     * Configures a fake database and enables TestMode.
     */
    @Before
    public void init() {
        setUpFakeDatabase();
        Config.goToTest();
        mActivityRule.launchActivity(new Intent());
    }

    private static Database database = new FakeDatabase(true);

    /**
     * Set up a fake database with two offers.
     */
    protected static void setUpFakeDatabase() {
        Offer offer1 = new Offer("user_id", TITLE1, "Hello");
        Offer offer2 = new Offer("user_id", "This is a title 2", "LOREM");
        database.write(Database.OFFERS_PATH, ID1, offer1);
        database.write(Database.OFFERS_PATH, ID2, offer2);
        Database.setDebugDatabase(database);
        Config.setUser(new User("aaa", "user_id", "ccc", "ddd"));
        DatabaseUser.addUser(Config.getUser());
    }

    @Test
    public void cancelDialog() {
        onView(withId(R.id.offers_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.button_delete_offer)).perform(click());
        onView(withText(android.R.string.no)).perform(click());
        onView(withId(R.id.button_delete_offer)).perform(click());
        onView(withText("Delete entry")).check(matches(isDisplayed()));
    }

    @Test
    public void deleteElementTest() throws InterruptedException {
        onView(withId(R.id.offers_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.show_offer_title)).check(matches(withText(TITLE1)));
        try {
            onView(withId(R.id.button_delete_offer)).perform(click());
        } catch (NoMatchingViewException e) {
            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
            onView(withContentDescription(R.string.delete_offer)).perform(click());
        }
        onView(withChild(withText(android.R.string.yes))).perform(click());
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
        database.read(Database.OFFERS_PATH, ID1, vl, Offer.class);
    }

}