package ch.epfl.sweng.swenggolf.notification;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

public class NotificationsActivity extends FragmentConverter {
    private NotificationsAdapter mAdapter;
    private List<Notification> notifications;

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
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void setRecyclerView(View inflated) {
        RecyclerView mRecyclerView = inflated.findViewById(R.id.notifications_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        notifications = new ArrayList<>();

        mAdapter = new NotificationsAdapter(notifications, getClickListener());
        mRecyclerView.setAdapter(mAdapter);

        fetchNotifications(inflated);

        // Add dividing line
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));

        if (mAdapter.getItemCount() == 0) {
            inflated.findViewById(R.id.message_empty).setVisibility(View.VISIBLE);
        }
    }

    private void fetchNotifications(final View inflated) {
        User currentUser = Config.getUser();
        ValueListener<List<Notification>> listener = new ValueListener<List<Notification>>() {
            @Override
            public void onDataChange(List<Notification> value) {
                if (value != null) {
                    notifications = value;
                    if (value.size() != 0) {
                        inflated.findViewById(R.id.message_empty).setVisibility(View.GONE);
                    }
                    mAdapter.setNotifications(value);
                }
            }

            @Override
            public void onCancelled(DbError error) {
                Toast.makeText(NotificationsActivity.this.getContext(), getResources()
                        .getString(R.string.notif_error), Toast.LENGTH_SHORT).show();
            }
        };
        Database.getInstance()
                .readList(NotificationManager.getNotificationPath(
                        currentUser.getUserId()), listener, Notification.class);

    }

    private ItemClickListener getClickListener() {
        return new ItemClickListener() {

            @Override
            public void onClick(View view, int position) {
                Notification notification = notifications.get(position);
                if (notification.getOfferId() != null) {
                    Database.getInstance().read(Database.OFFERS_PATH, notification.getOfferId(), new ValueListener<Offer>() {
                        @Override
                        public void onDataChange(Offer value) {
                            if (value != null) {
                                replaceCentralFragment(createShowOfferWithOffer(value));
                            }
                        }

                        @Override
                        public void onCancelled(DbError error) {
                            // do nothing, it just won't respond
                        }
                    }, Offer.class);
                } else if (notification.getUserId() != null) {
                    Database.getInstance().read(Database.USERS_PATH, notification.getUserId(), new ValueListener<User>() {
                        @Override
                        public void onDataChange(User value) {
                            if (value != null) {
                                replaceCentralFragment(createShowProfileWithProfile(value));
                            }
                        }

                        @Override
                        public void onCancelled(DbError error) {
                            // do nothing, it just won't respond
                        }
                    }, User.class);
                }
            }
        };
    }

}
