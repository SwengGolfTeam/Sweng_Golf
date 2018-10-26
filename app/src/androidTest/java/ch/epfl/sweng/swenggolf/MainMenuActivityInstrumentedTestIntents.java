package ch.epfl.sweng.swenggolf;

import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.CreateOfferActivity;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;
import ch.epfl.sweng.swenggolf.preference.ListPreferencesActivity;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

import static android.support.test.espresso.contrib.DrawerActions.open;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class MainMenuActivityInstrumentedTestIntents {
    @Rule
    public ActivityTestRule<MainMenuActivity> intentRule =
            new ActivityTestRule<>(MainMenuActivity.class);

    private void testReplacement(String className, int id) {
        onView(ViewMatchers.withId(R.id.drawer)).perform(NavigationViewActions.navigateTo(id));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Fragment> frags = ((AppCompatActivity)intentRule.getActivity()).getSupportFragmentManager().getFragments();
        assertThat(frags.get(0).getClass().getName(), is(className));
    }

    @Before
    public void setUp() {
        Database.setDebugDatabase(FakeDatabase.fakeDatabaseCreator());
        Matcher v = withId(R.id.side_menu);
        onView(v).perform(open());
    }

    @Test
    public void testIntentOfferList(){
        testReplacement(ListOfferActivity.class.getName(),R.id.offers);
    }

    @Test
    public void testIntentPreferenceList() {
        testReplacement(ListPreferencesActivity.class.getName(), R.id.preference_activity);
    }

    @Test
    public void testIntentCreateOffer(){
        testReplacement(CreateOfferActivity.class.getName(),R.id.create_offer);
    }

    @Test
    public void testIntentProfile() {
        testReplacement(ProfileActivity.class.getName(), R.id.my_account);
    }

    @Test
    public void testIntentProfileByClickingOnPicture() {
        onView(ViewMatchers.withId(R.id.menu_header)).perform(click());
        intended(hasComponent(ProfileActivity.class.getName()));
    }

}
