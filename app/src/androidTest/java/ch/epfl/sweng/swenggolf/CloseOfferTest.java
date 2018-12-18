package ch.epfl.sweng.swenggolf;

import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

public class CloseOfferTest extends ShowOfferFragmentTest {

    @Test
    public void closeButtonNotVisibileWhenClosing() {
        final ViewInteraction button = closeOffer();
        button.check(matches(not(isDisplayed())));
    }

    @NonNull
    private ViewInteraction closeOffer() {
        final ViewInteraction button = onView(withId(R.id.close_offer_button));
        TestUtility.showOfferCustomScrollTo();
        button.perform(click());
        return button;
    }

    @Test
    public void closeButtonVisibleWhenOfferIsOpen() {
        TestUtility.showOfferCustomScrollTo();
        onView(withId(R.id.close_offer_button)).check(matches(isDisplayed()));
    }

    @Test
    public void canNotAddAnswersWhenClosed() {
        closeOffer();
        onView(withId(R.id.react_button)).check(matches(not(isDisplayed())));
    }
}
