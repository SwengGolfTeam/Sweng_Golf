package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.swenggolf.ListOfferActivityTest.withRecyclerView;

public class SearchOffersTests {
    @Rule
    public final IntentsTestRule<MainMenuActivity> mActivityRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);
    FilledFakeDatabase database = (FilledFakeDatabase) FakeDatabase.fakeDatabaseCreator();

    @Before
    public void setup() {
        Database.setDebugDatabase(database);
        mActivityRule.launchActivity(new Intent());
    }

    @Test
    public void showFirstOffersWhenSearchIsEmpty() {
        checkTitle(database.getOffer(0));
    }

    private void checkTitle(Offer offer) {
        onView(withRecyclerView(R.id.offers_recycler_view).atPosition(0))
                .check(matches(hasDescendant(withText(offer.getTitle()))));
    }

    @Test
    public void showFirstOfferWhenClearingSearch() {
        onView(withId(R.id.search_bar)).perform(typeText("Hello"), clearText());
        checkTitle(database.getOffer(0));
    }

    @Test
    public void showRightOfferWhenSearchingWithEndOfTitle() {
        Offer offer = database.getOffer(5);
        search(offer.getTitle().substring(5));
        checkTitle(offer);
    }

    private void search(String substring) {
        onView(withId(R.id.search_bar)).perform(typeText(substring));
    }

    @Test
    public void showRightOfferWhenSearchingWithBeginningOfTitle() {
        Offer offer = database.getOffer(2);
        search(offer.getTitle().substring(0,5));
        checkTitle(offer);
    }

    @Test
    public void showRightOfferWhenSearchingWithSubstringOfTitle() {
        Offer offer = database.getOffer(10);
        search(offer.getTitle().substring(4,10));
        checkTitle(offer);
    }

    @Test
    public void searchIsNotCaseSensitiveUppercase() {
        Offer offer = database.getOffer(9);
        search(offer.getTitle().toUpperCase());
        checkTitle(offer);
    }

    @Test
    public void searchIsNotCaseSensitiveLowercase() {
        Offer offer = database.getOffer(8);
        search(offer.getTitle().toLowerCase());
        checkTitle(offer);
    }
}
