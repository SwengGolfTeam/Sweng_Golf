package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.LocalDatabase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class CategoriesMenuTest {

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    public static final String LOREM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
            + "Nam ut quam ornare, fringilla nunc eget, facilisis lectus."
            + "Curabitur ut nunc nec est feugiat commodo. Nulla vel porttitor justo."
            + "Suspendisse potenti. Morbi vehicula ante nibh,"
            + " at tristique tortor dignissim non."
            + "In sit amet ligula tempus, mattis massa dictum, mollis sem."
            + "Mauris convallis sed mauris ut sodales."
            + "Nullam tristique vel nisi a rutrum. Sed commodo nec libero sed volutpat."
            + "Fusce in nibh pharetra nunc pellentesque tempor id interdum est."
            + "Sed rutrum mauris in ipsum consequat, nec scelerisque nulla facilisis.";

    @Rule
    public final IntentsTestRule<MainMenuActivity> mActivityRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    /**
     * Configures a fake database and enables TestMode.
     */
    @Before
    public void init() throws InterruptedException {
        setUpFakeDatabase();
        Config.goToTest();
        mActivityRule.launchActivity(new Intent());
        // reinit local database to default values just in case test are not independent
        LocalDatabase localDb = new LocalDatabase(mActivityRule.getActivity(), null, 1);
        localDb.writeCategories(Arrays.asList(Category.values()));
    }

    protected static void setUpFakeDatabase() {
        Database database = new FakeDatabase(true);
        Offer offer1 = new Offer("user_id", "This is a title", LOREM, "", "idoftheoffer1");
        Offer offer2 = new Offer("user_id", "This is a title 2", LOREM, "", "idoftheoffer2");
        database.write("/offers", "idoftheoffer1", offer1);
        database.write("/offers", "idoftheoffer2", offer2);
        Database.setDebugDatabase(database);
        Config.setUser(new User("aaa", "user_id", "ccc", "ddd"));
        DatabaseUser.addUser(Config.getUser());
    }



    @Test
    public void uncheckAndCheckSameCategory() {
        clickOnCategoryInMenu(Category.getDefault());
        clickOnCategoryInMenu(Category.getDefault());

        Offer offer = ListOfferActivity.offerList.get(0);

        onView(withRecyclerView(R.id.offers_recycler_view).atPosition(0))
                .check(matches(hasDescendant(withText(offer.getTitle()))));
    }

    private void clickOnCategoryInMenu(Category cat){
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(cat.toString())).perform(click());
    }
}
