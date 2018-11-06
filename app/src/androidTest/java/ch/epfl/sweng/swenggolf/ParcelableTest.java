package ch.epfl.sweng.swenggolf;

import android.os.Parcel;
import android.os.Parcelable;

import org.junit.Test;

import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.Offer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ParcelableTest {
    private static final Offer parceledOffer = new Offer("a","b","c","d","e", Category.TEST);
    private static final User parceledUser = new User("a","b","c","d","e","f");

    private static Parcel writeToParcel(Parcelable parcelable) {
        Parcel parcel = Parcel.obtain();
        parcelable.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        return parcel;
    }

    private static <T extends Parcelable> void parcelCreator(Parcelable.Creator<T> creator, Parcelable parcelable) {
        assertEquals(parcelable ,creator.createFromParcel(writeToParcel(parcelable)));
    }

    @Test
    public void parcelConstructor() {
        User u = new User(writeToParcel(parceledUser));
        assertEquals(parceledUser, u);
    }

    @Test
    public void parcelOfferCreator() {
        parcelCreator(Offer.CREATOR, parceledOffer);
    }

    @Test
    public void parcelUserCreator() {
        parcelCreator(User.CREATOR, parceledUser);
    }

    @Test
    public void testDescribeContentsUser() {
        User u = new User();
        assertEquals(0, u.describeContents());
    }

    @Test
    public void testDescribeContentsOffer() {
        Offer o = new Offer();
        assertEquals(0, o.describeContents());
    }

    @Test
    public void newArrayHasGoodSizeOffer() {
        newArrayHasGoodSize(Offer.CREATOR);
    }

    @Test
    public void newArrayHasGoodSizeUser() {
        newArrayHasGoodSize(User.CREATOR);
    }

    private static <T extends Parcelable> void newArrayHasGoodSize(Parcelable.Creator<T> creator) {
        int size = 10;
        assertThat(creator.newArray(size).length, is(size));
    }
}
