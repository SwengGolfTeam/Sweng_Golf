package ch.epfl.sweng.swenggolf.tools;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.offer.CreateOfferActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.ShowOfferActivity;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;

public abstract class FragmentConverter extends Fragment {

    protected <T extends View> T findViewById(int id) {
        return getView().findViewById(id);
    }

    protected Intent getIntent() {
        return getActivity().getIntent();
    }

    protected ContentResolver getContentResolver() {
        return getContext().getContentResolver();
    }

    protected void replaceFragment(Fragment fragment, int viewId) {
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(viewId, fragment).commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_empty, menu);
    }

    protected void replaceCentralFragment(Fragment fragment) {
        replaceFragment(fragment, R.id.centralFragment);
    }

    protected void setToolbar(int homeIconResId, int titleResId) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);
        actionBar.setHomeAsUpIndicator(homeIconResId);
        actionBar.setTitle(getResources().getString(titleResId));
    }

    protected void openDrawer() {
        DrawerLayout drawer = getActivity().findViewById(R.id.side_menu);
        drawer.openDrawer(GravityCompat.START);
    }

    /**
     * Creates a ProfileActivity wit arguments already set.
     * @param user the user to pass to the fragment.
     * @return a new ProfileActivity fragment with user.
     */
    public static ProfileActivity createShowProfileWithProfile(User user) {
        String key = "ch.epfl.sweng.swenggolf.user";
        return fillFragment(new ProfileActivity(), key, user);
    }

    /**
     * Creates a ShowOfferActivity with arguments already set.
     * @param offer the offer to pass to the fragment.
     * @return a new ShowOfferActivity with an offer.
     */
    public static ShowOfferActivity createShowOfferWithOffer(Offer offer) {
        return fillFragment(new ShowOfferActivity(), "offer", offer);
    }

    /**
     * Creates a CreateOfferActivity with arguments already set.
     * @param offer the offer to pass to the fragment.
     * @return a new CreateOfferActivity with an offer.
     */
    public static CreateOfferActivity createOfferActivityWithOffer(Offer offer) {
        return fillFragment(new CreateOfferActivity(), "offer", offer);
    }

    /**
     * Returns a fragment with a parcelable added as an argument.
     * @param fragment arguments will be attached to it.
     * @param key the tag referencing p in the arguments.
     * @param p the argument to pass.
     * @param <T> the type of fragment.
     * @return fragment with p as an argument that can be get with key as key.
     */
    public static <T extends Fragment> T fillFragment(T fragment, String key, Parcelable p) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(key, p);
        fragment.setArguments(bundle);
        return fragment;
    }
}
