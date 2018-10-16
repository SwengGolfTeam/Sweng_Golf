package ch.epfl.sweng.swenggolf;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class UserTest {

    private final String username = "Bob", id = "1234", email = "Google", photo = "Picsou";


    @Before
    public void setUp(){
        Config.goToTest();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyusername(){
        new User("", id, email, photo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyId(){
        new User(username, "", email, photo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyLogin(){
        new User(username, id, "", photo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPhoto() {
        new User(username, id, email, null);
    }

    @Test
    public void testGettersFull(){
        User localUser = new User(username, id, email, photo);
        assertEquals("Usernames not equal", username, localUser.getUserName());
        assertEquals("userIds not equal", id, localUser.getUserId());
        assertEquals("Logins not equal", email, localUser.getEmail());
        assertEquals("Photo is not equal", photo, localUser.getPhoto());
    }


    @Test
    public void testMockFirebaseUser(){
        FirebaseUser fu = TestHelper.getFirebaseUser();
        User localUser = new User(fu);
        assertEquals("Usernames not equal", TestHelper.getName(), localUser.getUserName());
        assertEquals("userIds not equal", TestHelper.getUid(), localUser.getUserId());
        assertEquals("Logins not equal", TestHelper.getMail(), localUser.getEmail());
        assertNull("Photo is null",localUser.getPhoto());
    }

    @Test
    public void testSetters() {
        User user1 = new User(username, id, email, photo);
        User user2 = new User();
        user2.setEmail(email);
        user2.setUserName(username);
        user2.setPhoto(photo);
        user2.setUserId(id);
        assertEquals(user1.getEmail(), user2.getEmail());
        assertEquals(user1.getPhoto(), user2.getPhoto());
        assertEquals(user1.getUserId(), user2.getUserId());
        assertEquals(user1.getUserName(), user2.getUserName());
    }

    @Test
    public void testUserChanged(){
        User user1 = new User(username, id, email, photo);
        User user2 = User.userChanged(user1, "username", "email");
        assertFalse(user1.equals(user2));
        user1.setUserName("username");
        user1.setEmail("email");
        assertTrue(user1.equals(user2));
    }

    @Test
    public void testEquals(){
        User user1 = new User(username, id, email, photo);
        User user2 = new User(user1);
        assertTrue(user1.equals(user2));
        assertFalse(user1.equals(null));
        assertFalse(user1.equals("Hello"));
    }

    @Test
    public void sameAccount(){
        User user1 = new User(username, id, email, photo);
        User user2 = new User("hello", id, "taupe@poulpe.com", "photo");
        assertTrue(user1.sameAccount(user2));
    }

}
