package ch.epfl.sweng.swenggolf.tools;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.offer.CreateOfferActivity;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.ShowOfferActivity;
import ch.epfl.sweng.swenggolf.profile.EditProfileActivity;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;
import ch.epfl.sweng.swenggolf.profile.User;

public abstract class FragmentConverter extends Fragment {


    public static final String FRAGMENTSTOSKIP = "fragmentsToSkip";
    public static final String FUTUREFRAGMENTSTOSKIP = "futureFragmentsToSkip";

    protected void replaceFragment(Fragment fragment, int viewId) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction().replace(viewId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_empty, menu);
    }

    public void replaceCentralFragment(Fragment fragment) {
        replaceFragment(fragment, R.id.centralFragment);
    }


    /**
     * Creates a ProfileActivity wit arguments already set.
     *
     * @param user the user to pass to the fragment.
     * @return a new ProfileActivity fragment with user.
     */
    public static ProfileActivity createShowProfileWithProfile(User user) {
        String key = "ch.epfl.sweng.swenggolf.user";
        return fillFragment(new ProfileActivity(), key, user);
    }

    /**
     * Creates a ProfileActivity wit arguments already set.
     *
     * @param user the user to pass to the fragment.
     * @return a new ProfileActivity fragment with user.
     */
    public static ProfileActivity createShowProfileWithProfile(User user, int fragmentsToSkip) {
        String key = "ch.epfl.sweng.swenggolf.user";
        ProfileActivity fragment = fillFragment(new ProfileActivity(), key, user);
        fragment.getArguments().putInt(FRAGMENTSTOSKIP, fragmentsToSkip);
        return fragment;
    }

    /**
     * Create a EditProfileActivity with a number of fragments to skip.
     *
     * @param fragmentsToSkip the number of fragments we currently need to skip when we click on
     *                        back button
     * @return the EditProfileActivity
     */
    public static EditProfileActivity createEditProfileActivity(int fragmentsToSkip) {
        EditProfileActivity fragment = new EditProfileActivity();
        Bundle bundle = new Bundle();
        bundle.putInt(FUTUREFRAGMENTSTOSKIP, fragmentsToSkip);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * Creates a ShowOfferActivity with arguments already set.
     *
     * @param offer the offer to pass to the fragment.
     * @return a new ShowOfferActivity with an offer.
     */
    public static ShowOfferActivity createShowOfferWithOffer(Offer offer) {
        return fillFragment(new ShowOfferActivity(), Offer.OFFER, offer);
    }

    public static ShowOfferActivity createShowOfferWithOffer(Offer offer, int fragmentsToSkip) {
        ShowOfferActivity fragment = fillFragment(new ShowOfferActivity(), Offer.OFFER, offer);
        return putFragmentsToSkip(fragment, fragmentsToSkip);
    }

    @NonNull
    private static <T extends Fragment> T putFragmentsToSkip(T fragment, int fragmentsToSkip) {
        fragment.getArguments().putInt(FRAGMENTSTOSKIP, fragmentsToSkip);
        return fragment;
    }

    /**
     * Creates a CreateOfferActivity with arguments already set.
     *
     * @param offer the offer to pass to the fragment.
     * @return a new CreateOfferActivity with an offer.
     */
    public static CreateOfferActivity createOfferActivityWithOffer(Offer offer) {
        return fillFragment(new CreateOfferActivity(), Offer.OFFER, offer);
    }

    /**
     * Creates a CreateOfferActivity with arguments already set.
     *
     * @param offer the offer to pass to the fragment.
     * @return a new CreateOfferActivity with an offer.
     */
    public static CreateOfferActivity createOfferActivityWithOffer(Offer offer,
                                                                   int fragmentsToSkip) {
        CreateOfferActivity fragment = fillFragment(new CreateOfferActivity(), Offer.OFFER, offer);
        fragment.getArguments().putInt(FUTUREFRAGMENTSTOSKIP, fragmentsToSkip);
        return fragment;
    }

    /**
     * Returns a fragment with a parcelable added as an argument.
     *
     * @param fragment arguments will be attached to it.
     * @param key      the tag referencing p in the arguments.
     * @param p        the argument to pass.
     * @param <T>      the type of fragment.
     * @return fragment with p as an argument that can be get with key as key.
     */
    public static <T extends Fragment> T fillFragment(T fragment, String key, Parcelable p) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(key, p);
        fragment.setArguments(bundle);
        return fragment;
    }

    protected <T extends View> T findViewById(int id) {
        return getView().findViewById(id);
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
}
