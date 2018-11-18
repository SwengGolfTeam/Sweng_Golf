package ch.epfl.sweng.swenggolf;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

public class Leaderboard extends FragmentConverter {

    private LeaderboardAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private TextView errorMessage;
    private TextView noUser;
    public static final List<User> userList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        setToolbar(R.drawable.ic_menu_black_24dp, R.string.offers);
        View inflated = inflater.inflate(R.layout.activity_leaderboard, container, false);

        errorMessage = inflated.findViewById(R.id.error_message);
        noUser = inflated.findViewById(R.id.no_offers_to_show);
        setRecyclerView(inflated);
        return inflated;
    }

    private void setRecyclerView(View inflated) {
        noUser.setVisibility(View.VISIBLE);
        RecyclerView mRecyclerView = inflated.findViewById(R.id.offers_recycler_view);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new LeaderboardAdapter(userList);
        // Add dividing line
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        userList.clear();




    }

    /**
     * Get the users from the database.
     */
    protected void prepareOfferData(final View inflated,
                                    Database db) {
        Database database = Database.getInstance();
        inflated.findViewById(R.id.user_list_loading).setVisibility(View.VISIBLE);
        ValueListener listener = new ValueListener<List<User>>() {
            @Override
            public void onDataChange(List<User> users) {
                inflated.findViewById(R.id.user_list_loading).setVisibility(View.GONE);
                if (!users.isEmpty()) {
                    noUser.setVisibility(View.GONE);
                    mAdapter.add(users);
                }

            }

            @Override
            public void onCancelled(DbError error) {
                Log.d(error.toString(), "Unable to load offers from database");
                inflated.findViewById(R.id.user_list_loading).setVisibility(View.GONE);
                errorMessage.setVisibility(View.VISIBLE);
            }
        };
    }

}
