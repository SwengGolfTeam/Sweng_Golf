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

    private static final int PERMISSIONS_DIALOG_DELAY = 3000;
    private static final int GRANT_BUTTON_INDEX = 1;

    /**
     * Checks the OS permission box if needed.
     *
     * @throws InterruptedException in case of problem with sleep
     */
    public static void allowPermissionsIfNeeded() throws InterruptedException {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && !hasNeededPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                sleep(PERMISSIONS_DIALOG_DELAY);
                UiDevice device = UiDevice.getInstance(getInstrumentation());
                UiObject allowPermissions = device.findObject(new UiSelector()
                        .clickable(true)
                        .checkable(false)
                        .index(GRANT_BUTTON_INDEX));
                if (allowPermissions.exists()) {
                    allowPermissions.click();
                }
            }
        } catch (UiObjectNotFoundException e) {
            System.out.println("There is no permissions dialog to interact with");
        }
    }

    private static boolean hasNeededPermission(String permissionNeeded) {
        Context context = InstrumentationRegistry.getTargetContext();
        int permissionStatus = ContextCompat.checkSelfPermission(context, permissionNeeded);
        return permissionStatus == PackageManager.PERMISSION_GRANTED;
    }

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
