package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentTransaction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.statistics.OfferStats;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.fail;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
public class ShowOfferStatsTest {
    @Rule
    public final IntentsTestRule<MainMenuActivity> mActivityRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    private User user = FilledFakeDatabase.getUser(0);
    private Offer offer;

    /**
     * Set up a fake database, a fake user and launch activity.
     */
    @Before
    public void setUp() {
        Database.setDebugDatabase(FakeDatabase.fakeDatabaseCreator());
        Config.setUser(user);
        mActivityRule.launchActivity(new Intent());
    }

    @Test
    public void statsAreDisplayedOnOwnOffer() {
        offer = FilledFakeDatabase.getOffer(0);
        processTransaction(offer);
        TestUtility.showOfferCustomScrollTo();
        onView(withId(R.id.show_offer_views)).check(matches(isDisplayed()));
    }

    @Test
    public void statsAreHiddenOnNotOwnOffer() {
        offer = FilledFakeDatabase.getOffer(1);
        processTransaction(offer);
        TestUtility.showOfferCustomScrollTo();
        onView(withId(R.id.show_offer_views)).check(matches(not(isDisplayed())));
    }

    @Test
    public void statsAreIncremented() {
        offer = FilledFakeDatabase.getOffer(2);

        //visit offer n°2 with user n°0
        processTransaction(offer);

        //visit offer n°2 with user n°8 (author)
        Config.setUser(FilledFakeDatabase.getUser(8));
        processTransaction(offer);

        // test if displayed and incremented
        onView(withId(R.id.show_offer_views)).check(matches(isDisplayed()));
        ValueListener<Integer> listener = new ValueListener<Integer>() {
            @Override
            public void onDataChange(Integer nb) {
                assert (nb == OfferStats.INITIAL_NB_VIEWS + 1); // incremented once
            }

            @Override
            public void onCancelled(DbError error) {
                fail();
            }
        };

        OfferStats.getNbViews(listener, offer);
    }

    @Test
    public void statsAreDefaultWhenDbNotWorking() {
        Database.setDebugDatabase(new FakeDatabase(false));
        offer = FilledFakeDatabase.getOffer(0);
        processTransaction(offer);
        TestUtility.showOfferCustomScrollTo();
        onView(withId(R.id.show_offer_views)).check(matches(isDisplayed()));
    }

    @Test
    public void initializeStatsAfterDataNotFound() {
        Offer newOffer = (new Offer.Builder()).setTitle("title")
                .setDescription("description").setUserId("userid").setUuid("newid").build();
        OfferStats.checkBackwardsCompatibility(DbError.DATA_DOES_NOT_EXIST, newOffer);

        ValueListener<Integer> listener = new ValueListener<Integer>() {
            @Override
            public void onDataChange(Integer nb) {
                assert (nb == OfferStats.INITIAL_NB_VIEWS); // exists
            }

            @Override
            public void onCancelled(DbError error) {
                fail();
            }
        };

        OfferStats.getNbViews(listener, newOffer);
        // Check that following call does not throw exception
        OfferStats.checkBackwardsCompatibility(DbError.DISCONNECTED, newOffer);
    }

    private void processTransaction(Offer offer) {
        FragmentTransaction transaction = mActivityRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.centralFragment,
                FragmentConverter.createShowOfferWithOffer(offer))
                .commit();
    }
}
