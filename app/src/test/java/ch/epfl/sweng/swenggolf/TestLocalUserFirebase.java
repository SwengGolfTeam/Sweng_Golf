package ch.epfl.sweng.swenggolf;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestLocalUserFirebase {

    private FirebaseUser user;


    @Before
    public void setup() {
        user = TestHelper.getUser();
    }

    @Test
    public void testUser() {
        UserLocal u = new UserLocal(user);
        assertEquals(TestHelper.getMail(), u.getEmail());
        assertEquals(TestHelper.getName(), u.getUserName());
        assertEquals(TestHelper.getPhoto(), u.getPhoto());
        assertEquals(TestHelper.getUid(), u.getUserId());

    }
}