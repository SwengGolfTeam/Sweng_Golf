package ch.epfl.sweng.sweng_golf;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ListOfferAdapterTest {

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    private final List<Offer> offerList =
            Arrays.asList(new Offer("Jim", "Beer", "A lot of beer"));

    @Test(expected = NullPointerException.class)
    public void testEmptyList() {
        new ListOfferAdapter(null);
    }


    @Test
    public void testSizeList() {
        ListOfferAdapter a = new ListOfferAdapter(offerList);
        assertEquals("Adapter doesn't show the right list size",
                offerList.size(), a.getItemCount());
    }
}
