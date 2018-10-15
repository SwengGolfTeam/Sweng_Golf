package ch.epfl.sweng.swenggolf;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseError;
import ch.epfl.sweng.swenggolf.database.FakeDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;

public class FakeDatabaseTest {

    @Test
    public void writeAndReadReturnGoodValues(){
        Database d = new FakeDatabase(true);
        final String path = "/offers";
        final String id = "id1";
        final String content = "This is a string";
        d.write( path, id, content);

        ValueListener<String> listener = new ValueListener<String>() {
            @Override
            public void onDataChange(String value) {
                assertThat(value, is(content));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                fail();
            }
        };
        d.read(path, id, listener, String.class);

    }
}
