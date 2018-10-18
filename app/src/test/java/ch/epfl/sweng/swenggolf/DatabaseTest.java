package ch.epfl.sweng.swenggolf;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.offer.Offer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class DatabaseTest {
    protected class TestDatabase extends Database {
        private final Map<String, Object> database;

        public TestDatabase(){
            database = new HashMap<>();
        }

        @Override
        public void write(@NonNull String path, @NonNull String id, @NonNull Object object) {
            database.put(path + "/" + id, object);
        }

        @Override
        public void write(@NonNull String path, @NonNull String id, @NonNull Object object, @NonNull CompletionListener listener) {
            write(path, id, object);
            listener.onComplete(DbError.NONE);
        }

        @Override
        public <T> void read(@NonNull String path, @NonNull String id, @NonNull ValueListener<T> listener, @NonNull Class<T> c) {
            String key = path + "/" + id;
            if (database.containsKey(key)) {
                listener.onDataChange((T) database.get(key));
            } else {
                listener.onDataChange(null);
            }
        }

        @Override
        public <T> void readList(@NonNull String path, @NonNull ValueListener<List<T>> listener, @NonNull Class<T> c) {
            List<T> list = new ArrayList<>();
            for (Map.Entry<String, Object> entry : database.entrySet()) {
                if (entry.getKey().startsWith(path)) {
                    list.add((T) entry.getValue());
                }
            }
            List<T> returnList = list.isEmpty() ? null : list;
            listener.onDataChange(returnList);
        }
    }

    private static final String PATH = "/offers";
    private static final String ID = "123456789";
    private static final String ID2 = "id_test";
    private static final String CONTENT = "Lore Ipsum bla bla bla";
    private static final String CONTENT_2 = "This is a long string. This is a long string. This is a long string."
            + "This is a long string. This is a long string. This is a long string. This is a long string."
            + "This is a long string. This is a long string. This is a long string. This is a long string.";


    @Test
    public void getInstanceAndSetDebugDatabaseNoError(){
        Database db = new TestDatabase();
        db.getInstance();
        db.setDebugDatabase(db);
    }

    @Test
    public void writeAndRead() {
        Database db = new TestDatabase();

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
    }

    @Test
    public void readList(){
        Database db = new TestDatabase();

        db.write(PATH, ID, CONTENT);
        db.write(PATH, ID2, CONTENT_2);
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
        db.readList(PATH, listener, String.class);
    }

    @Test
    public void writeWithListener(){
        Database db = new TestDatabase();

        CompletionListener listener = new CompletionListener() {
            @Override
            public void onComplete(DbError error) {
                assertThat(error, is(DbError.NONE));
            }
        };
        db.write(PATH, ID, CONTENT, listener);

    }

    @Test
    public void readOffers(){
        Database db = new TestDatabase();

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


}


