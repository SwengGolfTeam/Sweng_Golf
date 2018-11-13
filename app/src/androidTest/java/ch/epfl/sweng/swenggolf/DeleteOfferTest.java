package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.ShowOfferActivity;
import ch.epfl.sweng.swenggolf.storage.FakeStorage;
import ch.epfl.sweng.swenggolf.storage.Storage;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.object.HasToString.hasToString;
import static org.junit.Assert.fail;

/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
public class DeleteOfferTest {

    @Rule
    public final ActivityTestRule<MainMenuActivity> mActivityRule =
            new ActivityTestRule<>(MainMenuActivity.class, false, false);

    /**
     * Configures a fake database and enables TestMode.
     */
    @Before
    public void init() {
        Database.setDebugDatabase(FakeDatabase.fakeDatabaseCreator());
        Storage storage = new FakeStorage(true);
        Storage.setDebugStorage(storage);
        Config.setUser(FilledFakeDatabase.getUser(0));
        Config.goToTest();
        mActivityRule.launchActivity(new Intent());
    }

    @Test
    public void cancelDialog() {
        onView(withId(R.id.offers_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        deleteClick();
        onView(withText(android.R.string.no)).perform(scrollTo(), click());
        Fragment current = mActivityRule.getActivity()
                .getSupportFragmentManager().getFragments().get(0);
        assertThat(ShowOfferActivity.class.getName(), is(current.getClass().getName()));
    }

    @Test
    public void deleteElementTest() {
        onView(withId(R.id.offers_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //onView(withId(R.id.show_offer_title)).check(matches(withText(TITLE1)));
        ValueListener vl = new ValueListener() {
            @Override
            public void onDataChange(Object value) {
                //assertNull(value);
            }

            @Override
            public void onCancelled(DbError error) {
                fail();
            }
        };
        Database.getInstance().read(Database.OFFERS_PATH, "id1", vl, Offer.class);
        deleteClick();
        onView(withText(android.R.string.yes)).perform(scrollTo(), click());
    }

    private void deleteClick() {
        try {
            onView(withId(R.id.button_delete_offer)).perform(click());
        } catch (NoMatchingViewException e) {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            onData(hasToString("Delete offer")).inRoot(isPlatformPopup())
                    .inAdapterView(CoreMatchers.<View>instanceOf(AdapterView.class))
                    .perform(scrollTo(), ViewActions.click());
        }
    }
}