package ch.epfl.sweng.swenggolf;

import android.support.test.rule.ActivityTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
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

    /**
     * Posts an answer on the current offer.
     *
     * @param answer the message to be posted
     */
    public static void addAnswer(String answer) {
        showOfferCustomScrollTo();
        onView(withId(R.id.react_button)).perform(click());
        onView(withId(R.id.your_answer_description))
                .perform(typeText(answer), closeSoftKeyboard());
        onView(withId(R.id.post_button)).perform(click());

    }

    /**
     * Performs something equivalent to a scroll in ShowOfferActivity,
     * since scrollTo is not permitted with a NestedScrollView.
     */
    public static void showOfferCustomScrollTo() {
        onView(withId(R.id.show_offer_description)).perform(swipeUp());
        onView(withId(R.id.show_offer_description)).perform(swipeUp());
    }
}
