package ch.epfl.sweng.swenggolf;

import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.CreateOfferActivity;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainMenuActivityInstrumentedTestIntents {
    @Rule
    public IntentsTestRule<MainMenuActivity> intentRule =
            new IntentsTestRule(MainMenuActivity.class);

    private void testIntent(String c, int id) {
        onView(withId(R.id.drawer)).perform(NavigationViewActions.navigateTo(id));
        intended(hasComponent(c));
    }

    @Before
    public void setUp() {
        Matcher v = withId(R.id.side_menu);
        onView(v).perform(open());
    }

    @Test
    public void testIntentOfferList(){
        testIntent(ListOfferActivity.class.getName(),R.id.friends);
    }

    @Test
    public void testIntentCreateOffer(){
        testIntent(CreateOfferActivity.class.getName(),R.id.nearby_services);
    }

    @Test
    public void testIntentProfile(){
        testIntent(ProfileActivity.class.getName(),R.id.my_account);
    }
}
