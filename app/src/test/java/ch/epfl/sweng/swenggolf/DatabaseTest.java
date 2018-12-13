package ch.epfl.sweng.swenggolf;

import org.junit.Test;

import java.util.List;

import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.offer.Offer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class DatabaseTest {

    private static final String PATH = Database.OFFERS_PATH;
    private static final String ID = "123456789";
    private static final String ID2 = "id_test";
    private static final String CONTENT = "Lore Ipsum bla bla bla";
    private static final String CONTENT_2 = "This is a long string. This is a long string. This is a long string."
            + "This is a long string. This is a long string. This is a long string. This is a long string."
            + "This is a long string. This is a long string. This is a long string. This is a long string.";
    private static final Offer OFFER_1 = (new Offer.Builder()).setUserId("user").setTitle("offer1").setDescription(CONTENT).setUuid(ID).build();
    private static final Offer OFFER_2 = (new Offer.Builder(OFFER_1)).build();


    @Test
    public void getInstanceAndSetDebugDatabaseNoException() {
        Database db = new FakeDatabase(true);

        Database.setDebugDatabase(db);
        Database.getInstance();
    }

    @Test
    public void readOffersReturnsCorrectValues() {
        Database db = new FakeDatabase(true);

        db.write(PATH, ID, OFFER_1);
        db.write(PATH, ID2, OFFER_2);

        ValueListener<List<Offer>> listener = new ValueListener<List<Offer>>() {
            @Override
            public void onDataChange(List<Offer> offers) {

                assertThat(offers.contains(OFFER_1), is(true));
                assertThat(offers.contains(OFFER_2), is(true));
            }

            @Override
            public void onCancelled(DbError error) {
                fail();
            }
        };
        db.readOffers(listener);
    }

    @Test
    public void removeRemoveElements() {
        Database db = new FakeDatabase(true);
        db.write(PATH, ID, CONTENT);
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
        db.read(PATH, ID, listener, String.class);

        CompletionListener completionListener = new CompletionListener() {
            @Override
            public void onComplete(DbError error) {
                assertThat(error, is(DbError.NONE));
            }
        };

        db.remove(PATH, ID, completionListener);

        ValueListener<String> valueListener = new ValueListener<String>() {
            @Override
            public void onDataChange(String value) {
                fail();
            }

            @Override
            public void onCancelled(DbError error) {
                if (error == DbError.DATA_DOES_NOT_EXIST){
                    assert(true);
                } else {
                    fail();
                }

            }
        };
        db.read(PATH, ID, valueListener, String.class);
    }

    @Test
    public void removeGetError() {
        Database database = new FakeDatabase(false);
        CompletionListener listener = new CompletionListener() {
            @Override
            public void onComplete(DbError error) {
                assertThat(error, is(not(DbError.NONE)));
            }
        };
        database.remove(PATH, ID, listener);
    }


}


