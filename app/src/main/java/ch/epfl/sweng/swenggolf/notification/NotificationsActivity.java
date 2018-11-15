package ch.epfl.sweng.swenggolf.notification;

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

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

public class NotificationsActivity extends FragmentConverter {
    private NotificationsAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        setToolbar(R.drawable.ic_menu_black_24dp, R.string.notifications);
        View inflated = inflater.inflate(R.layout.activity_notifications, container, false);
        setRecyclerView(inflated);
        return inflated;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openDrawer();
                return true; // TODO voir quoi renvoyer
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRecyclerView(View inflated) {
        RecyclerView mRecyclerView = inflated.findViewById(R.id.notifications_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // TODO put this as an attribute but which modificators (private, final etc) ?
        List<Notification> notifications = new ArrayList<>();

        // TODO sample test
        String userName = "God";
        String offerName = "Could you please give this man a cookie?";
        notifications.add(new Notification(NotificationType.ANSWER_CHOSEN, userName, offerName));
        notifications.add(new Notification(NotificationType.ANSWER_POSTED, userName, offerName));
        notifications.add(new Notification(NotificationType.FOLLOW, userName, null));

        // TODO add a custom divider ?
        // Add dividing line
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
        mAdapter = new NotificationsAdapter(notifications);
        mRecyclerView.setAdapter(mAdapter);

    }
}
