package ch.epfl.sweng.swenggolf;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;

import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;

public class FakeDatabaseTest {

    private final String path = "/offers";
    private final String id = "id1";
    private final String id2 = "id2";
    private final String content = "This is a string";
    private final String content2 = "This is a second string";

    @Test
    public void writeAndReadReturnGoodValues() {
        Database d = new FakeDatabase(true);

        d.write( path, id, content);

        ValueListener<String> listener = new ValueListener<String>() {
            @Override
            public void onDataChange(String value) {
                assertThat(value, is(content));
            }

            @Override
            public void onCancelled(DbError error) {
                fail();
            }
        };
        d.read(path, id, listener, String.class);

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
        d.read(path, id, listener, String.class);
    }

    @Test
    public void writeAndReadListReturnGoodValues() {
        Database d = new FakeDatabase(true);
        d.write(path,id,content);
        d.write(path,id2,content2);

        ValueListener<List<String>> listener = new ValueListener<List<String>>() {
            @Override
            public void onDataChange(List<String> value) {

                assertThat(value.contains(content), is(true));
                assertThat(value.contains(content2), is(true));
            }

            @Override
            public void onCancelled(DbError error) {
                fail();
            }
        };
        d.readList(path,listener,String.class);
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
        d.readList(path,listener,String.class);
    }

    private void writeListenerError(boolean working, DbError error) {
        Database d = new FakeDatabase(working);
        CompletionListener listener = new CompletionListener() {
            @Override
            public void onComplete(DbError error) {
                assertThat(error, is(error));
            }
        };
        d.write(path, id, content, listener);
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
        d.read(path, id, listener, String.class);
    }

    @Test
    public void readListenerListHasError() {
        Database d = new FakeDatabase(false);

        ValueListener<List<String>> listener = new ValueListener<List<String>>() {
            @Override
            public void onDataChange(List<String> value) {

                fail();
            }

            @Override
            public void onCancelled(DbError error) {
                assertThat(error, is(DbError.UNKNOWN_ERROR));
            }
        };
        d.readList(path,listener,String.class);
    }



}
