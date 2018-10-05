package ch.epfl.sweng.sweng_golf;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static android.support.test.espresso.contrib.DrawerActions.openDrawer;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MainMenuActivityInstrumentedTest {
    @Rule public final ActivityTestRule<MainMenuActivity> mMenuRule = new ActivityTestRule<>(MainMenuActivity.class);

    @Test
    public void testCanOpenDrawer() {
        DrawerLayout drawer = mMenuRule.getActivity().findViewById(R.id.side_menu);
        openDrawer(R.id.side_menu);
        assertTrue("drawer was closed",drawer.isDrawerOpen(GravityCompat.START));
    }
}
