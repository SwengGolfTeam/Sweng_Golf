package ch.epfl.sweng.swenggolf;

import org.junit.Test;

import ch.epfl.sweng.swenggolf.offer.Offer;

import static org.junit.Assert.assertEquals;

public class OfferTest {

    private final String author = "Patrick", id = "id_"+author, title = "Echange un panda",
            description = "Echange un panda contre l'animal de votre choix";

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyAuthor() {
        new Offer("",id, title, description);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyId() {
        new Offer(author, "", title, description);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTitle() {
        new Offer(author, id, "", description);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyDescription() {
        new Offer(author, id, title, "");
    }


    @Test
    public void testGetter() {
        Offer offer = new Offer(author, id, title, description);
        assertEquals("Authors are not equal", author, offer.getAuthor());
        assertEquals("Ids are not equal", id, offer.getUserId());
        assertEquals("Titles are not equal", title, offer.getTitle());
        assertEquals("Descriptions are not equal", description, offer.getDescription());
    }
}
