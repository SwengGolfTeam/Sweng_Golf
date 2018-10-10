package ch.epfl.sweng.swenggolf.preferences;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import ch.epfl.sweng.swenggolf.R;

public class ListPreferencesActivty extends Activity {
    private static final int spanCount = 4;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_preference);
        mRecyclerView = (RecyclerView) findViewById(R.id.preference_list);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this,spanCount);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ListPreferenceAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }
}
