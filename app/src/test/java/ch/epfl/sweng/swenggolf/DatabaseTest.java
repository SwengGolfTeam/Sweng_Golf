package ch.epfl.sweng.swenggolf;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static final String PATH = "/offers";
    private static final String ID = "123456789";
    private static final String ID2 = "id_test";
    private static final String CONTENT = "Lore Ipsum bla bla bla";
    private static final String CONTENT_2 = "This is a long string. This is a long string. This is a long string."
            + "This is a long string. This is a long string. This is a long string. This is a long string."
            + "This is a long string. This is a long string. This is a long string. This is a long string.";


    @Test
    public void getInstanceAndSetDebugDatabaseNoException(){
        Database db = new FakeDatabase(true);

        db.setDebugDatabase(db);
        db.getInstance();
    }

    @Test
    public void readOffersReturnsCorrectValues(){
        Database db = new FakeDatabase(true);

        db.write(PATH, ID, CONTENT);
        db.write(PATH, ID2, CONTENT_2);

        ValueListener<List<Offer>> listener = new ValueListener<List<Offer>>() {
            @Override
            public void onDataChange(List<Offer> offers) {

                assertThat(offers.contains(CONTENT), is(true));
                assertThat(offers.contains(CONTENT_2), is(true));
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
                assertNull(value);
            }

            @Override
            public void onCancelled(DbError error) {
                fail();
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
                assertThat(error, is(not(DbError.NONE.NONE)));
            }
        };
        database.remove(PATH, ID, listener);
    }


}


