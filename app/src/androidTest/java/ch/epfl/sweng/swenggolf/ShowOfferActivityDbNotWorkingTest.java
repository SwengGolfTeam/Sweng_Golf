package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.annotation.UiThreadTest;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.ShowOfferActivity;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.swenggolf.TestUtility.testToastShow;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class ShowOfferActivityDbNotWorkingTest {
    @Rule
    public final IntentsTestRule<ShowOfferActivity> mActivityRule =
            new IntentsTestRule<>(ShowOfferActivity.class, false, false);

    private static final FakeDatabase database = new FakeDatabase(true);
    private static final User user = new User("patrick", "0", "email", "photo", "preference");
    private static final Offer offer = new Offer("0", "title", "description");

    /**
     * Set up a fake database, a fake user and launch activity.
     */
    @Before
    public void setUp() {
        database.write(Database.USERS_PATH, "0", user);
        database.write(Database.OFFERS_PATH, "0", offer);
        Database.setDebugDatabase(database);
        Config.setUser(user);
        Intent intent = new Intent();
        intent.putExtra("offer", offer);
        mActivityRule.launchActivity(intent);
    }

    @Test
    public void openProfileFromOfferShowToastOnFail() {
        onView(withId(R.id.show_offer_title)).perform(click());
        database.setEntryNotWorking(Database.USERS_PATH, offer.getUserId());
        onView(withId(R.id.show_offer_author)).perform(click());
        testToastShow(mActivityRule, R.string.error_load_user);
    }


}
