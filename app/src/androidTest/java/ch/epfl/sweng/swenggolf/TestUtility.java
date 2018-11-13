package ch.epfl.sweng.swenggolf;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.v4.content.ContextCompat;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

public final class TestUtility {

    /**
     * Use this test to check if we can see a Toast.
     *
     * @param mActivityRule the tested activity
     * @param toastMessage  the message of the Toast
     */
    public static void testToastShow(ActivityTestRule mActivityRule, int toastMessage) {
        onView(withText(toastMessage)).inRoot(
                withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    /**
     * Use this test to check if we can see a Toast.
     *
     * @param mActivityRule the tested activity
     * @param toastMessage  the message of the Toast
     */
    public static void testToastShow(ActivityTestRule mActivityRule, String toastMessage) {
        onView(withText(toastMessage)).inRoot(
                withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

}
