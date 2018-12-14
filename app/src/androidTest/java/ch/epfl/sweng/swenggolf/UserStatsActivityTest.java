package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.contrib.DrawerMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentTransaction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.statistics.UserStats;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

@RunWith(AndroidJUnit4.class)
public class UserStatsActivityTest {

    @Rule
    public IntentsTestRule<MainMenuActivity> mActivityRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    /**
     * Sets up the database and the launches the activity.
     */
    @Before
    public void setUp() {
        Database.setDebugDatabase(FilledFakeDatabase.fakeDatabaseCreator());
        Config.setUser(FilledFakeDatabase.getUser(0));
        mActivityRule.launchActivity(new Intent());
    }

    @Test
    public void allStatsAreDisplayedTest() {
        //working Database
        processTransaction(Config.getUser());
        checkAllStatsAreDisplayed();
    }

    @Test
    public void hashMapSetterAndGetterAreOkay() { // for Firebase to work we need them
        UserStats stats = new UserStats();
        HashMap<String, Integer> newMap = new HashMap<>();

        stats.setMap(newMap);
        assertEquals(newMap, stats.getMap());
    }

    @Test
    public void checkBackwardsCompatibilityTest() {
        String newUserId = "newUserId";
        UserStats.checkBackwardsCompatibility(DbError.DATA_DOES_NOT_EXIST, newUserId);

        ValueListener<UserStats> listener = new ValueListener<UserStats>() {
            @Override
            public void onDataChange(UserStats stats) { // check that initialized correctly
                for (UserStats.Stats stat : UserStats.Stats.values()) {
                    assertEquals(UserStats.INITIAL_VALUE, stats.getValue(stat));
                }
            }

            @Override
            public void onCancelled(DbError error) {
                fail();
            }
        };

        UserStats.read(listener, newUserId);
        // Check that following call does not throw exception
        UserStats.checkBackwardsCompatibility(DbError.DISCONNECTED, newUserId);
    }

    @Test
    public void canOpenMenuWithButton() {
        onView(withContentDescription("abc_action_bar_home_description")).perform(click());
        onView(withId(R.id.side_menu)).check(matches(DrawerMatchers.isOpen()));
    }

    @Test
    public void databaseNotWorkingTest() {
        Database.setDebugDatabase(new FakeDatabase(false));
        processTransaction(Config.getUser());
        checkAllStatsAreDisplayed();
    }


    private void processTransaction(User user) {
        FragmentTransaction transaction = mActivityRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.centralFragment,
                FragmentConverter.createStatisticsActivityWithUser(user))
                .commit();
    }

    private void checkAllStatsAreDisplayed() {
        UserStats stats = new UserStats();

        for (int i = 0; i < stats.size(); i++) {
            UserStats.Stats stat = stats.getKey(i);
            onView(withText(stat.getText())).check(matches(isDisplayed()));
            onView(withText(stat.getText())).perform(swipeUp());
        }
    }
}
