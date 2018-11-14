package ch.epfl.sweng.swenggolf;

import android.location.Location;

import org.junit.Test;

import java.util.Calendar;

import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.Offer;

import static org.junit.Assert.assertEquals;


public class OfferTest {

    private final String id = "id_Patrick", title = "Echange un panda",
            description = "Echange un panda contre l'animal de votre choix";
    private static final String LUMIERE = "https://lumiere-a.akamaihd.net/v1/images/";


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

    @Test(expected = IllegalArgumentException.class)
    public void testDateSmaller() {
        new Offer("9", "bipbupbap",
                "titut bip bop tilit tut tut tat dut dut ! Mip zat zat !",
                LUMIERE
                        + "jawas_42e63e07.jpeg?region=866%2C10%2C1068%2C601&width=768",
                "09", Category.values()[3], 123123123, 123123122);
    }

    @Test
    public void testsameDate() {
        new Offer("9", "bipbupbap",
                "titut bip bop tilit tut tut tat dut dut ! Mip zat zat !",
                LUMIERE
                        + "jawas_42e63e07.jpeg?region=866%2C10%2C1068%2C601&width=768",
                "09", Category.values()[3], 123123123, 123123123);

        new Offer("9", "bipbupbap",
                "titut bip bop tilit tut tut tat dut dut ! Mip zat zat !",
                LUMIERE
                        + "jawas_42e63e07.jpeg?region=866%2C10%2C1068%2C601&width=768",
                "09", Category.values()[3], 123123123, 234234234);
    }

    @Test
    public void testUpdateLink() {
        Offer offer =         new Offer("9", "bipbupbap",
                "titut bip bop tilit tut tut tat dut dut ! Mip zat zat !",
                LUMIERE
                        + "jawas_42e63e07.jpeg?region=866%2C10%2C1068%2C601&width=768",
                "09", Category.values()[3], 123123123, 123123123);
        String newLink = "jawas_42e63e07.jpeg?region=123%2C10%2C1068%2C601&width=123";
        offer = offer.updateLinkToPicture(newLink);
        assertEquals(newLink, offer.getLinkPicture());
    }

    @Test
    public void testSetOffer() {
        Offer offer =         new Offer("9", "bipbupbap",
                "titut bip bop tilit tut tut tat dut dut ! Mip zat zat !",
                LUMIERE
                        + "jawas_42e63e07.jpeg?region=866%2C10%2C1068%2C601&width=768",
                "09", Category.values()[3], 123123123, 123123123);
        String newLink = "jawas_42e63e07.jpeg?region=123%2C10%2C1068%2C601&width=123";
        offer.setLocation(1,2);
        assertEquals(1, offer.getLatitude(),0.05);
        assertEquals(2, offer.getLongitude(), 0.05);
    }
}
