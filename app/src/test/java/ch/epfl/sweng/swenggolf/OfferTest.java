package ch.epfl.sweng.swenggolf;

import org.junit.Test;

import java.util.logging.Logger;

import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.Offer;

import static ch.epfl.sweng.swenggolf.profile.PointType.ADD_LOCALISATION;
import static ch.epfl.sweng.swenggolf.profile.PointType.ADD_PICTURE;
import static ch.epfl.sweng.swenggolf.profile.PointType.POST_OFFER;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class OfferTest {

    private static final String LUMIERE = "https://lumiere-a.akamaihd.net/v1/images/";
    private static final String id = "id_Patrick", title = "Echange un panda",
            description = "Echange un panda contre l'animal de votre choix";

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyUserId() {
        (new Offer.Builder()).setTitle(title).setDescription(description).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTitle() {
        (new Offer.Builder()).setUserId(id).setDescription(description).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyDescription() {
        (new Offer.Builder()).setUserId(id).setTitle(title).build();
    }


    @Test
    public void testGetter() {
        Offer offer = (new Offer.Builder()).setUserId(id).setTitle(title)
                .setDescription(description).build();
        assertEquals("Ids are not equal", id, offer.getUserId());
        assertEquals("Titles are not equal", title, offer.getTitle());
        assertEquals("Descriptions are not equal", description, offer.getDescription());
    }

    @Test
    public void testEmptyConstructor() {
        Offer offer = new Offer();
        assertEquals("Wrong uuid", "", offer.getUuid());
    }

    private static Offer.Builder buildPartially() {
        return (new Offer.Builder()).setTitle(title).setDescription(description)
                .setUserId(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateSmaller() {
        buildPartially().setCreationDate(123123123).setEndDate(123123122).build();
    }

    @Test
    public void testsameDate() {
        buildPartially().setCreationDate(2).setEndDate(2).build();
    }

    @Test
    public void testUpdateLink() {
        Offer offer = buildPartially().setLinkPicture("oldLink").build();
        String newLink = "newLink";
        offer = offer.updateLinkToPicture(newLink);
        assertEquals(newLink, offer.getLinkPicture());
    }

    @Test
    public void testLocation() {
        Offer offer = buildPartially().setLongitude(2).setLatitude(1).build();
        assertEquals(1, offer.getLatitude(), 0.05);
        assertEquals(2, offer.getLongitude(), 0.05);
    }

    @Test
    public void testOfferValueWithNoExtras() {
        Offer offer = buildPartially().build();
        assertThat(offer.offerValue(), is(POST_OFFER.getValue()));
    }

    @Test
    public void testOfferValueWithLocation() {
        Offer offer = buildPartially().setLongitude(12).setLatitude(13).build();
        assertThat(offer.offerValue(),
                is(POST_OFFER.getValue() + ADD_LOCALISATION.getValue()));
    }

    @Test
    public void testOfferValueWithLinkPicture() {
        Offer offer = buildPartially().setLinkPicture("myPicture").build();
        assertThat(offer.offerValue(), is(POST_OFFER.getValue() + ADD_PICTURE.getValue()));
    }

    @Test
    public void testOfferValueWithAllExtras() {
        Offer offer = buildPartially().setLinkPicture("myPicture")
                .setLongitude(23).setLatitude(24).build();
        assertThat(offer.offerValue(), is(POST_OFFER.getValue() + ADD_PICTURE.getValue()
                + ADD_LOCALISATION.getValue()));
    }

    @Test
    public void testSameOfferHaveNoDiff() {
        Offer offer = buildPartially().build();
        assertThat(offer.offerValueDiff(offer), is(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffOnNullModification() {
        Offer o = new Offer();
        o.offerValueDiff(null);
    }

    @Test
    public void testDiffOnDifferentOffers() {
        Offer offer = buildPartially().build();
        Offer offer1 = buildPartially().setLatitude(123).setLongitude(123).build();
        assertThat(offer.offerValueDiff(offer1), is(ADD_LOCALISATION.getValue()));
    }
}
