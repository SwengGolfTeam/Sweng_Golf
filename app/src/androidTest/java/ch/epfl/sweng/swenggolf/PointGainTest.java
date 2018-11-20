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
import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.create.CreateOfferActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.storage.FakeStorage;
import ch.epfl.sweng.swenggolf.storage.Storage;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
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

    @Test
    public void createOfferYieldsPoints() {
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment, new CreateOfferActivity()).commit();
        CreateOfferActivityTest.fillOffer();
        int scoredPoints = FilledFakeDatabase.getUser(0).getPoints()
                + POST_OFFER.getValue()
                + ADD_PICTURE.getValue()
                + ADD_LOCALISATION.getValue();
        testUserPoints(scoredPoints, Config.getUser());
    }

    @Test
    public void modifyingOfferChangesPoints() {
        Offer offer = new Offer(Config.getUser().getUserId(), "test",
                "test", "", "23",
                Category.values()[3], 123123123, 123123123);
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment,
                        FragmentConverter.createOfferActivityWithOffer(offer)).commit();
        CreateOfferActivityTest.fillOffer();
        testUserPoints(ADD_PICTURE.getValue() + ADD_LOCALISATION.getValue(), Config.getUser());
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
        Offer offer = new Offer(Config.getUser().getUserId(), "test",
                "test", "", "23",
                Category.values()[3], 123123123, 123123123);
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment,
                        FragmentConverter.createShowOfferWithOffer(offer)).commit();
        addAnswer();
        performFavoriteAction();
        testUserPoints(RESPOND_OFFER.getValue(), Config.getUser());
        performFavoriteAction();
        testUserPoints(0, Config.getUser());
    }

    private void addAnswer() {
        onView(withId(R.id.react_button)).perform(scrollTo(), click());
        onView(withId(R.id.your_answer_description))
                .perform(scrollTo(),
                        typeText("For the empire !"), closeSoftKeyboard());
        onView(withId(R.id.post_button)).perform(scrollTo(), click());
    }

    private void performFavoriteAction() {
        onView(withContentDescription("fav0")).perform(scrollTo(), click());
        onView(withText(android.R.string.yes)).perform(scrollTo(), click());
    }

    @Test
    public void removeOfferDecreasesPoints() {
        Offer offer = new Offer(Config.getUser().getUserId(), "test",
                "test", "", "23",
                Category.values()[3], 123123123, 123123123);
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.centralFragment, FragmentConverter.createShowOfferWithOffer(offer))
                .commit();

        //TODO: find why the test fail if we have this line
       // openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        try {
            onView(withId(R.id.button_delete_offer)).perform(click());
        } catch (NoMatchingViewException | PerformException e) {
            onData(hasToString("Delete offer")).inRoot(isPlatformPopup()).perform(click());
        }
        onView(withText(android.R.string.yes)).perform(scrollTo(), click());
        testUserPoints(-offer.offerValue(), Config.getUser());
    }
}
