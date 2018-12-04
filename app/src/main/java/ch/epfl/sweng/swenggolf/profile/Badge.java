package ch.epfl.sweng.swenggolf.profile;

import ch.epfl.sweng.swenggolf.R;

/**
 * Class which represents a User Badge.
 */
public final class Badge {
    public static final int MAX_LEVEL = 15;
    public static final int LEVEL_SPACE = 20;

    // Color format: #AARRGGBB or #RRGGBB
    public static final String GOLD = "#40ffdf00";
    public static final String SILVER = "#40c0c0c0";
    public static final String BRONZE = "#40cd7f32";
    public static final String WHITE = "#ffffff";

    /**
     * Private constructor to hide the implicit public one.
     */
    private Badge() {
    }

    /**
     * Returns the Drawable element of the badge corresponding to the points of a user.
     *
     * @param points The amount of points of a user
     * @return The Drawable of the badge
     */
    public static int getDrawable(int points) {
        int level = computeLevel(points);
        switch (level) {
            case 1:
                return R.drawable.badge01;
            case 2:
                return R.drawable.badge02;
            case 3:
                return R.drawable.badge03;
            case 4:
                return R.drawable.badge04;
            case 5:
                return R.drawable.badge05;
            case 6:
                return R.drawable.badge06;
            case 7:
                return R.drawable.badge07;
            case 8:
                return R.drawable.badge08;
            case 9:
                return R.drawable.badge09;
            case 10:
                return R.drawable.badge10;
            case 11:
                return R.drawable.badge11;
            case 12:
                return R.drawable.badge12;
            case 13:
                return R.drawable.badge13;
            case 14:
                return R.drawable.badge14;
            case 15:
                return R.drawable.badge15;
            default:
                return R.drawable.badge00;
        }

    }

    /**
     * Computes the color of the background of the comments of the user (under the Offers).
     *
     * @param points the amount of points of the user
     * @return the color corresponding to its badge
     */
    public static String getColor(int points) {
        int level = computeLevel(points);
        switch (level) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return BRONZE;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                return SILVER;
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                return GOLD;
            default:
                return WHITE;
        }
    }

    /**
     * Computes the Level corresponding to the amount of points.
     *
     * @param points the amount of points of the user
     * @return the level of the corresponding Badge
     */
    public static int computeLevel(int points) {
        int level = points / LEVEL_SPACE;
        return (level > MAX_LEVEL) ? MAX_LEVEL : level;
    }
}
