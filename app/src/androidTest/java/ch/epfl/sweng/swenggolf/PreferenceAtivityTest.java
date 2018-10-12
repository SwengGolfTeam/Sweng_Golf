package ch.epfl.sweng.swenggolf;


import android.app.Activity;
import android.app.ListActivity;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.preference.ListPreferenceAdapter;
import ch.epfl.sweng.swenggolf.preference.ListPreferencesActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PreferenceAtivityTest {
    @Rule
    ActivityTestRule preferenceRule = new ActivityTestRule<ListPreferencesActivity>(ListPreferencesActivity.class);

    @Test
    public void onScrollLastLoadsMoreElements(){
        RecyclerView.Adapter rw = ((ListPreferencesActivity)preferenceRule.getActivity()).mAdapter;
        int itemLoadedOriginal = rw.getItemCount();
        onView(withId(R.id.preference_list)).perform(RecyclerViewActions.<ListPreferenceAdapter.PreferenceViewHolder>scrollToPosition(rw.getItemCount()-1));
        assertTrue(itemLoadedOriginal < rw.getItemCount());
    }

}
