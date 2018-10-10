package ch.epfl.sweng.swenggolf;

import org.junit.Test;

import ch.epfl.sweng.swenggolf.Offer.ListOfferTouchListener;

import static org.junit.Assert.assertEquals;

public class ListOfferTouchListenerTest {

    private final ListOfferTouchListener touchListener =
            new ListOfferTouchListener(null, null, null);

    @Test
    public void onTouchEventDoesNothing() {
        touchListener.onTouchEvent(null, null);
    }

    @Test
    public void onRequestDisallowInterceptTouchEventDoesNothing() {
        touchListener.onRequestDisallowInterceptTouchEvent(false);
        touchListener.onRequestDisallowInterceptTouchEvent(true);
    }
}
