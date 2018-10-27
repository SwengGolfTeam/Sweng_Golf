package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.ShowOfferActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.swenggolf.TestUtility.testToastShow;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;


public class ShowOfferActivityDbNotWorkingTest {
    @Rule
    public final IntentsTestRule<ShowOfferActivity> mActivityRule =
            new IntentsTestRule<>(ShowOfferActivity.class, false, false);

    private FakeDatabase database = (FakeDatabase) FakeDatabase.fakeDatabaseCreator();

    /**
     * Set up a fake database, a fake user and launch activity.
     */
    @Before
    public void setUp() {
        Database.setDebugDatabase(database);
        User user = FilledFakeDatabase.FAKE_USERS[0];
        Config.setUser(user);
        Intent intent = new Intent();
        Offer offer = FilledFakeDatabase.FAKE_OFFERS[0];
        intent.putExtra("offer", offer);
        mActivityRule.launchActivity(intent);
    }

    @Test
    public void openProfileFromOfferShowToastOnFail() {
        onView(withId(R.id.show_offer_title)).perform(click());
        database.setWorking(false);
        onView(withId(R.id.show_offer_author)).perform(click());
        testToastShow(mActivityRule, R.string.error_load_user);
    }


}
