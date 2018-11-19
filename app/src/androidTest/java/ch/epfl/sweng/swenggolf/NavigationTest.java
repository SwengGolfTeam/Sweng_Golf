package ch.epfl.sweng.swenggolf;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.CreateOfferActivity;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;
import ch.epfl.sweng.swenggolf.offer.ListOwnOfferActivity;
import ch.epfl.sweng.swenggolf.offer.ShowOfferActivity;
import ch.epfl.sweng.swenggolf.preference.ListPreferencesActivity;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;
import ch.epfl.sweng.swenggolf.storage.FakeStorage;
import ch.epfl.sweng.swenggolf.storage.Storage;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.swenggolf.ListOfferActivityTest.withRecyclerView;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;

public class NavigationTest {
    @Rule
    public final IntentsTestRule<MainMenuActivity> intentRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    @Rule
    public GrantPermissionRule permissionFineGpsRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    private final KeyEvent goBack = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);

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
        for(int i = 0; i <4; ++i) {
        onView(withId(R.id.edit_profile)).perform(click());
        onView(withId(R.id.saveButton)).perform(click());
        }

        checkFragmentShown(ProfileActivity.class);

        pressBackButton();
        checkFragmentShown(ListOfferActivity.class);
    }

    @NonNull
    private <T extends Fragment> FragmentManager checkFragmentShown(Class<T> fragmentExpected) {
        final MainMenuActivity activity = intentRule.getActivity();
        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        List<Fragment> frags = fragmentManager.getFragments();
        boolean condition = false;
        for(Fragment fragment : frags) {
            if(fragment.getClass().getName().equals(fragmentExpected.getName())){
                condition = true;
                break;
            }
        }
        assertTrue(condition);
        return fragmentManager;
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
    public void goToFollowingAndBackToMenu() {
        goToXAndBackToMenu(R.id.preference_activity, ListPreferencesActivity.class);
    }

    @Test
    public void goToOfferAndBackToMyOffer() {
        goToShowOffer();
        pressBackButton();
        checkFragmentShown(ListOwnOfferActivity.class);
    }

    private void goToShowOffer() {
        clickOnDrawer(R.id.my_offers);
        onView(withRecyclerView(R.id.offers_recycler_view).atPosition(0)).perform(click());
        for(int i = 0; i< 4; ++i) {
            onView(withId(R.id.button_modify_offer)).perform(click());
            onView(withId(R.id.button_create_offer)).perform(scrollTo(), click());
        }
        checkFragmentShown(ShowOfferActivity.class);
    }

    @Test
    public void deleteOffer() {
        goToShowOffer();
        onView(withId(R.id.button_delete_offer)).perform(click());
        onView(withText(android.R.string.yes)).perform(click());
        //TODO : maybe we need a sleep here
        checkFragmentShown(ListOwnOfferActivity.class);

       /* try {
            onView(withId(R.id.button_delete_offer)).perform(click());
        } catch (NoMatchingViewException | PerformException e) {
            onData(hasToString("Delete offer")).inRoot(isPlatformPopup()).perform(click());
        }
        onView(withText(android.R.string.yes)).perform(scrollTo(), click());*/
    }

    private <T> void goToXAndBackToMenu(int menuItemId, Class<T> destinationClass) {
        clickOnDrawer(menuItemId);
        final FragmentManager fragmentManager = intentRule.getActivity().getSupportFragmentManager();
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
