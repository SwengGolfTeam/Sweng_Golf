package ch.epfl.sweng.swenggolf;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.CreateUserActivity;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class CreateUserActivityTest {

    private static final String NAME = "Hello";
    private static final String NAME_CHANGED = "Hello World";
    private static final String MAIL = "Hello@World.ok";
    private static final String MAIL_CHANGED = "Hello@World.okapi";
    private static final String UID = "123456789009876543211234567890";
    private static final String PHOTO = "photo";

    @Rule
    public final ActivityTestRule<CreateUserActivity> mActivityRule =
            new ActivityTestRule<>(CreateUserActivity.class);

    /**
     * Initialise the Config and the Database for tests.
     */
    @BeforeClass
    public static void setUp() {
        Config.goToTest();
        User user1 = new User(NAME, UID, MAIL, PHOTO);
        Config.setUser(new User(user1));
        Database database = new FakeDatabase(true);
        Database.setDebugDatabase(database);

    }

    @Test
    public void canDisplay() {
        onView(withId(R.id.mail)).check(matches(withText(MAIL)));
        onView(withId(R.id.mail)).perform(typeText("api"));
        onView(withId(R.id.name)).check(matches(withText(NAME)));
        onView(withId(R.id.name)).perform(typeText(" World"));
        onView(withId(R.id.create_account)).perform(click());
        DatabaseUser.getUser(new ValueListener() {
            @Override
            public void onDataChange(Object value) {
                assertEquals("mail test", ((User) (value)).getEmail(), MAIL_CHANGED);
                assertEquals("NAME test", ((User) (value)).getUserName(), NAME_CHANGED);
            }

            @Override
            public void onCancelled(DbError error) {

            }
        }, Config.getUser());
    }

    @Test
    public void errorCorrectlyDisplayed() {
        onView(withId(R.id.mail)).perform(replaceText(""));
        onView(withId(R.id.create_account)).perform(click());
        onView(withText(R.string.incorrect_user_creation))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

}
