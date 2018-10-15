package ch.epfl.sweng.swenggolf;

import android.support.test.rule.ActivityTestRule;
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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class WaitingActivityTest {

    private static final String name = "Hello";
    private static final String namechanged = "Hello World";
    private static final String mail = "Hello@World.ok";
    private static final String mailchanged = "Hello@World.okapi";
    private static final String uid1 = "1234";
    private static final String uid2 = "5678";
    private static final String photo = "Picsou";

    @Rule
    public final ActivityTestRule<WaitingActivity> mActivityRule =
            new ActivityTestRule<>(WaitingActivity.class);

    @Before
    public void init() {
        ch.epfl.sweng.swenggolf.database.Database database = new FakeDatabase(true);
        User user1 = new User(name,uid1,mail,photo);
        User user2 = new User(namechanged,uid2,mailchanged,photo);
        database.write("/users", uid1, user1);
        database.write("/users", uid2, user2);
        Database.setDebugDatabase(database);
    }

    @Test
    public void canDisplay() {
        onView(withId(R.id.waiting_msg)).check(matches(withText("Please wait.")));
    }

}

