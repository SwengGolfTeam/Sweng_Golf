package ch.epfl.sweng.swenggolf;

import android.support.annotation.NonNull;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.swenggolf.database.AttributeFilter;
import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.Offer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class FakeDatabaseTest {

    private static final String PATH = Database.OFFERS_PATH;
    private static final String ID = "id1";
    private static final String ID2 = "ID2";
    private static final String CONTENT = "This is a string";
    private static final String CONTENT_2 = "This is a second string";

    @Test
    public void writeAndReadReturnGoodValues() {
        Database d = new FakeDatabase(true);

        d.write(PATH, ID, CONTENT);

        ValueListener<String> listener = new ValueListener<String>() {
            @Override
            public void onDataChange(String value) {
                assertThat(value, is(CONTENT));
            }

            @Override
            public void onCancelled(DbError error) {
                fail();
            }
        };
        d.read(PATH, ID, listener, String.class);

    }

    @Test
    public void readReturnNull() {
        Database d = new FakeDatabase(true);
        ValueListener<String> listener = new ValueListener<String>() {
            @Override
            public void onDataChange(String value) {
                assertNull(value);
            }

            @Override
            public void onCancelled(DbError error) {
                fail();
            }
        };
        d.read(PATH, ID, listener, String.class);
    }

    @Test
    public void writeAndReadListReturnGoodValues() {
        Database d = new FakeDatabase(true);
        d.write(PATH, ID, CONTENT);
        d.write(PATH, ID2, CONTENT_2);

        ValueListener<List<String>> listener = new ValueListener<List<String>>() {
            @Override
            public void onDataChange(List<String> value) {

                assertThat(value.contains(CONTENT), is(true));
                assertThat(value.contains(CONTENT_2), is(true));
            }

            @Override
            public void onCancelled(DbError error) {
                fail();
            }
        };
        d.readList(PATH, listener, String.class);
    }

    @Test
    public void readListReturnNull() {
        Database d = new FakeDatabase(true);

        ValueListener<List<String>> listener = new ValueListener<List<String>>() {
            @Override
            public void onDataChange(List<String> value) {
                assertNull(value);
            }

            @Override
            public void onCancelled(DbError error) {
                fail();
            }
        };
        d.readList(PATH, listener, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void readListThrowExceptionOnInvalidAttribute() {
        FakeDatabase database = new FakeDatabase(true);
        database.readList(PATH, null, Offer.class,
                new AttributeFilter("invalid attribute", "random"));
    }

    private void writeListenerError(boolean working, DbError error) {
        Database d = new FakeDatabase(working);
        CompletionListener listener = new CompletionListener() {
            @Override
            public void onComplete(DbError error) {
                assertThat(error, is(error));
            }
        };
        d.write(PATH, ID, CONTENT, listener);
    }

    @Test
    public void writeListenerReturnNone() {
        writeListenerError(true, DbError.NONE);
    }


    @Test
    public void writeListenerHasError() {
        writeListenerError(false, DbError.UNKNOWN_ERROR);
    }

    @Test
    public void readListenerHasError() {
        Database d = new FakeDatabase(false);

        ValueListener<String> listener = new ValueListener<String>() {
            @Override
            public void onDataChange(String value) {
                fail();

            }

            @Override
            public void onCancelled(DbError error) {
                assertThat(error, is(DbError.UNKNOWN_ERROR));
            }
        };
        d.read(PATH, ID, listener, String.class);
    }

    @Test
    public void readListenerListHasError() {
        Database d = new FakeDatabase(false);

        ValueListener<List<Offer>> listener = getListValueListener();
        d.readList(PATH, listener, Offer.class);
    }

    @Test
    public void readListenerListWithAttributeHasError() {
        Database d = new FakeDatabase(false);

        ValueListener<List<Offer>> listener = getListValueListener();
        d.readList(PATH, listener, Offer.class,new AttributeFilter("s1", "s2"));
    }

    @Test
    public void readOffersOfUserHasError() {
        Database d = new FakeDatabase(false);

        ValueListener<List<Offer>> listener = getListValueListener();
        d.readOffers(listener, new ArrayList<Category>(), "user");
    }

    @NonNull
    private ValueListener<List<Offer>> getListValueListener() {
        return new ValueListener<List<Offer>>() {
                @Override
                public void onDataChange(List<Offer> value) {

                    fail();
                }

                @Override
                public void onCancelled(DbError error) {
                    assertThat(error, is(DbError.UNKNOWN_ERROR));
                }
            };
    }


}
