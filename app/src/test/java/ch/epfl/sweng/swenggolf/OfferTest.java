package ch.epfl.sweng.swenggolf;

import org.junit.Test;

import ch.epfl.sweng.swenggolf.offer.Offer;

import static org.junit.Assert.assertEquals;


public class OfferTest {

    private final String id = "id_Patrick", title = "Echange un panda",
            description = "Echange un panda contre l'animal de votre choix";

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyId() {
        new Offer("", title, description);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTitle() {
        new Offer(id, "", description);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyDescription() {
        new Offer(id, title, "");
    }


    @Test
    public void testGetter() {
        Offer offer = new Offer(id, title, description);
        assertEquals("Ids are not equal", id, offer.getUserId());
        assertEquals("Titles are not equal", title, offer.getTitle());
        assertEquals("Descriptions are not equal", description, offer.getDescription());
    }

    @Test
    public void testEmptyConstructor() {
        Offer offer = new Offer();
        assertEquals("Wrong uuid", "createdByEmptyConstructor", offer.getUuid());
    }
}
