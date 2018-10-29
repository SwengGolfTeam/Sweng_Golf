package ch.epfl.sweng.swenggolf;

import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;

import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.ShowOfferActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

public final class TestUtility {

    /**
     * Use this test to check if we can see a Toast.
     * @param mActivityRule the tested activity
     * @param toastMessage the message of the Toast
     */
    public static void testToastShow(ActivityTestRule mActivityRule, int toastMessage) {
        onView(withText(toastMessage)).inRoot(
                withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    public static Fragment offerFragment(Fragment fragment, Offer offer) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("offer", offer);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment userFragment(Fragment fragment, User user) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("ch.epfl.sweng.swenggolf.user", user);
        fragment.setArguments(bundle);
        return fragment;
    }
}
