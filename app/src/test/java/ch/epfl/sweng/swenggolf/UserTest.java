package ch.epfl.sweng.swenggolf;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {

    private final String username = "Bob", id = "1234", login = "Google";

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyusername(){
        new User("", id, login);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyId(){
        new User(username, "", login);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyLogin(){
        new User(username, id, "");
    }

    @Test
    public void testGetters(){
        User user = new User(username, id, login);
        assertEquals("Usernames not equal", username, user.getUsername());
        assertEquals("userIds not equal", id, user.getUserId());
        assertEquals("Logins not equal", login, user.getLogin());
    }
}
