package ch.epfl.sweng.swenggolf;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.Database.FirebaseAccount;
import ch.epfl.sweng.swenggolf.Main.MainMenuActivity;

import static android.support.test.espresso.contrib.DrawerActions.openDrawer;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MainMenuActivityInstrumentedTestUnavailable {

    @Rule
    public final ActivityTestRule<MainMenuActivity> mMenuRule = new ActivityTestRule<>(MainMenuActivity.class);

    @Test
    public void testCanOpenDrawer() {
        DrawerLayout drawer = mMenuRule.getActivity().findViewById(R.id.side_menu);
        openDrawer(R.id.side_menu);
        assertTrue("drawer was closed",drawer.isDrawerOpen(GravityCompat.START));
    }

    @Test
    public void usernameFieldIsDefault(){
        FirebaseAccount.test = false;
        TextView t = mMenuRule.getActivity().findViewById(R.id.username);
        assertThat(t.getText().toString(),is("username"));
    }

    @Test
    public void userIdFieldIsDefault() {
        FirebaseAccount.test = false;
        TextView t = mMenuRule.getActivity().findViewById(R.id.usermail);
        assertThat(t.getText().toString(),is("userid"));
    }
}
