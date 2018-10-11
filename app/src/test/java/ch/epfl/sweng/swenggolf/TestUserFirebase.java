package ch.epfl.sweng.swenggolf;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import ch.epfl.sweng.swenggolf.database.User;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestUserFirebase {

    private FirebaseUser user;

    @Before
    public void setup(){
        user = TestHelper.getUser();
    }

    @Test
    public void testUser() {
        User u = new User(user);
        assertEquals(TestHelper.getMail() , u.getEmail());
        assertEquals(TestHelper.getName() , u.getUsername());
        assertEquals(TestHelper.getPhoto() , u.getPhoto());
        assertEquals(TestHelper.getUid() , u.getUserId());

    }
}