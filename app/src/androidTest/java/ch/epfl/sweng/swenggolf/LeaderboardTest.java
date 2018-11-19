package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.contrib.DrawerMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.profile.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.fail;


@RunWith(AndroidJUnit4.class)
public class LeaderboardTest {

    private final static User USER2 =  new User("username2", "userId2", "email2", "photo2", "", "DEFAULT_DESCRIPTION2", 50);

    @Rule
    public final IntentsTestRule<MainMenuActivity> mActivityRule =
            new IntentsTestRule<>(MainMenuActivity.class, false, false);

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    /**
     * Set up a fake database with two offers.
     */
    protected static void setUpFakeDatabase() {
        Database database = new FakeDatabase(true);
        Config.goToTest();
        User user1 = new User("username1", "userId1", "email1", "photo1", "", "DEFAULT_DESCRIPTION1", 30);
        database.write("users", "userId1", user1);
        database.write("users", "userId2", USER2);
        Database.setDebugDatabase(database);
        DatabaseUser.addUser(Config.getUser());
    }

    /**
     * Configures a fake database and enables TestMode.
     */
    @Before
    public void init() {
        setUpFakeDatabase();
        Config.goToTest();
        mActivityRule.launchActivity(new Intent());
        mActivityRule.getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.centralFragment, new Leaderboard()).commit();
    }




    @Test
    public void userCorrectlyDisplayedAfterClickOnList() {
        onView(withId(R.id.users_recycler_view)).perform(actionOnItem(hasDescendant(
                ViewMatchers.withText(Leaderboard.userList.get(0).getUserName())), click()));

        onView(withId(R.id.name)).check(matches(withText(USER2.getUserName())));
    }


    @Test
    public void backFromListIsMenu() {
        onView(withContentDescription("abc_action_bar_home_description")).perform(click());
        onView(withId(R.id.side_menu)).check(matches(DrawerMatchers.isOpen()));
    }

}

