package ch.epfl.sweng.swenggolf;

import org.junit.Test;

import ch.epfl.sweng.swenggolf.profile.Badge;

import static org.junit.Assert.assertEquals;

public class BadgeTest {

    /**
     * Helper function to get the amount of points corresponding to a level.
     * Must be the inverse function of computeLevel() in the Badge.java file
     *
     * @param level the level for which we want to get the points
     * @return the amount of points corresponding to the level
     */
    private int levelToPoints(int level) {
        return level * 7 - 1;
    }

    @Test
    public void correctBadgeForPointsTest() {
        assertEquals(R.drawable.badge00, Badge.getDrawable(0));
        assertEquals(R.drawable.badge01, Badge.getDrawable(levelToPoints(1)));
        assertEquals(R.drawable.badge02, Badge.getDrawable(levelToPoints(2)));
        assertEquals(R.drawable.badge03, Badge.getDrawable(levelToPoints(3)));
        assertEquals(R.drawable.badge04, Badge.getDrawable(levelToPoints(4)));
        assertEquals(R.drawable.badge05, Badge.getDrawable(levelToPoints(5)));
        assertEquals(R.drawable.badge06, Badge.getDrawable(levelToPoints(6)));
        assertEquals(R.drawable.badge07, Badge.getDrawable(levelToPoints(7)));
        assertEquals(R.drawable.badge08, Badge.getDrawable(levelToPoints(8)));
        assertEquals(R.drawable.badge09, Badge.getDrawable(levelToPoints(9)));
        assertEquals(R.drawable.badge10, Badge.getDrawable(levelToPoints(10)));
        assertEquals(R.drawable.badge11, Badge.getDrawable(levelToPoints(11)));
        assertEquals(R.drawable.badge12, Badge.getDrawable(levelToPoints(12)));
        assertEquals(R.drawable.badge13, Badge.getDrawable(levelToPoints(13)));
        assertEquals(R.drawable.badge14, Badge.getDrawable(levelToPoints(14)));
        assertEquals(R.drawable.badge15, Badge.getDrawable(levelToPoints(15)));
    }

    @Test
    public void correctColorForPointsTest() {
        assertEquals(Badge.BRONZE, Badge.getColor(levelToPoints(3)));
        assertEquals(Badge.SILVER, Badge.getColor(levelToPoints(8)));
        assertEquals(Badge.GOLD, Badge.getColor(levelToPoints(13)));
        assertEquals(Badge.WHITE, Badge.getColor(0));
    }
}
