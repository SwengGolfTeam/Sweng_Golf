package ch.epfl.sweng.swenggolf;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConfigTest {

    @Test
    public void testModeTest(){
        Config.quitTest();
        assertFalse(Config.isTest());
        Config.goToTest();
        assertTrue(Config.isTest());
        Config.quitTest();
        assertFalse(Config.isTest());
    }

    @Test
    public void staticUserTest(){
        User user1 = TestHelper.getUser();
        Config.setUser(new User(user1));
        assertTrue(user1.equals(Config.getUser()));

    }

}
