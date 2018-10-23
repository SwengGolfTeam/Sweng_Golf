package ch.epfl.sweng.swenggolf;

import android.support.design.widget.NavigationView;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.CreateOfferActivity;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;
import ch.epfl.sweng.swenggolf.preference.ListPreferencesActivity;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.close;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class MainMenuActivityInstrumentedTestIntents {
    @Rule
    public IntentsTestRule<MainMenuActivity> intentRule =
            new IntentsTestRule(MainMenuActivity.class);

    private void testIntent(String className, int id) {
        onView(ViewMatchers.withId(R.id.drawer)).perform(NavigationViewActions.navigateTo(id));
        intended(hasComponent(className));
    }

    @Before
    public void setUp() {
        Matcher v = withId(R.id.side_menu);
        onView(v).perform(open());
    }

    @Test
    public void testIntentOfferList(){
        ListOfferActivityTest.setUpFakeDatabase();
        testIntent(ListOfferActivity.class.getName(),R.id.offers);
    }

    @Test
    public void testIntentCreateOffer(){
        testIntent(CreateOfferActivity.class.getName(),R.id.create_offer);
    }

    @Test
    public void testIntentPreferences() {
        testIntent(ListPreferencesActivity.class.getName(), R.id.preference_activity);
    }

    @Test
    public void testIntentProfile() {
        testIntent(ProfileActivity.class.getName(), R.id.my_account);
    }

    @Test
    public void testIntentProfileByClickingOnPicture() {
        onView(ViewMatchers.withId(R.id.menu_header)).perform(click());
        intended(hasComponent(ProfileActivity.class.getName()));
    }

}
