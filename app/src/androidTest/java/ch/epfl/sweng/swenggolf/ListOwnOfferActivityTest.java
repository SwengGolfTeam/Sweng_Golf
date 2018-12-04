package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.ListOwnOfferActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.profile.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.swenggolf.ListOfferActivityTest.withRecyclerView;

public class ListOwnOfferActivityTest {

    private final Database database = FakeDatabase.fakeDatabaseCreator();
    private final User user = FilledFakeDatabase.getUser(0);
    private final Offer offer = FilledFakeDatabase.getOffer(0);

    @Rule
    public IntentsTestRule<MainMenuActivity> mActivityRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    /**
     * Set up a fake database, a fake user and launch the fragment.
     */
    @Before
    public void init() {
        Database.setDebugDatabase(database);
        Config.goToTest();
        Config.setUser(user);
        mActivityRule.launchActivity(new Intent());
        mActivityRule.getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.centralFragment, new ListOwnOfferActivity()).commit();
    }

    @Test
    public void viewHasOwnOffers() {
        onView(withRecyclerView(R.id.offers_recycler_view).atPosition(0))
                .check(matches(hasDescendant(withText(offer.getTitle()))));
    }

    @After
    public void release(){
        Config.quitTest();
        mActivityRule.finishActivity();
    }
}
