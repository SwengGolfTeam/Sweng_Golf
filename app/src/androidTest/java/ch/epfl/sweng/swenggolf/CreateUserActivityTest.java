package ch.epfl.sweng.swenggolf;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.CreateUserActivity;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseConnection;
import ch.epfl.sweng.swenggolf.database.DatabaseError;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.main.MainActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CreateUserActivityTest {

    private static final String name = "Hello";
    private static final String namechanged = "Hello World";
    private static final String mail = "Hello@World.ok";
    private static final String mailchanged = "Hello@World.okapi";
    private static final String uid = "123456789009876543211234567890";
    private static final String photo = "photo";

    @Rule
    public final ActivityTestRule<CreateUserActivity> mActivityRule =
            new ActivityTestRule<>(CreateUserActivity.class);

    @BeforeClass
    public static void setUp(){
        Config.goToTest();
        User user1 = new User(name, uid , mail, photo);
        Config.setUser(new User(user1));
        Database database = new FakeDatabase(true);
        //database.write("/users", uid, user1);
        Database.setDebugDatabase(database);
    }

    @Test
    public void canDisplay() {
        onView(withId(R.id.mail)).check(matches(withText(mail)));
        onView(withId(R.id.mail)).perform(typeText("api"));
        onView(withId(R.id.name)).check(matches(withText(name)));
        onView(withId(R.id.name)).perform(typeText(" World"));
        onView(withId(R.id.create_account)).perform(click());
        DatabaseUser.getUser(new ValueListener() {
            @Override
            public void onDataChange(Object value) {
                assertEquals("mail test", ((User)(value)).getEmail(), mailchanged);
                assertEquals("name test", ((User)(value)).getUserName(), namechanged);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        }, Config.getUser());
    }

}
