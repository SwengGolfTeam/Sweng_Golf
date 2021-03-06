package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.location.AppLocation;
import ch.epfl.sweng.swenggolf.location.FakeLocation;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.create.CreateOfferFragment;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.statistics.OfferStats;
import ch.epfl.sweng.swenggolf.storage.FakeStorage;
import ch.epfl.sweng.swenggolf.storage.Storage;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.swenggolf.database.DatabaseUser.POINTS;
import static ch.epfl.sweng.swenggolf.profile.PointType.ADD_LOCALISATION;
import static ch.epfl.sweng.swenggolf.profile.PointType.ADD_PICTURE;
import static ch.epfl.sweng.swenggolf.profile.PointType.FOLLOW;
import static ch.epfl.sweng.swenggolf.profile.PointType.POST_OFFER;
import static ch.epfl.sweng.swenggolf.profile.PointType.RESPOND_OFFER;
import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;

@RunWith(AndroidJUnit4.class)
public class PointGainTest {

    @Rule
    public IntentsTestRule<MainMenuActivity> activityTestRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    @Rule
    public GrantPermissionRule permissionFineGpsRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    private static Offer createFakeOffer() {
        Offer newOffer = FilledFakeDatabase.getOffer(0);
        OfferStats.initializeNbViews(newOffer);
        return newOffer;
    }

    /**
     * Sets up the database and the location.
     */
    @Before
    public void setUpDatabase() {
        Storage.setDebugStorage(new FakeStorage(true));
        Database.setDebugDatabase(FakeDatabase.fakeDatabaseCreator());
        Config.setUser(FilledFakeDatabase.getUser(0));
        AppLocation.setDebugLocation(FakeLocation.fakeLocationCreator());
        Config.goToTest();
        activityTestRule.launchActivity(new Intent());
    }

    private void testUserPoints(final int newPoints, final User user) {
        Database.getInstance().read(Database.USERS_PATH + "/" + user.getUserId(),
                POINTS, new ValueListener<Integer>() {
                    @Override
                    public void onDataChange(Integer value) {
                        assertThat(user.getPoints(), is(newPoints));
                        assertThat(value, is(newPoints));
                    }

                    @Override
                    public void onCancelled(DbError error) {
                        fail();
                    }
                }, Integer.class);
    }

    private void testUserPointsDbOnly(final int newPoints, final User user) {
        Database.getInstance().read(Database.USERS_PATH + "/" + user.getUserId(),
                POINTS, new ValueListener<Integer>() {
                    @Override
                    public void onDataChange(Integer value) {
                        assertThat(value, is(newPoints));
                    }

                    @Override
                    public void onCancelled(DbError error) {
                        fail();
                    }
                }, Integer.class);
    }

    @Test
    public void createOfferYieldsPoints() {
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment, new CreateOfferFragment()).commit();
        CreateOfferFragmentTest.fillOffer();
        int scoredPoints = FilledFakeDatabase.getUser(0).getPoints()
                + POST_OFFER.getValue()
                + ADD_PICTURE.getValue()
                + ADD_LOCALISATION.getValue();
        testUserPoints(scoredPoints, Config.getUser());
    }

    @Test
    public void modifyingOfferChangesPoints() {
        Offer offer = (new Offer.Builder(createFakeOffer())).setLatitude(0).setLongitude(0)
                .setLinkPicture("").build();
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment,
                        FragmentConverter.createOfferActivityWithOffer(offer)).commit();
        CreateOfferFragmentTest.fillOffer();
        testUserPoints(ADD_PICTURE.getValue() + ADD_LOCALISATION.getValue(),
                Config.getUser());
    }

    @Test
    public void followingSomeoneIncreasesDecreasesHisPoints() {
        User otherUser = FilledFakeDatabase.getUser(1);
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment,
                        FragmentConverter.createShowProfileWithProfile(otherUser)).commit();
        onView(withId(R.id.follow)).perform(click());
        testUserPoints(-FOLLOW.getValue(), otherUser);
        onView(withId(R.id.follow)).perform(click());
        testUserPoints(0, otherUser);
    }

    @Test
    public void acceptingOfferIncreasesPoints() {
        Config.setUser(FilledFakeDatabase.getUser(0));
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment,
                        FragmentConverter.createShowOfferWithOffer(createFakeOffer())).commit();
        // User A adds answer
        Config.setUser(FilledFakeDatabase.getUser(2));
        TestUtility.addAnswer("For the empire !");
        // User B accepts it
        Config.setUser(FilledFakeDatabase.getUser(0));
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment,
                        FragmentConverter.createShowOfferWithOffer(createFakeOffer())).commit();
        TestUtility.showOfferCustomScrollTo();
        performFavoriteAction();
        testUserPointsDbOnly(RESPOND_OFFER.getValue(), FilledFakeDatabase.getUser(2));
        performFavoriteAction();
        testUserPointsDbOnly(0, FilledFakeDatabase.getUser(2));
    }

    private void performFavoriteAction() {
        onView(withContentDescription("fav0")).perform(click());
        onView(withText(android.R.string.yes)).perform(scrollTo(), click());
    }

    @Test
    public void removeOfferDecreasesPoints() {
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment, FragmentConverter
                        .createShowOfferWithOffer(createFakeOffer()))
                .commit();
        
        try {
            onView(withId(R.id.button_delete_offer)).perform(click());
        } catch (NoMatchingViewException | PerformException e) {
            onData(hasToString("Delete offer")).inRoot(isPlatformPopup())
                    .perform(click());
        }
        onView(withText(android.R.string.yes)).perform(scrollTo(), click());
        testUserPoints(-createFakeOffer().offerValue(), Config.getUser());
    }
}
