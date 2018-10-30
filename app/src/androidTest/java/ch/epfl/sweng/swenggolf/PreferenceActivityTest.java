package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.FilledFakeDatabase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;

import ch.epfl.sweng.swenggolf.preference.ListPreferenceAdapter;
import ch.epfl.sweng.swenggolf.preference.ListPreferencesActivity;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.is;

@RunWith(AndroidJUnit4.class)
public class PreferenceActivityTest {

    @Rule
    public ActivityTestRule<MainMenuActivity> preferenceRule =
            new ActivityTestRule<>(MainMenuActivity.class, false, false);

    /**
     * Enters adapter debug mode.
     */
    @Before
    public void setUp() {
        Database fake = FakeDatabase.fakeDatabaseCreator();
        Database.setDebugDatabase(fake);
        preferenceRule.launchActivity(new Intent());
        preferenceRule.getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.centralFragment, new ListPreferencesActivity())
                    .commit();
    }

    /**
     * Scrolls to last element and check that it is displayed.
     */
    @Test
    public void scrollingWorks(){
        int userLength = FilledFakeDatabase.numberUser()-1;
        ViewAction scrollToLast =
                RecyclerViewActions
                        .<ListPreferenceAdapter.PreferenceViewHolder>scrollToPosition(userLength);
        onView(ViewMatchers.withId(R.id.preference_list)).perform(scrollToLast);
    }

    @Test
    public void testListSize(){
        ListPreferenceAdapter adapter = new ListPreferenceAdapter();
        assertThat(adapter.getItemCount(),is(FilledFakeDatabase.numberUser()));
    }

    @Test
    public void showProfileWhenClickOnView() {
        User user = FilledFakeDatabase.getUser(0);
        onView(withId(R.id.preference_list)).perform(actionOnItem(hasDescendant(
                ViewMatchers.withText(user.getUserName())), click()));
        Fragment fragment = preferenceRule.getActivity().getSupportFragmentManager().getFragments().get(0);
        assertThat(fragment.getClass().getName(), is(ProfileActivity.class.getName()));
        assertNotNull(fragment.getArguments().getParcelable("ch.epfl.sweng.swenggolf.user"));
    }
}
