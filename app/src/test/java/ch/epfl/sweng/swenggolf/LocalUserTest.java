package ch.epfl.sweng.swenggolf;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LocalUserTest {

    private final String username = "Bob", id = "1234", mail = "Google";

    @BeforeClass
    public static void setUp(){
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
    public void testGetters(){
        UserLocal localUser = new UserLocal(username, id, mail);
        assertEquals("Usernames not equal", username, localUser.getUserName());
        assertEquals("userIds not equal", id, localUser.getUserId());
        assertEquals("Logins not equal", mail, localUser.getEmail());
    }
}
