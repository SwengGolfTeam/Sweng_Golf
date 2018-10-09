package ch.epfl.sweng.swenggolf;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import com.google.firebase.auth.FirebaseUser;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private FirebaseUser user;

    @Before
    public void setup(){
        user = TestHelper.getUser();
    }

    @Test
    public void testUser() {
        User u = new User(user);
        assertEquals(TestHelper.getMail() , u.getMail());
        assertEquals(TestHelper.getName() , u.getName());
        assertEquals(TestHelper.getPhoto() , u.getPhoto());
        assertEquals(TestHelper.getUid() , u.getUid());

    }
}