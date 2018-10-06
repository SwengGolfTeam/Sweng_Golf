package ch.epfl.sweng.sweng_golf;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OfferTest {

    private final String author = "Patrick", title = "Echange un panda",
            description = "Echange un panda contre l'animal de votre choix";

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyAuthor() {
        new Offer("", title, description);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTitle() {
        new Offer(author, "", description);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyDescription() {
        new Offer(author, title, "");
    }


    @Test
    public void testGetter() {
        Offer offer = new Offer(author, title, description);
        assertEquals("Authors are not equal", author, offer.getAuthor());
        assertEquals("Titles are not equal", title, offer.getTitle());
        assertEquals("Descriptions are not equal", description, offer.getDescription());
    }
}
