package ch.epfl.sweng.sweng_golf;

import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainMenuActivityTest {
    @Rule public final ActivityTestRule<MainMenuActivity> mMenuRule = new ActivityTestRule<>(MainMenuActivity.class);

    @Test
    public void testCanOpenDrawer() {
        onView(withId(R.id.side_menu)).perform(swipeRight());
        onView(withId(R.id.side_menu)).perform(swipeLeft());
        onView(withId(R.id.main_menu_frame)).perform(new GeneralSwipeAction(Swipe.FAST,GeneralLocation.CENTER_LEFT, GeneralLocation.CENTER, Press.FINGER));
        onView(withId(R.id.side_menu)).perform(new GeneralSwipeAction(Swipe.FAST,GeneralLocation.CENTER_LEFT, GeneralLocation.CENTER, Press.FINGER));

    }
}
