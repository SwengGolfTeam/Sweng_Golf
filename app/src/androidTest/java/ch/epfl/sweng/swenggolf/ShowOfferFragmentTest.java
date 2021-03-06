package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.GrantPermissionRule;
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
import ch.epfl.sweng.swenggolf.location.AppLocation;
import ch.epfl.sweng.swenggolf.location.FakeLocation;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.profile.ProfileFragment;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasPackage;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;


@RunWith(AndroidJUnit4.class)
public class ShowOfferFragmentTest {
    @Rule
    public final IntentsTestRule<MainMenuActivity> mActivityRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    @Rule
    public GrantPermissionRule permissionFineGpsRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    private User user = FilledFakeDatabase.getUser(0);
    private Offer offer = FilledFakeDatabase.getOffer(0);

    /**
     * Set up a fake database, a fake user and launch activity.
     */
    @Before
    public void setUp() {
        Database.setDebugDatabase(FakeDatabase.fakeDatabaseCreator());
        AppLocation.setDebugLocation(FakeLocation.fakeLocationCreator());
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
        assertThat(currentFragment.getClass().getName(), is(ProfileFragment.class.getName()));
    }

    @Test
    public void correctDistanceShowed() {
        Location l1 = new Location("");
        Location l2 = new Location("");

        l1.setLatitude(FilledFakeDatabase.FAKE_LATITUDE);
        l1.setLongitude(FilledFakeDatabase.FAKE_LONGITUDE);
        l2.setLatitude(FakeLocation.LATITUDE);
        l2.setLongitude(FakeLocation.LONGITUDE);

        int distance = (int) l1.distanceTo(l2) / 1000;

        // We assume that the distance is larger than 1 km
        String expectedDistance = distance + " km";

        onView(withId(R.id.saved_location_offer))
                .check(matches(withText(expectedDistance)));
    }

    @Test
    public void correctIntentSentWhenClickedOnDistance() {
        onView(withId(R.id.saved_location_offer)).perform(click());

        if (!isGoogleMapsInstalled()) {
            assert (true); //skip test
        } else {
            intended(hasPackage("com.google.android.apps.maps"));
        }
    }

    private boolean isGoogleMapsInstalled() {
        try {
            ApplicationInfo info = mActivityRule.getActivity().getPackageManager()
                    .getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Test
    public void imageDisplayedOnClick() {
        onView(withId(R.id.show_offer_picture)).perform(click());
        onView(withId(R.id.imageView))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }
}
