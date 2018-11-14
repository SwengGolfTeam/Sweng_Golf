package ch.epfl.sweng.swenggolf.notification;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
        setToolbar(R.drawable.ic_menu_black_24dp, R.string.nofifications);
        View inflated = inflater.inflate(R.layout.activity_notifications, container, false);
        setRecyclerView(inflated);
        return inflated;
    }

    private void setRecyclerView(View inflated) {
        RecyclerView mRecyclerView = inflated.findViewById(R.id.notifications_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // put this as an attribute but which modificators ?
        List<Notification> notifications = new ArrayList<>();

        mAdapter = new NotificationsAdapter(notifications);

    }
}
