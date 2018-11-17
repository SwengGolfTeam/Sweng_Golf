package ch.epfl.sweng.swenggolf;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;

import ch.epfl.sweng.swenggolf.profile.PointType;
import ch.epfl.sweng.swenggolf.profile.User;

import static ch.epfl.sweng.swenggolf.profile.PointType.*;
import static ch.epfl.sweng.swenggolf.profile.PointType.CLOSE_OFFER;
import static ch.epfl.sweng.swenggolf.profile.PointType.POST_OFFER;
import static ch.epfl.sweng.swenggolf.profile.PointType.RESPOND_OFFER;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class UserTest {

    private static final String USERNAME = "Bob", ID = "1234",
            EMAIL = "Google", PHOTO = "Picsou",
            PREFERENCE = "Banana", DESCRIPTION = "Hello, I'm happy";

    private static final int POINTS = CLOSE_OFFER.getValue()
            + RESPOND_OFFER.getValue()
            + POST_OFFER.getValue();

    private User user1;

    @Before
    public void setUp() {
        Config.goToTest();
        user1 = new User(USERNAME, ID, EMAIL, PHOTO, PREFERENCE, DESCRIPTION, POINTS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyusername() {
        new User("", ID, EMAIL, PHOTO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyId() {
        new User(USERNAME, "", EMAIL, PHOTO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyLogin() {
        new User(USERNAME, ID, "", PHOTO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPhoto() {
        new User(USERNAME, ID, EMAIL, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPreference() {  new User(USERNAME, ID, EMAIL, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePoints() {
       new User(USERNAME, ID, EMAIL, PHOTO,
                PREFERENCE, DESCRIPTION, -POINTS);
    }

    @Test
    public void testGettersFull() {
        User localUser = new User(USERNAME, ID, EMAIL, PHOTO);
        assertEquals("Usernames not equal", USERNAME, localUser.getUserName());
        assertEquals("userIds not equal", ID, localUser.getUserId());
        assertEquals("Logins not equal", EMAIL, localUser.getEmail());
        assertEquals("Photo is not equal", PHOTO, localUser.getPhoto());
    }


    @Test
    public void testMockFirebaseUser() {
        FirebaseUser fu = TestHelper.getFirebaseUser();
        User localUser = new User(fu);
        assertEquals("Usernames not equal", TestHelper.getName(), localUser.getUserName());
        assertEquals("userIds not equal", TestHelper.getUid(), localUser.getUserId());
        assertEquals("Logins not equal", TestHelper.getMail(), localUser.getEmail());
        assertNull("Photo is null", localUser.getPhoto());
    }

    @Test
    public void testSetters() {
        User user2 = new User();
        user2.setEmail(EMAIL);
        user2.setUserName(USERNAME);
        user2.setPhoto(PHOTO);
        user2.setUserId(ID);
        user2.setPreference(PREFERENCE);
        user2.setDescription(DESCRIPTION);
        user2.addPoints(POST_OFFER.getValue() + RESPOND_OFFER.getValue() + CLOSE_OFFER.getValue());
        assertEquals("Failed to set preference", user1, user2);
    }

    @Test
    public void testUserChanged() {
        User user2 = User.userChanged(user1, "USERNAME", "EMAIL");
        assertFalse(user1.equals(user2));
        user1.setUserName("USERNAME");
        user1.setEmail("EMAIL");
        assertTrue(user1.equals(user2));
    }

    @Test
    public void testEquals() {
        User user2 = new User(user1);
        assertTrue(user1.equals(user2));
        assertFalse(user1.equals(null));
        assertFalse(user1.equals("Hello"));
    }

    @Test
    public void sameAccount() {
        User user2 = new User("hello", ID, "taupe@poulpe.com", "PHOTO");
        assertTrue(user1.sameAccount(user2));
    }

    @Test
    public void sameInformations() {
        User user2 = new User(USERNAME, "12345", EMAIL, PHOTO, PREFERENCE, DESCRIPTION, POINTS);
        assertTrue(user1.sameInformation(user2));
    }

    @Test
    public void defaultPreferenceWorks() {
        User user = new User(USERNAME, ID, EMAIL, PHOTO);
        assertEquals("Default preference is not empty", "", user.getPreference());
    }

    @Test
    public void initializingUserWithPreferenceWorks() {
        User user = new User(USERNAME, ID, EMAIL, PHOTO, PREFERENCE);
        assertEquals("Failed to initialize user preference", PREFERENCE, user.getPreference());
    }

    @Test
    public void getDescription() {
        User user = new User(USERNAME, ID, EMAIL, PHOTO, PREFERENCE);
        assertEquals("Default description", User.DEFAULT_DESCRIPTION, user.getDescription());
        user.setDescription(DESCRIPTION);
        assertEquals(DESCRIPTION, user.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionWithIllegalUsername() {
        new User("This is a very long username that should not be valid", ID, EMAIL, PHOTO);
    }

}
