package ch.epfl.sweng.sweng_golf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OfferTest {
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParameters(){
        Offer offer = new Offer("","","");

    }

    private final String username = "Patrick", name = "Echange un panda",
            description = "Echange un panda contre l'animal de votre choix";
    @Test
    public void testGetter(){
        Offer offer = new Offer(username, name, description);
        assertEquals("Usernames are not equals", username, offer.getUsername());
        assertEquals("Names are not equals", name, offer.getName());
        assertEquals("Descriptions are not equals", description, offer.getDescription());

    }
}
