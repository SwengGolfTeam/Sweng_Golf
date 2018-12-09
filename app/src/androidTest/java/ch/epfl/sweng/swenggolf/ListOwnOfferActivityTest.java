package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.database.LocalDatabase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.list_offer.list_own_offer.ListOwnOfferActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.profile.User;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.swenggolf.ListOfferActivityTest.withRecyclerView;

public class ListOwnOfferActivityTest {

    private final Database database = new FakeDatabase(true);
    private final User user = FilledFakeDatabase.getUser(0);
    private final Offer offer = FilledFakeDatabase.getOffer(0);
    private final Offer oppositeOffer = (new Offer.Builder(offer))
            .setIsClosed(!offer.getIsClosed()).setTitle("opposite" + offer.getTitle())
            .setUuid("opposite" + offer.getUuid()).build();
    @Rule
    public IntentsTestRule<MainMenuActivity> mActivityRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    /**
     * Set up a fake database, a fake user and launch the fragment.
     */
    @Before
    public void setup() {
        Database.setDebugDatabase(database);
        database.write(Database.USERS_PATH, user.getUserId(), user);
        database.write(Database.OFFERS_PATH, oppositeOffer.getUuid(), oppositeOffer);
        database.write(Database.OFFERS_PATH, offer.getUuid(), offer);
        Config.setUser(user);
        mActivityRule.launchActivity(new Intent());
        mActivityRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment, new ListOwnOfferActivity()).commit();
        LocalDatabase localDb = new LocalDatabase(mActivityRule.getActivity(), null, 1);
        localDb.writeCategories(Arrays.asList(Category.values()));
    }

    @Test
    public void viewHasOwnOffers() {
        onView(withRecyclerView(R.id.offers_recycler_view).atPosition(0))
                .check(matches(hasDescendant(withText(offer.getTitle()))));
    }

    @Test
    public void testOpenPanelDisplaysOnlyOpen() throws InterruptedException {
        onView(withText(offer.getTitle())).check(matches(isDisplayed()));
    }

    @Test
    public void testClosedPanelDisplaysOnlyClosed() throws InterruptedException {
        onView(withText("CLOSED")).perform(click());
        onView(withText(oppositeOffer.getTitle())).check(matches(isDisplayed()));
    }

    @Test
    public void testCategoriesAmongPanels() throws InterruptedException {
        String defaultCategory = Category.getDefault().toString();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(defaultCategory)).perform(click());
        onView(withText("CLOSED")).perform(click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(defaultCategory)).perform(click());
        onView(withText(oppositeOffer.getTitle())).check(matches(isDisplayed()));
    }

}
