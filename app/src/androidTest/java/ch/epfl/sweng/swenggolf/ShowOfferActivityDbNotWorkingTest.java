package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.swenggolf.TestUtility.testToastShow;

@RunWith(AndroidJUnit4.class)
public class ShowOfferActivityDbNotWorkingTest {
    private static final FakeDatabase database = new FakeDatabase(true);
    private static final User user = new User("patrick", "0", "email",
            "photo", "preference");
    private static final Offer offer = (new Offer.Builder()).setUserId("0").setTitle("title")
            .setDescription("description").build();
    @Rule
    public final IntentsTestRule<MainMenuActivity> mActivityRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    /**
     * Set up a fake database, a fake user and launch activity.
     */
    @Before
    public void setUp() {
        database.write(Database.USERS_PATH, "0", user);
        database.write(Database.OFFERS_PATH, "0", offer);
        Database.setDebugDatabase(database);
        Config.setUser(user);
        mActivityRule.launchActivity(new Intent());
        mActivityRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment, FragmentConverter.createShowOfferWithOffer(offer))
                .commit();
    }

    @Test
    public void openProfileFromOfferShowToastOnFail() {
        database.setEntryNotWorking(Database.USERS_PATH, offer.getUserId());
        onView(withId(R.id.show_offer_author)).perform(scrollTo(), click());
        testToastShow(mActivityRule, R.string.error_load_user);
    }

    @Test
    public void displaysErrorMessageWhenAnswersCannotBeLoaded() {
        database.setEntryNotWorking(Database.ANSWERS_PATH, offer.getUuid());
        onView(withId(R.id.error_message)).perform(scrollTo()).check(matches(isDisplayed()));
    }


}
