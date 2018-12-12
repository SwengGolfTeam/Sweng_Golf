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
import android.widget.EditText;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.offer.answer.Answer;
import ch.epfl.sweng.swenggolf.offer.answer.Answers;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

public class StatisticsActivity extends FragmentConverter {
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
        setToolbar(R.drawable.ic_baseline_arrow_back_24px, user.getUserName());
        inflated = inflater.inflate(R.layout.activity_statistics, container, false);
        setRecyclerView();
        //TODO inflate with different Stats
        return inflated;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setRecyclerView() {
        RecyclerView mRecyclerView = inflated.findViewById(R.id.statistics);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        UserStats test = new UserStats();
        statisticsAdapter = new StatisticsAdapter(test);
        mRecyclerView.setAdapter(statisticsAdapter);


        // Add dividing line
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
    }

    //TODO retrieve Stats in database
    /*private void fetchMessages() {
        ValueListener<Answers> messageListener = new ValueListener<Answers>() {
            @Override
            public void onDataChange(Answers value) {
                if (value != null) {
                    messagesAdapter.setAnswers(value);
                }
            }

            @Override
            public void onCancelled(DbError error) {
            }
        };
        Database.getInstance().listen(Database.MESSAGES_PATH, offerId,
                messageListener, Answers.class);
        messagesAdapter.setUpdateListener(messageListener);
    }*/


}

