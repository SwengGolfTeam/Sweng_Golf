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

    private static final Offer.Builder builder = new Offer.Builder();

    @Test
    public void setAndGetUUID() {
        builder.setUuid("uuid");
        assertThat(builder.getUuid() , is("uuid"));
    }

    @Test
    public void setAndGetLinkPicture() {
        builder.setLinkPicture("link");
        assertThat(builder.getLinkPicture(), is("link"));
    }

    @Test
    public void setAndGetCreationDate() {
        builder.setCreationDate(123);
        assertThat(builder.getCreationDate(), is((long) 123));
    }

    @Test
    public void setAndGetEndDate() {
        builder.setEndDate(123);
        assertThat(builder.getEndDate(), is((long) 123));
    }

    @Test
    public void setAndGetUserId() {
        builder.setUserId("user");
        assertThat(builder.getUserId(), is("user"));
    }

    @Test
    public void setAndGetLongitude() {
        builder.setLongitude(23.3);
        assertEquals(23.3, builder.getLongitude(), 0.05);
    }

    @Test
    public void setAndGetLatitude() {
        builder.setLatitude(23.3);
        assertEquals(23.3, builder.getLatitude(), 0.05);
    }

    @Test
    public void setAndGetTitle() {
        builder.setTitle("title");
        assertThat(builder.getTitle(), is("title"));
    }

    @Test
    public void setAndGetDescription() {
        builder.setDescription("description");
        assertThat(builder.getDescription(), is("description"));
    }

    @Test
    public void setAndGetTag() {
        builder.setTag(Category.TEST);
        assertThat(builder.getTag(), is(Category.TEST));
    }

    @Test
    public void setAndGetLocation() {
        Location loc = new Location("provider");
        loc.setLongitude(36.2);
        loc.setLatitude(23.4);
        assertEquals(loc.getLatitude(), builder.getLatitude(), 0.05);
        assertEquals(loc.getLongitude(), builder.getLongitude(), 0.05);
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
