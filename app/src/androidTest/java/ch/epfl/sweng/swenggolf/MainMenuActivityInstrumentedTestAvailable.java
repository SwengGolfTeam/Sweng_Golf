package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.widget.TextView;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.profile.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.close;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(AndroidJUnit4.class)
public class MainMenuActivityInstrumentedTestAvailable {

    @Rule
    public final ActivityTestRule<MainMenuActivity> mMenuRule =
            new ActivityTestRule<>(MainMenuActivity.class, false, false);

    /**
     * Inits a Debugdatabase and creates the stats for the user.
     */
    @Before
    public void setUp() {
        Config.goToTest();
        Config.setUser(new User("usernameValid", "userIdValid", "emailValid", "photoValid"));
        Database.setDebugDatabase(new FakeDatabase(true));
        mMenuRule.launchActivity(new Intent());
    }

    @Test
    public void usernameFieldIsOfUser() {
        TextView t = mMenuRule.getActivity().findViewById(R.id.username);
        assertThat(t.getText().toString(), is("usernameValid"));
    }

    @Test
    public void userMailFieldIsOfUser() {
        TextView t = mMenuRule.getActivity().findViewById(R.id.usermail);
        assertThat(t.getText().toString(), is("emailValid"));
    }

    @Test
    public void canCloseAndOpenDrawerWithButton() {
        Matcher v = withId(R.id.side_menu);
        onView(withContentDescription("abc_action_bar_home_description")).perform(click());
        onView(v).check(matches(isOpen(Gravity.LEFT)));
        onView(v).perform(close());
        onView(v).check(matches(isClosed(Gravity.LEFT)));
    }

    @Test
    public void rotationDoesntYieldANewFragment() throws InterruptedException {
        List<Fragment> currents = mMenuRule.getActivity()
                .getSupportFragmentManager().getFragments();
        int sizeBefore = currents.size();
        mMenuRule.getActivity()
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Thread.sleep(500);
        List<Fragment> after = mMenuRule.getActivity().getSupportFragmentManager().getFragments();
        int sizeAfter = after.size();
        assertThat(sizeAfter, is(sizeBefore));
    }
}


