package ch.epfl.sweng.swenggolf;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConfigTest {

    @Test
    public void testModeTest() {
        Config.quitTest();
        assertFalse(Config.isTest());
        Config.goToTest();
        assertTrue(Config.isTest());
        Config.quitTest();
        assertFalse(Config.isTest());
    }

    @Test
    public void staticUserTest() {
        Config.goToTest();
        assertEquals(Config.getUser(), new User());
        User user1 = TestHelper.getUser();
        Config.setUser(new User(user1));
        assertEquals(user1, Config.getUser());

    }

    @Test
    public void staticActivityCallback() {
        Config.getActivityCallback();

        ActivityCallback activityCallback = new ActivityCallback() {
            @Override
            public void isDone() {
                assertTrue(true);
            }
        };
        Config.setActivityCallback(activityCallback);
        Config.getActivityCallback();
        Config.goToTest();
        Config.getActivityCallback().isDone();
        Config.resetActivityCallback();
        Config.getActivityCallback();
    }
}
