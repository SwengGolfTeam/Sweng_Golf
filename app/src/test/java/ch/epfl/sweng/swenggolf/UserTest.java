package ch.epfl.sweng.swenggolf;

import org.junit.Test;

import ch.epfl.sweng.swenggolf.database.User;

import static org.junit.Assert.assertEquals;

public class UserTest {

    private final String username = "Bob", id = "1234", mail = "Google";

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyusername(){
        new User("", id, mail);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyId(){
        new User(username, "", mail);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyLogin(){
        new User(username, id, "");
    }

    @Test
    public void testGetters(){
        User user = new User(username, id, mail);
        assertEquals("Usernames not equal", username, user.getUsername());
        assertEquals("userIds not equal", id, user.getUserId());
        assertEquals("Logins not equal", mail, user.getEmail());
    }
}
