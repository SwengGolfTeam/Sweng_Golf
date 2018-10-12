package ch.epfl.sweng.swenggolf;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.DatabaseConnection;
import ch.epfl.sweng.swenggolf.main.MainActivity;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ListOfferActivityTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){
        DatabaseConnection.setDebugDatabase(FakeFirebaseDatabase.firebaseDatabaseOffers());
    }

    /**
     * Opens the list activity.
     */
    public void openListActivity() {
        onView(withId(R.id.show_offers_button)).perform(click());
    }

    @Test
    public void offerCorrectlyDisplayedAfterClickOnList() {
        openListActivity();

        onView(withId(R.id.offers_recycler_view)).perform(actionOnItem(
                hasDescendant(withText(ListOfferActivity.offerList.get(0).getTitle())), click()));

        onView(withId(R.id.show_offer_title))
                .check(matches(withText(ListOfferActivity.offerList.get(0).getTitle())));
    }

    @Test
    public void offerCorrectlyExpandedAfterLongPressOnList() {
        openListActivity();

        onView(withId(R.id.offers_recycler_view)).perform(actionOnItem(
                hasDescendant(withText(ListOfferActivity.offerList.get(0).getTitle())),
                longClick()));


    }

    @Test
    public void listShowCorrectly(){
        onView(withId(R.id.show_offers_button)).perform(click());
    }
}
