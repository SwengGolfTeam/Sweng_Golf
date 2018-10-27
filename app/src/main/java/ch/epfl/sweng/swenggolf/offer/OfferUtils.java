package ch.epfl.sweng.swenggolf.offer;

import android.os.Bundle;

import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

public abstract class OfferUtils {

    /**
     * Creates a ShowOffer fragment with an argument.
     * Argument name is "offer".
     *
     * @param offer the offer to set as an argument
     * @return the fragment to show the argument offer
     */
    public static ShowOfferActivity createShowOfferWithOffer(Offer offer) {
        ShowOfferActivity showOff = new ShowOfferActivity();
        Bundle offerBundle = new Bundle();
        offerBundle.putParcelable("offer", offer);
        showOff.setArguments(offerBundle);
        return showOff;
    }
}
