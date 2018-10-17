package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.preference.ListPreferenceAdapter;
import ch.epfl.sweng.swenggolf.preference.ListPreferencesActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;

@RunWith(AndroidJUnit4.class)
public class PreferenceActivityTest {

    private static final User[] USERS_INITIAL = {
            new User("Anna", "0", "anna@mail.com", "Tomatoes"),
            new User("Bob", "1", "bob@mail.com", "Screwdriver"),
            new User("Geany", "2", "geany@mail.com", "Comics"),
            new User("Greg", "3", "greg@gmail.com", "Ropes"),
            new User("Fred", "4", "fred@gmail.com", "Beverages"),
            new User("AAnna", "5", "aanna@mail.com", "Friends"),
            new User("ABob", "6", "abob@mail.com", "Washing machine"),
            new User("AGeany", "7", "ageany@mail.com", "Hammer"),
            new User("AGreg", "8", "agreg@gmail.com", "Lunch"),
            new User("AFred", "9", "afred@gmail.com", "Cheeseburgers"),
            new User("BAnna", "10", "banna@mail.com", "Champaign"),
            new User("BBob", "11", "bbob@mail.com", "Mushrooms"),
            new User("BGeany", "12", "bgeany@mail.com", "Nothing"),
            new User("BGreg", "13", "bgreg@gmail.com", "Fries"),
            new User("BFreEricisSIstirusiwssjdsidjsidskdisjdijsmdisjd",
                    "14", "fr@gmail.com", "A nice sweatshirt, some hot shoes and a poncho")
    };

    @Rule
    public ActivityTestRule preferenceRule =
            new ActivityTestRule<>(ListPreferencesActivity.class,false,false);

    /**
     * Enters adapter debug mode.
     */
    @Before
    public void setUp() throws InterruptedException{
        Database usersDatabase = new FakeDatabase(true);
        for(User user : USERS_INITIAL){
            usersDatabase.write("/users",user.getUserId(),user);
        }
        Database.setDebugDatabase(usersDatabase);
        preferenceRule.launchActivity(new Intent());
        Thread.sleep(4000);
    }

    /**
     * Scrolls to last element and check that it is displayed.
     */
    @Test
    public void scrollingWorks(){
        int userLength = USERS_INITIAL.length-1;
        ViewAction scrollToLast =
                RecyclerViewActions
                        .<ListPreferenceAdapter.PreferenceViewHolder>scrollToPosition(userLength);
        onView(ViewMatchers.withId(R.id.preference_list)).perform(scrollToLast);
    }

    @Test
    public void testListSize(){
        ListPreferenceAdapter adapter = new ListPreferenceAdapter();
        assertThat(adapter.getItemCount(),is(USERS_INITIAL.length));
    }
}
