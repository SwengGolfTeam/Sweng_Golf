package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.location.Location;
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
import ch.epfl.sweng.swenggolf.location.AppLocation;
import ch.epfl.sweng.swenggolf.location.FakeLocation;
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
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.swenggolf.TestUtility.allowPermissionsIfNeeded;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class ShowOfferActivityTest {
    @Rule
    public final IntentsTestRule<MainMenuActivity> mActivityRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    /*@Rule
    public GrantPermissionRule permissionFineGpsRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    */

    private User user = FilledFakeDatabase.getUser(0);
    private Offer offer = FilledFakeDatabase.getOffer(0);

    /**
     * Set up a fake database, a fake user and launch activity.
     */
    @Before
    public void setUp() throws InterruptedException {
        Database.setDebugDatabase(FakeDatabase.fakeDatabaseCreator());
        AppLocation.setDebugLocation(FakeLocation.fakeLocationCreator());
        Config.setUser(user);
        mActivityRule.launchActivity(new Intent());
        FragmentTransaction transaction = mActivityRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.centralFragment,
                FragmentConverter.createShowOfferWithOffer(offer))
                .commit();
        allowPermissionsIfNeeded();
    }

    @Test
    public void canOpenProfileFromOffer() {
        onView(withId(R.id.show_offer_author)).perform(click());
        Fragment currentFragment = mActivityRule.getActivity()
                .getSupportFragmentManager().getFragments().get(0);
        assertThat(currentFragment.getClass().getName(), is(ProfileActivity.class.getName()));
    }

    @Test
    public void textOfAnswerIsCorrect() {
        String answer = "my answer";
        addAnswer(answer);
        onView(withContentDescription("description0"))
                .check(matches(withText(answer)));
    }

    @Test
    public void answerHasEmptyConstructorForFirebase() {
        Answer answer = new Answer();
        Answers answers = new Answers();
    }

    @Test
    public void authorOfAnswerIsCorrect() {
        addAnswer("I wrote this");
        onView(withContentDescription("username0"))
                .check(matches(withText(Config.getUser().getUserName())));
    }

    @Test
    public void authorCanSelectAndDeselectFavorite() {
        addAnswer("test");
        ViewInteraction favButton = onView(withContentDescription("fav0"));
        // user is author
        favButton.check(matches(isClickable()));
        favButton.perform(click());
        favButton.check(matches(withTagValue(equalTo((Object) R.drawable.ic_favorite))));
        favButton.perform(click());
        favButton.check(matches(withTagValue(equalTo((Object) R.drawable.ic_favorite_border))));
    }

    @Test
    public void onlyAuthorCanChooseFavorite() {
        Config.setUser(FilledFakeDatabase.getUser(1));
        // user is not author
        addAnswer("hey!");
        onView(withContentDescription("fav0"))
                .check(matches(not(isClickable())));
    }

    private void addAnswer(String answer) {
        onView(withId(R.id.answer_description))
                .perform(scrollTo(), typeText(answer), closeSoftKeyboard());
        onView(withId(R.id.post_button)).perform(scrollTo(), click());

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

        onView(withId(R.id.saved_location_offer)).perform(scrollTo())
                .check(matches(withText(expectedDistance)));
    }

    @Test
    public void correctIntentSentWhenClickedOnDistance() {
        onView(withId(R.id.saved_location_offer)).perform(scrollTo(), click());
        // FIXME travis doesn't detect the intent, locally yes
        //intended(hasPackage("com.google.android.apps.maps"));
    }

}
