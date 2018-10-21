package ch.epfl.sweng.swenggolf;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.WaitingActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;

@RunWith(AndroidJUnit4.class)
public class WaitingActivityTest2 {

    @Rule
    public final IntentsTestRule<WaitingActivity> mActivityRule =
            new IntentsTestRule<>(WaitingActivity.class);


    /**
     * Initialise the Config and the Database for tests.
     */
    @Before
    public void setUp() {
        Config.goToTest();
        Database database = new FakeDatabase(false);
        Database.setDebugDatabase(database);
    }

    @Test
    public void connectionErrorTest() {
        Config.setActivityCallback(new ActivityCallback() {
            @Override
            public void isDone() {
                System.out.println("Hello");
                onView(withText(R.string.connection_error))
                        .inRoot(withDecorView(not(is(
                                mActivityRule.getActivity().getWindow().getDecorView()))))
                        .check(matches(isDisplayed()));

            }
        });
    }

}
