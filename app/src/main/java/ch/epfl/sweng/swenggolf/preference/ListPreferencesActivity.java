package ch.epfl.sweng.swenggolf.preference;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.offer.CreateOfferActivity;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

public class ListPreferencesActivity extends FragmentConverter {
    private static final int SPAN_COUNT = 3;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflated = inflater.inflate(R.layout.activity_list_preference, container,false);
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        mRecyclerView = inflated.findViewById(R.id.preference_list);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this.getContext(), SPAN_COUNT);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ListPreferenceAdapter();
        mRecyclerView.setAdapter(mAdapter);
        return inflated;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home : {
                openDrawer();
                break;
            }
            case R.id.add_offer : {
                loadCreateOfferActivity();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Launches the CreateOfferActivity.
     */
    public void loadCreateOfferActivity() {
        replaceCentralFragment(new CreateOfferActivity());
    }

}
