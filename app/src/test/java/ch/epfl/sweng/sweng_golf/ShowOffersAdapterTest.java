package ch.epfl.sweng.sweng_golf;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShowOffersAdapterTest {

    private final List<Offer> offerList =
            Arrays.asList(new Offer("Jim", "Beer", "A lot of beer"));

    @Test(expected = NullPointerException.class)
    public void testEmptyList() {
        ShowOffersAdapter a = new ShowOffersAdapter(null);
    }


    @Test
    public void testSizeList() {
        ShowOffersAdapter a = new ShowOffersAdapter(offerList);
        assertEquals("Adapter doesn't show the right list size",
                offerList.size(), a.getItemCount());
    }
}
