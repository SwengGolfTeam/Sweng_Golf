package ch.epfl.sweng.swenggolf.tools;

import android.content.ContentResolver;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.offer.ShowOfferActivity;

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

    protected void replaceCentralFragment(Fragment fragment) {
        replaceFragment(fragment, R.id.centralFragment);
    }
}
