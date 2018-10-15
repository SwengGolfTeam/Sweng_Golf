package ch.epfl.sweng.swenggolf;

import android.net.Uri;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LocalUserTest {

    private static final String username = "Bob", id = "1234", mail = "Google", preference = "Bananas";
    private static final Uri photo = Uri.EMPTY;

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

    public void testUserFields(UserLocal user, String username, String id, String mail, Uri photo, String preference){
        assertEquals("Usernames not equal", username, user.getUserName());
        assertEquals("userIds not equal", id, user.getUserId());
        assertEquals("Logins not equal", mail, user.getEmail());
        assertEquals("Photo not equal",photo,user.getPhoto());
        assertEquals("Preference not equal",user.getPreference(),preference);
    }

    @Test
    public void testGettersFull(){
        UserLocal localUser = new UserLocal(username, id, mail, preference);
        testUserFields(localUser,username,id,mail,Uri.EMPTY,preference);
    }

    @Test
    public void testGetters(){
        UserLocal localUser = new UserLocal(username, id, mail);
        testUserFields(localUser,username,id,mail,null,"");
    }

    @Test
    public void testEmptyUserIsEmpty(){
        UserLocal localUser = new UserLocal();
        testUserFields(localUser,"","","",null,"");
    }

    @Test
    public void testUriConstructorFunctionnality(){
        UserLocal localUser = new UserLocal(username,id,mail,photo,preference);
        testUserFields(localUser,username,id,mail,photo,preference);
    }
}
