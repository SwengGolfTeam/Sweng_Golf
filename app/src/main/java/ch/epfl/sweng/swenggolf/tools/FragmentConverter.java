package ch.epfl.sweng.swenggolf.tools;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.messaging.MessagingFragment;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.ShowOfferFragment;
import ch.epfl.sweng.swenggolf.offer.create.CreateOfferFragment;
import ch.epfl.sweng.swenggolf.profile.EditProfileFragment;
import ch.epfl.sweng.swenggolf.profile.ProfileFragment;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.statistics.StatisticsFragment;

/**
 * Class adding functionalities to work with the MainMenuActivity
 * and automatize initialization such as setting the toolbar.
 * It also adds functionalities to create initialized fragments
 * such as ShowOfferFragment or CreateOfferFragment.
 */
public abstract class FragmentConverter extends Fragment {


    public static final String FRAGMENTS_TO_SKIP = "fragmentsToSkip";
    public static final String FUTURE_FRAGMENTS_TO_SKIP = "futureFragmentsToSkip";

    /**
     * Creates a ProfileFragment wit arguments already set.
     *
     * @param user the user to pass to the fragment.
     * @return a new ProfileFragment fragment with user.
     */
    public static ProfileFragment createShowProfileWithProfile(User user) {
        String key = User.USER;
        return fillFragment(new ProfileFragment(), key, user);
    }

    /**
     * Creates a ProfileFragment wit arguments already set.
     *
     * @param user the user to pass to the fragment.
     * @return a new ProfileFragment fragment with user.
     */
    public static ProfileFragment createShowProfileWithProfile(User user, int fragmentsToSkip) {
        String key = User.USER;
        ProfileFragment fragment = fillFragment(new ProfileFragment(), key, user);
        fragment.getArguments().putInt(FRAGMENTS_TO_SKIP, fragmentsToSkip);
        return fragment;
    }

    /**
     * Create a EditProfileFragment with a number of fragments to skip.
     *
     * @param fragmentsToSkip the number of fragments we currently need to skip when we click on
     *                        back button
     * @return the EditProfileFragment
     */
    public static EditProfileFragment createEditProfileActivity(int fragmentsToSkip) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FUTURE_FRAGMENTS_TO_SKIP, fragmentsToSkip);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * Creates a ShowOfferFragment with arguments already set.
     *
     * @param offer the offer to pass to the fragment.
     * @return a new ShowOfferFragment with an offer.
     */
    public static ShowOfferFragment createShowOfferWithOffer(Offer offer) {
        return fillFragment(new ShowOfferFragment(), Offer.OFFER, offer);
    }

    /**
     * Creates a ShowOfferFragment with arguments already set.
     *
     * @param offer           the offer to pass to the fragment.
     * @param fragmentsToSkip the number of fragments to skip when the user click on back button.
     * @return new ShowOfferFragment with an offer.
     */
    public static ShowOfferFragment createShowOfferWithOffer(Offer offer, int fragmentsToSkip) {
        ShowOfferFragment fragment = fillFragment(new ShowOfferFragment(), Offer.OFFER, offer);
        return putFragmentsToSkip(fragment, fragmentsToSkip);
    }

    @NonNull
    private static <T extends Fragment> T putFragmentsToSkip(T fragment, int fragmentsToSkip) {
        fragment.getArguments().putInt(FRAGMENTS_TO_SKIP, fragmentsToSkip);
        return fragment;
    }

    /**
     * Creates a CreateOfferFragment with arguments already set.
     *
     * @param offer the offer to pass to the fragment.
     * @return a new CreateOfferFragment with an offer.
     */
    public static CreateOfferFragment createOfferActivityWithOffer(Offer offer) {
        return fillFragment(new CreateOfferFragment(), Offer.OFFER, offer);
    }

    /**
     * Creates a CreateOfferFragment with arguments already set.
     *
     * @param offer the offer to pass to the fragment.
     * @return a new CreateOfferFragment with an offer.
     */
    public static CreateOfferFragment createOfferActivityWithOffer(Offer offer,
                                                                   int fragmentsToSkip) {
        CreateOfferFragment fragment = fillFragment(new CreateOfferFragment(), Offer.OFFER, offer);
        fragment.getArguments().putInt(FUTURE_FRAGMENTS_TO_SKIP, fragmentsToSkip);
        return fragment;
    }

    /**
     * Creates a MessagingFragment with all information needed.
     *
     * @param offer the offer which the discussion is about
     * @param user  the user to talk to
     * @return a new MessagingFragment with the user and about the offer
     */
    public static MessagingFragment createMessagingActivityWithOfferAndUser(
            Offer offer, User user) {
        MessagingFragment fragment = fillFragment(new MessagingFragment(), User.USER, user);
        fragment.getArguments().putString(Offer.OFFER, offer.getUuid());
        return fragment;
    }

    /**
     * Creates a StatisticsFragment for a specific User.
     *
     * @param user the user for which we want the stats
     * @return a new StatisticsFragment with all statistics for the User
     */
    public static StatisticsFragment createStatisticsActivityWithUser(User user) {
        StatisticsFragment fragment = fillFragment(new StatisticsFragment(), User.USER, user);
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

    protected void replaceFragment(Fragment fragment, int viewId) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction().replace(viewId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    protected void closeSoftKeyboard(EditText edited) {
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edited.getWindowToken(), 0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_empty, menu);
    }

    public void replaceCentralFragment(Fragment fragment) {
        replaceFragment(fragment, R.id.centralFragment);
    }

    protected <T extends View> T findViewById(int id) {
        return getView().findViewById(id);
    }


    protected void setToolbar(int homeIconResId, int titleResId) {
        setToolbar(homeIconResId, getResources().getString(titleResId));
    }

    protected void setToolbar(int homeIconResId, String title) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);
        actionBar.setHomeAsUpIndicator(homeIconResId);
        actionBar.setTitle(title);
    }

    protected void openDrawer() {
        DrawerLayout drawer = getActivity().findViewById(R.id.side_menu);
        drawer.openDrawer(GravityCompat.START);
    }

    /**
     * This method is called when the user leave this fragment (use back button or back arrow).
     * Fragments that need to do some action (e.g. save data) when the we are leaving the fragment
     * need to override this method.
     */
    public void close() {

    }
}
