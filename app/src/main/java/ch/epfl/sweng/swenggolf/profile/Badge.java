package ch.epfl.sweng.swenggolf.profile;

import ch.epfl.sweng.swenggolf.R;

public final class Badge {

    public static int getDrawable(int points){
        int level = computeLevel(points);
        switch (level){
            case 1: return R.drawable.badge01;
            case 2: return R.drawable.badge02;
            case 3: return R.drawable.badge03;
            case 4: return R.drawable.badge04;
            case 5: return R.drawable.badge05;
            case 6: return R.drawable.badge06;
            case 7: return R.drawable.badge07;
            case 8: return R.drawable.badge08;
            case 9: return R.drawable.badge09;
            case 10: return R.drawable.badge10;
            case 11: return R.drawable.badge11;
            case 12: return R.drawable.badge12;
            case 13: return R.drawable.badge13;
            case 14: return R.drawable.badge14;
            default: return R.drawable.badge00;
        }

    }

    private static int computeLevel(int points){
        return points/6+1;
    }
}
