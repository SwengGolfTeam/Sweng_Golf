package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.LocalDatabase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.profile.User;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class CategoriesMenuTest {

    public static final String LOREM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

    @Rule
    public final IntentsTestRule<MainMenuActivity> activityRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    private static void initFakeDatabase() {
        Database database = new FakeDatabase(true);
        Offer offer1 = new Offer("user_id", "This is a title", LOREM, "", "idoftheoffer1");
        database.write("/offers", "idoftheoffer1", offer1);
        Database.setDebugDatabase(database);
        Config.setUser(new User("aaa", "user_id", "ccc", "ddd"));
        DatabaseUser.addUser(Config.getUser());
    }

    /**
     * Configures a fake database and enables TestMode.
     */
    @Before
    public void init() {
        initFakeDatabase();
        Config.goToTest();
        activityRule.launchActivity(new Intent());
        initLocalDatabase();
    }

    @Test
    public void uncheckAndCheckSameCategory() {
        clickOnCategoryInMenu(Category.getDefault());
        clickOnCategoryInMenu(Category.getDefault());

        Offer offer = ListOfferActivity.offerList.get(0);

        onView(withRecyclerView(R.id.offers_recycler_view).atPosition(0))
                .check(matches(hasDescendant(withText(offer.getTitle()))));
    }

    private void clickOnCategoryInMenu(Category cat) {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(cat.toString())).perform(click());
    }

    private void initLocalDatabase() {
        LocalDatabase localDb = new LocalDatabase(activityRule.getActivity(), null, 1);
        List<Category> allCategories = Arrays.asList(Category.values());
        localDb.writeCategories(allCategories);
    }
}
