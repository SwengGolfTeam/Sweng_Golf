package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;
import ch.epfl.sweng.swenggolf.offer.ListOwnOfferActivity;
import ch.epfl.sweng.swenggolf.offer.ShowOfferActivity;
import ch.epfl.sweng.swenggolf.offer.create.CreateOfferActivity;
import ch.epfl.sweng.swenggolf.preference.ListPreferencesActivity;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;
import ch.epfl.sweng.swenggolf.storage.FakeStorage;
import ch.epfl.sweng.swenggolf.storage.Storage;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.swenggolf.ListOfferActivityTest.withRecyclerView;
import static junit.framework.TestCase.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

public class NavigationTest {
    @Rule
    public final IntentsTestRule<MainMenuActivity> intentRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    @Rule
    public GrantPermissionRule permissionFineGpsRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    /**
     * Set up the fake database, the user, the fake storage and launch activity.
     */
    @Before
    public void setUp() {
        Database database = FakeDatabase.fakeDatabaseCreator();
        Database.setDebugDatabase(database);
        FakeStorage storage = new FakeStorage(true);
        Storage.setDebugStorage(storage);
        Config.setUser(FilledFakeDatabase.getUser(0));
        intentRule.launchActivity(new Intent());
    }

    @Test
    public void goToEditUserAndBackToMenu() {

        clickOnDrawer(R.id.my_account);
        //Edit profile multiple times
        closeSoftKeyboard();
        editMultipleTimes(R.id.edit_profile, R.id.saveButton, 4);

        checkFragmentShown(ProfileActivity.class);

        pressBackButton();
        checkFragmentShown(ListOfferActivity.class);
    }

    private void editMultipleTimes(int modifyButton, int saveButton, int numberOfIterations) {
        for (int i = 0; i < numberOfIterations; ++i) {
            onView(withId(modifyButton)).perform(click());
            onView(withId(saveButton)).perform(scrollTo(), click());
        }
    }

    @NonNull
    private <T extends Fragment> void checkFragmentShown(Class<T> fragmentExpected) {
        final MainMenuActivity activity = intentRule.getActivity();
        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        List<Fragment> frags = fragmentManager.getFragments();

        for (Fragment fragment : frags) {
            if (fragment.getClass().getName().equals(fragmentExpected.getName())) {
                return;
            }
        }
        fail();
    }

    @Test
    public void goToMyOffersAndBackToMenu() {
        goToXAndBackToMenu(R.id.my_offers, ListOwnOfferActivity.class);
    }

    @Test
    public void goToCreateOfferAndBackToMenu() {
        goToXAndBackToMenu(R.id.create_offer, CreateOfferActivity.class);
    }

    @Test
    public void goToCreateOfferAndBackToMenuUsingArrow() {
        clickOnDrawer(R.id.create_offer);
        checkFragmentShown(CreateOfferActivity.class);
        onView(withContentDescription("abc_action_bar_home_description")).perform(click());
        checkFragmentShown(ListOfferActivity.class);
    }

    @Test
    public void goToFollowingAndBackToMenu() {
        goToXAndBackToMenu(R.id.preference_activity, ListPreferencesActivity.class);
    }

    @Test
    public void goToOfferAndBackToMyOffer() {
        goToShowOffer();
        pressBackButton();
        checkFragmentShown(ListOwnOfferActivity.class);
    }

    @Test
    public void goToOfferAndBackToMyOfferUsingArrow() {
        goToShowOffer();
        checkFragmentShown(ShowOfferActivity.class);
        onView(withContentDescription("abc_action_bar_home_description")).perform(click());
        checkFragmentShown(ListOwnOfferActivity.class);
    }

    private void goToShowOffer() {
        clickOnDrawer(R.id.my_offers);
        onView(withRecyclerView(R.id.offers_recycler_view).atPosition(0)).perform(click());
        editMultipleTimes(R.id.button_modify_offer, R.id.button_create_offer, 4);
        checkFragmentShown(ShowOfferActivity.class);
    }

    @Test
    public void deleteOffer() {
        goToShowOffer();
        onView(withId(R.id.button_delete_offer)).perform(click());
        onView(withText(android.R.string.yes)).perform(click());
        checkFragmentShown(ListOwnOfferActivity.class);
    }

    @Test
    public void goHomeScreen() {
        pressBackButton();
        final int expectedNumberOfFragments = 1;
        assertEquals(expectedNumberOfFragments,
                intentRule.getActivity().getSupportFragmentManager().getFragments().size());
    }

    private <T> void goToXAndBackToMenu(int menuItemId, Class<T> destinationClass) {
        clickOnDrawer(menuItemId);
        final FragmentManager fragmentManager =
                intentRule.getActivity().getSupportFragmentManager();
        List<Fragment> frags = fragmentManager.getFragments();
        assertThat(frags.get(0).getClass().getName(), is(destinationClass.getName()));

        pressBackButton();
        frags = fragmentManager.getFragments();
        assertThat(frags.get(0).getClass().getName(), is(ListOfferActivity.class.getName()));
    }

    private void clickOnDrawer(int menuItemId) {
        onView(withId(R.id.side_menu)).perform(open());
        onView(withId(R.id.drawer)).perform(navigateTo(menuItemId));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void pressBackButton() {
        try {
            intentRule.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    intentRule.getActivity().onBackPressed();
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
