package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.Answer;
import ch.epfl.sweng.swenggolf.offer.Answers;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.is;

import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class ShowOfferActivityTest {
    @Rule
    public final IntentsTestRule<MainMenuActivity> mActivityRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    private User user = FilledFakeDatabase.getUser(0);
    private Offer offer = FilledFakeDatabase.getOffer(0);

    /**
     * Set up a fake database, a fake user and launch activity.
     */
    @Before
    public void setUp() {
        Database.setDebugDatabase(FakeDatabase.fakeDatabaseCreator());
        Config.setUser(user);
        mActivityRule.launchActivity(new Intent());
        FragmentTransaction transaction = mActivityRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.centralFragment,
                FragmentConverter.createShowOfferWithOffer(offer))
                .commit();
    }



    @Test
    public void canOpenProfileFromOffer() {
        onView(withId(R.id.show_offer_author)).perform(click());
        Fragment currentFragment = mActivityRule.getActivity()
                .getSupportFragmentManager().getFragments().get(0);
        assertThat(currentFragment.getClass().getName(), is(ProfileActivity.class.getName()));
    }

}
