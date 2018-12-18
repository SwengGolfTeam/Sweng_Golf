package ch.epfl.sweng.swenggolf.statistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

public class StatisticsFragment extends FragmentConverter {
    private View inflated;
    private User user;
    private StatisticsAdapter statisticsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = getArguments();
        user = bundle.getParcelable(User.USER);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        setToolbar(R.drawable.ic_baseline_arrow_back_24px, "Statistics");
        inflated = inflater.inflate(R.layout.activity_statistics, container, false);
        getStats();
        return inflated;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void setRecyclerView(UserStats stats) {
        RecyclerView mRecyclerView = inflated.findViewById(R.id.statistics);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        statisticsAdapter = new StatisticsAdapter(stats);
        mRecyclerView.setAdapter(statisticsAdapter);

        // Add dividing line
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
    }

    private void getStats() {
        ValueListener<UserStats> listener = new ValueListener<UserStats>() {
            @Override
            public void onDataChange(UserStats stats) {
                if (stats != null) {
                    setRecyclerView(stats);
                }
            }

            @Override
            public void onCancelled(DbError error) {
                UserStats.checkBackwardsCompatibility(error, user.getUserId());
                setRecyclerView(new UserStats()); // default values no need to reload
            }
        };
        UserStats.read(listener, user.getUserId());
    }
}

