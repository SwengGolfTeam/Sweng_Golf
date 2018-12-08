package ch.epfl.sweng.swenggolf.offer;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

public class ListOwnOfferPager extends FragmentPagerAdapter {

    private Context context;
    private ListOwnOfferTabActivity tabOpen;
    private ListOwnOfferTabActivity tabClosed;

    /**
     * Creates a new adapter for a viewPager to display a user offers.
     *
     * @param user the user who's offers are displayed.
     * @param context the context of the activity in which this pager is used.
     * @param manager the fragment manager used to manage the different fragments of the pager.
     */
    ListOwnOfferPager(User user, Context context, FragmentManager manager) {
        super(manager);
        this.context = context;
        tabOpen = new ListOwnOfferTabActivity();
        tabClosed = new ListOwnOfferTabActivity();
        FragmentConverter.fillFragment(tabOpen, User.USER, user);
        FragmentConverter.fillFragment(tabClosed, User.USER, user);
        tabOpen.getArguments().putBoolean(ListOfferActivity.DISPLAY_CLOSED_BUNDLE_KEY, false);
        tabClosed.getArguments().putBoolean(ListOfferActivity.DISPLAY_CLOSED_BUNDLE_KEY, true);
    }

    @Override
    public Fragment getItem(int i) {
        if(i == 0) {
            return tabOpen;
        }
        return tabClosed;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0) {
            return context.getString(R.string.open);
        }
        return context.getString(R.string.closed);
    }


}
