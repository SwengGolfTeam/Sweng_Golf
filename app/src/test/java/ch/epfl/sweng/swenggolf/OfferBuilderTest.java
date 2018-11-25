package ch.epfl.sweng.swenggolf;

import android.location.Location;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.Offer;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class OfferBuilderTest {

    private static final Offer.Builder builder = new Offer.Builder().setDescription("test")
            .setTitle("test").setUserId("test").setLinkPicture("test");

    @Test
    public void setAndGetUUID() {
        Offer offer = builder.setUuid("uuid").build();
        assertThat(offer.getUuid() , is("uuid"));
    }

    @Test
    public void setAndGetLinkPicture() {
        Offer offer = builder.setLinkPicture("link").build();
        assertThat(offer.getLinkPicture(), is("link"));
    }

    @Test
    public void setAndGetCreationDate() {
        Offer offer = builder.setCreationDate(123).build();
        assertThat(offer.getCreationDate(), is((long) 123));
    }

    @Test
    public void setAndGetEndDate() {
        Offer offer = builder.setEndDate(123).build();
        assertThat(offer.getEndDate(), is((long) 123));
    }

    @Test
    public void setAndGetUserId() {
        Offer offer = builder.setUserId("user").build();
        assertThat(offer.getUserId(), is("user"));
    }

    @Test
    public void setAndGetLongitude() {
        Offer offer = builder.setLongitude(23.3).build();
        assertEquals(23.3, offer.getLongitude(), 0.05);
    }

    @Test
    public void setAndGetLatitude() {
        Offer offer = builder.setLatitude(23.3).build();
        assertEquals(23.3, offer.getLatitude(), 0.05);
    }

    @Test
    public void setAndGetTitle() {
        Offer offer = builder.setTitle("title").build();
        assertThat(offer.getTitle(), is("title"));
    }

    @Test
    public void setAndGetDescription() {
        Offer offer = builder.setDescription("description").build();
        assertThat(offer.getDescription(), is("description"));
    }

    @Test
    public void setAndGetTag() {
        Offer offer = builder.setTag(Category.TEST).build();
        assertThat(offer.getTag(), is(Category.TEST));
    }

    @Test
    public void setAndGetLocation() {
        Location loc = new Location("provider");
        loc.setLongitude(36.2);
        loc.setLatitude(23.4);
        Offer offer = builder.build();
        assertEquals(loc.getLatitude(), offer.getLatitude(), 0.05);
        assertEquals(loc.getLongitude(), offer.getLongitude(), 0.05);
    }

    @Test
    public void buildCorrectOffer() {
        Offer builded = new Offer.Builder().setLatitude(23.3).setLongitude(23.4).setTitle("title")
                .setCreationDate(23).setEndDate(26).setUuid("uuid").setUserId("userid")
                .setDescription("description").setTag(Category.TEST).setLinkPicture("link").build();
        assertThat(builded.getLatitude(), is(23.3));
        assertThat(builded.getLongitude(), is(23.4));
        assertThat(builded.getTitle(), is("title"));
        assertThat(builded.getCreationDate(), is((long) 23));
        assertThat(builded.getEndDate(), is((long) 26));
        assertThat(builded.getUuid(), is("uuid"));
        assertThat(builded.getUserId(), is("userid"));
        assertThat(builded.getTag(), is(Category.TEST));
        assertThat(builded.getLinkPicture(), is("link"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildFailsOnNullString() {
        new Offer.Builder().setLinkPicture(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildFailsOnEmptyString() {
        new Offer.Builder().build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildFailsOnDateFormatError() {
        new Offer.Builder().setCreationDate(23).setEndDate(22).build();
    }
}
