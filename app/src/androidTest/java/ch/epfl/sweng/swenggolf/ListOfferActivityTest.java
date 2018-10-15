package ch.epfl.sweng.swenggolf;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseConnection;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.main.MainActivity;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;

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

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    private static final String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
            + "Nam ut quam ornare, fringilla nunc eget, facilisis lectus."
            + "Curabitur ut nunc nec est feugiat commodo. Nulla vel porttitor justo."
            + "Suspendisse potenti. Morbi vehicula ante nibh,"
            + " at tristique tortor dignissim non."
            + "In sit amet ligula tempus, mattis massa dictum, mollis sem."
            + "Mauris convallis sed mauris ut sodales."
            + "Nullam tristique vel nisi a rutrum. Sed commodo nec libero sed volutpat."
            + "Fusce in nibh pharetra nunc pellentesque tempor id interdum est."
            + "Sed rutrum mauris in ipsum consequat, nec scelerisque nulla facilisis.";

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
        Database database = new FakeDatabase(true);
        Offer offer1 = new Offer("Author", "This is a title", lorem);
        Offer offer2 = new Offer("Author", "This is a title 2", lorem);
        database.write("/offers", "idoftheoffer1", offer1);
        database.write("/offers", "idoftheoffer2", offer2);
        Database.setDebugDatabase(database);
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

       // onView(withId(R.id.show_offer_title))
       //         .check(matches(withText(ListOfferActivity.offerList.get(0).getTitle())));
    }

    @Test
    public void offerCorrectlyExpandedAndRetractedAfterLongPressOnList() {
        openListActivity();

        Offer offerToTest = ListOfferActivity.offerList.get(0);
        Offer otherOffer = ListOfferActivity.offerList.get(1);

        String longDescription = offerToTest.getDescription();
        String shortDescription = offerToTest.getShortDescription();

        // Check if short description is displayed, then expand.
        onView(withRecyclerView(R.id.offers_recycler_view).atPosition(0))
                .check(matches(hasDescendant(withText(shortDescription))));
        
        onView(withId(R.id.offers_recycler_view)).perform(actionOnItem(
                hasDescendant(withText(offerToTest.getTitle())), longClick()));

        // Check that the long description is displayed, and the sort to another offer, then expand
        // other offer.
        onView(withRecyclerView(R.id.offers_recycler_view).atPosition(0))
                .check(matches(hasDescendant(withText(longDescription))));
        onView(withRecyclerView(R.id.offers_recycler_view).atPosition(1))
                .check(matches(hasDescendant(withText(otherOffer.getShortDescription()))));

        onView(withId(R.id.offers_recycler_view)).perform(actionOnItem(
                hasDescendant(withText(otherOffer.getTitle())), longClick()));

        // Check that the first offer is retracted and that the second is expanded.
        onView(withRecyclerView(R.id.offers_recycler_view).atPosition(0))
                .check(matches(hasDescendant(withText(shortDescription))));
        onView(withRecyclerView(R.id.offers_recycler_view).atPosition(1))
                .check(matches(hasDescendant(withText(otherOffer.getDescription()))));

        // Close second offer and check if closed.
        onView(withId(R.id.offers_recycler_view)).perform(actionOnItem(
                hasDescendant(withText(otherOffer.getTitle())), longClick()));

        onView(withRecyclerView(R.id.offers_recycler_view).atPosition(1))
                .check(matches(hasDescendant(withText(otherOffer.getShortDescription()))));
    }

    @Test
    public void listShowCorrectly() {
        onView(withId(R.id.show_offers_button)).perform(click());
    }
}
