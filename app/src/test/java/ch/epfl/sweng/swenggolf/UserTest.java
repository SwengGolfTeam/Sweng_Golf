package ch.epfl.sweng.swenggolf;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UserTest {

    private final String username = "Bob", id = "1234", mail = "Google";

    @Before
    public void setUp(){
        TestMode.goToTest();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyusername(){
        new UserLocal("", id, mail);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyId(){
        new UserLocal(username, "", mail);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyLogin(){
        new UserLocal(username, id, "");
    }

    @Test
    public void testGettersFull(){
        UserLocal localUser = new UserLocal(username, id, mail, null);
        assertEquals("Usernames not equal", username, localUser.getUserName());
        assertEquals("userIds not equal", id, localUser.getUserId());
        assertEquals("Logins not equal", mail, localUser.getEmail());
        assertNull("Photo is null",localUser.getPhoto());
    }

    @Test
    public void testGetters(){
        UserLocal localUser = new UserLocal(username, id, mail);
        assertEquals("Usernames not equal", username, localUser.getUserName());
        assertEquals("userIds not equal", id, localUser.getUserId());
        assertEquals("Logins not equal", mail, localUser.getEmail());
        assertNull("Photo is null",localUser.getPhoto());
    }

    @Test
    public void testFirebaseUser(){
        FirebaseUser fu = TestHelper.getUser();
        User localUser = new UserFirebase(fu);
        assertEquals("Usernames not equal", TestHelper.getName(), localUser.getUserName());
        assertEquals("userIds not equal", TestHelper.getUid(), localUser.getUserId());
        assertEquals("Logins not equal", TestHelper.getMail(), localUser.getEmail());
        assertNull("Photo is null",localUser.getPhoto());
    }

    @Test
    public void testMockFirebaseUser(){
        FirebaseUser fu = TestHelper.getUser();
        User localUser = new UserLocal(fu);
        assertEquals("Usernames not equal", TestHelper.getName(), localUser.getUserName());
        assertEquals("userIds not equal", TestHelper.getUid(), localUser.getUserId());
        assertEquals("Logins not equal", TestHelper.getMail(), localUser.getEmail());
        assertNull("Photo is null",localUser.getPhoto());
    }

}
