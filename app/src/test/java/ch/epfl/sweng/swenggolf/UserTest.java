package ch.epfl.sweng.swenggolf;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UserTest {

    private final String username = "Bob", id = "1234", mail = "Google", photo = "Picsou";


    @Before
    public void setUp(){
        Config.goToTest();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyusername(){
        new UserLocal("", id, mail, photo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyId(){
        new UserLocal(username, "", mail, photo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyLogin(){
        new UserLocal(username, id, "", photo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPhoto() {
        new UserLocal(username, id, mail, null);
    }

    @Test
    public void testGettersFull(){
        UserLocal localUser = new UserLocal(username, id, mail, photo);
        assertEquals("Usernames not equal", username, localUser.getUserName());
        assertEquals("userIds not equal", id, localUser.getUserId());
        assertEquals("Logins not equal", mail, localUser.getEmail());
        assertEquals("Photo is not equal", photo, localUser.getPhoto());
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
