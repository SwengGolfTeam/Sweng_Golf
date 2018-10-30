package ch.epfl.sweng.swenggolf;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.TestTimedOutException;

import java.net.ConnectException;
import java.util.UUID;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.main.MainActivity;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.fail;


@RunWith(AndroidJUnit4.class)
public class ListOfferActivityTest {

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    public static final String LOREM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
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
    public final IntentsTestRule<MainActivity> mActivityRule =
            new IntentsTestRule<>(MainActivity.class);

    /**
     * Configures a fake database and enables TestMode.
     */
    @Before
    public void init() {
        setUpFakeDatabase();
        Config.goToTest();
    }

    /**
     * Set up a fake database with two offers.
     */
    protected static void setUpFakeDatabase() {
        Database database = new FakeDatabase(true);
        Offer offer1 = new Offer("user_id", "This is a title", LOREM);
        Offer offer2 = new Offer("user_id", "This is a title 2", LOREM);
        database.write(Database.OFFERS_PATH, "idoftheoffer1", offer1);
        database.write(Database.OFFERS_PATH, "idoftheoffer2", offer2);
        Database.setDebugDatabase(database);
        Config.setUser(new User("aaa", "user_id", "ccc", "ddd"));
        database.write(Database.USERS_PATH, Config.getUser().getUserId(), Config.getUser());
    }

    /**
     * Opens the list activity.
     */
    public void openListActivity() {
        onView(withId(R.id.show_offers_button)).perform(click());
    }

    @Test
    public void offerCorrectlyDisplayedInTheList() {
        openListActivity();

        Offer offer = ListOfferActivity.offerList.get(0);

        onView(withRecyclerView(R.id.offers_recycler_view).atPosition(0))
                .check(matches(hasDescendant(withText(offer.getTitle()))));
        onView(withRecyclerView(R.id.offers_recycler_view).atPosition(0))
                .check(matches(hasDescendant(withText(offer.getShortDescription()))));

        DatabaseUser.getUser(new ValueListener<User>() {
            @Override
            public void onDataChange(User value) {
                onView(withRecyclerView(R.id.offers_recycler_view).atPosition(0))
                        .check(matches(hasDescendant(withText(value.getUserName()))));
            }
            
            @Override
            public void onCancelled(DbError error) {
                fail();
            }
        }, offer.getUserId());
   }

    @Test
    public void canGoToMenu() {
        openListActivity();
        onView(withContentDescription("abc_action_bar_up_description")).perform(click());
        intended(hasComponent(MainMenuActivity.class.getName()));
    }

    @Test
    public void offerCorrectlyDisplayedAfterClickOnList() {
        openListActivity();

        onView(withId(R.id.offers_recycler_view)).perform(actionOnItem(hasDescendant(
                ViewMatchers.withText(ListOfferActivity.offerList.get(0).getTitle())), click()));
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

    @Test
    public void allCategoriesUncheckedMessageShown() {
        openListActivity();
        for (Category cat : Category.values()) {
            clickOnCategoryInMenu(cat);
        }
        onView(withId(R.id.no_offers_to_show)).check(matches(isDisplayed()));
    }

    @Test
    public void uncheckAndCheckSameCategory() {
        openListActivity();

        clickOnCategoryInMenu(Category.getDefault());
        clickOnCategoryInMenu(Category.getDefault());

        Offer offer = ListOfferActivity.offerList.get(0);

        onView(withRecyclerView(R.id.offers_recycler_view).atPosition(0))
                .check(matches(hasDescendant(withText(offer.getTitle()))));
    }

    private void clickOnCategoryInMenu(Category cat){
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(cat.toString())).perform(click());
    }
}
