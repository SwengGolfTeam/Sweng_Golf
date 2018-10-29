package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.ShowOfferActivity;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.is;

@RunWith(AndroidJUnit4.class)
public class ShowOfferActivityTest {
    @Rule
    public final IntentsTestRule<MainMenuActivity> mActivityRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    /**
     * Set up a fake database, a fake user and launch activity.
     */
    @Before
    public void setUp() {
        Database.setDebugDatabase(FakeDatabase.fakeDatabaseCreator());
        User user = FilledFakeDatabase.getUser(0);
        Config.setUser(user);
        Offer offer = FilledFakeDatabase.getOffer(0);
        mActivityRule.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.centralFragment, TestUtility.offerFragment(new ShowOfferActivity(), offer)).commit();
    }

    @Test
    public void canOpenProfileFromOffer() {
        onView(withId(R.id.show_offer_author)).perform(click());
        assertThat(mActivityRule.getActivity().getSupportFragmentManager().getFragments().get(0).getClass().getName(), is(ProfileActivity.class.getName()));
    }


}
