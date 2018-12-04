package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.leaderboard.Leaderboard;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;
import ch.epfl.sweng.swenggolf.offer.ListOwnOfferActivity;
import ch.epfl.sweng.swenggolf.offer.create.CreateOfferActivity;
import ch.epfl.sweng.swenggolf.preference.ListPreferencesActivity;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class MainMenuActivityInstrumentedTestIntents {
    @Rule
    public ActivityTestRule<MainMenuActivity> intentRule =
            new ActivityTestRule<>(MainMenuActivity.class, false, false);

    private void testReplacement(Class expectedClass, int id, boolean click) {
        if (click) {
            onView(withId(R.id.drawer)).perform(navigateTo(id));
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Fragment> frags = intentRule.getActivity().getSupportFragmentManager().getFragments();
        assertThat(frags.get(0).getClass().getName(), is(expectedClass.getName()));
    }

    /**
     * Create a fake database, launches the activity and opens the drawer.
     */
    @Before
    public void init() {
        Database.setDebugDatabase(FakeDatabase.fakeDatabaseCreator());
        Config.goToTest();
        Config.setUser(FilledFakeDatabase.getUser(0));
        intentRule.launchActivity(new Intent());
        Matcher v = withId(R.id.side_menu);
        onView(v).perform(open());
        Database.setDebugDatabase(FakeDatabase.fakeDatabaseCreator());
        Config.setUser(FilledFakeDatabase.getUser(0));
    }

    @Test
    public void testIntentOfferList() {
        testReplacement(ListOfferActivity.class, R.id.offers, true);
    }

    @Test
    public void testIntentPreferenceList() {
        testReplacement(ListPreferencesActivity.class, R.id.preference_activity, true);
    }

    @Test
    public void testIntentCreateOffer() {
        testReplacement(CreateOfferActivity.class, R.id.create_offer, true);
    }

    @Test
    public void testIntentProfile() {
        testReplacement(ProfileActivity.class, R.id.my_account, true);
    }

    @Test
    public void testIntentListOwnOffer() {
        testReplacement(ListOwnOfferActivity.class, R.id.my_offers, true);
    }

    @Test
    public void testIntentLeaderboard() {
        testReplacement(Leaderboard.class, R.id.leaderboard_activity, true);
    }

    @Test
    public void testIntentProfileByClickingOnPicture() {
        onView(ViewMatchers.withId(R.id.menu_header)).perform(click());
        testReplacement(ProfileActivity.class, 0, false);
    }

    @After
    public void release(){
        Config.quitTest();
        intentRule.finishActivity();
    }

}
