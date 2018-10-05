package ch.epfl.sweng.sweng_golf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OfferTest {

    private final String username = "Patrick", name = "Echange un panda",
            description = "Echange un panda contre l'animal de votre choix";

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyUsername(){
        Offer offer = new Offer("",name,description);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyName(){
        Offer offer = new Offer(username,"",description);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyDescription(){
        Offer offer = new Offer(username,name,"");

    }


    @Test
    public void testGetter(){
        Offer offer = new Offer(username, name, description);
        assertEquals("Usernames are not equals", username, offer.getUsername());
        assertEquals("Names are not equals", name, offer.getName());
        assertEquals("Descriptions are not equals", description, offer.getDescription());

    }
}
