package ch.epfl.sweng.swenggolf;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ch.epfl.sweng.swenggolf.preference.ListPreferenceAdapter;

import static junit.framework.TestCase.assertEquals;

@RunWith(JUnit4.class)
public class PreferenceAdapterTest {

    @Test
    public void getItemCountReturnsRightValue(){
        ListPreferenceAdapter.debug = true;
        ListPreferenceAdapter adapter = new ListPreferenceAdapter();
        assertEquals(adapter.getItemCount(),ListPreferenceAdapter.USERS_INITIAL.length);
    }
}
