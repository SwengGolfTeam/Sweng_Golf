package ch.epfl.sweng.swenggolf.notification;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.LocalDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.profile.Badge;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

/**
 * Notification fragment which shows all the User's notification.
 */
public class NotificationsFragment extends FragmentConverter {
    private NotificationsAdapter mAdapter;
    private TextView noNotification;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        setToolbar(R.drawable.ic_menu_black_24dp, R.string.notifications);
        View inflated = inflater.inflate(R.layout.activity_notifications, container, false);
        noNotification = inflated.findViewById(R.id.message_empty);
        setRecyclerView(inflated);
        setRefreshListener(inflated);


        return inflated;
    }

    private void setRefreshListener(final View inflated) {
        final SwipeRefreshLayout refresh = inflated.findViewById(R.id.refresh_notifications);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchNotifications();
                refresh.setRefreshing(false);
            }
        });
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

        mAdapter = new NotificationsAdapter(getClickListener());
        mRecyclerView.setAdapter(mAdapter);

        checkUserPoints();

        fetchNotifications();

    }

    private void fetchNotifications() {
        User currentUser = Config.getUser();
        ValueListener<List<Notification>> listener = new ValueListener<List<Notification>>() {
            @Override
            public void onDataChange(List<Notification> value) {
                if (value != null) {
                    displayEmptyListMessage(value);
                    // so that they appear the most recent on top
                    Collections.reverse(value);
                    mAdapter.setNotifications(value);
                }
            }

            @Override
            public void onCancelled(DbError error) {
                Toast.makeText(NotificationsFragment.this.getContext(), getResources()
                        .getString(R.string.notif_error), Toast.LENGTH_SHORT).show();
            }
        };
        Database.getInstance()
                .readList(NotificationManager.getNotificationPath(
                        currentUser.getUserId()), listener, Notification.class);

    }

    private void displayEmptyListMessage(List<Notification> value) {
        if (value.isEmpty()) {
            noNotification.setVisibility(View.VISIBLE);
        } else {
            noNotification.setVisibility(View.INVISIBLE);
        }
    }

    private void checkUserPoints() {
        LocalDatabase localDb = new LocalDatabase(getContext(), null, 1);
        int currentLevel = Badge.computeLevel(Config.getUser().getPoints());
        int previousLevel = localDb.readLevel();
        if (currentLevel > previousLevel) {
            Notification n = new Notification(NotificationType.LEVEL_GAINED, null, null);
            NotificationManager.addPendingNotification(Config.getUser().getUserId(), n);
        }
        localDb.writeLevel(currentLevel);
    }

    private ItemClickListener getClickListener() {
        return new ItemClickListener() {

            @Override
            public void onClick(View view, int position) {
                Notification notification = mAdapter.getNotifications().get(position);
                if (notification.getOfferId() != null) {
                    lookUpAndGoTo(notification.getOfferId(), Database.OFFERS_PATH);
                } else if (notification.getUserId() != null) {
                    lookUpAndGoTo(notification.getUserId(), Database.USERS_PATH);
                } else {
                    replaceCentralFragment(createShowProfileWithProfile(Config.getUser()));
                }
            }
        };
    }

    private void lookUpAndGoTo(String id, String destination) {
        final boolean isUser = destination.equals(Database.USERS_PATH);
        Class c = isUser ? User.class : Offer.class;
        Database.getInstance().read(destination, id, new ValueListener<Object>() {
            @Override
            public void onDataChange(Object value) {
                if (value != null) {
                    if (isUser) {
                        replaceCentralFragment(createShowProfileWithProfile((User) value));
                    } else {
                        replaceCentralFragment(createShowOfferWithOffer((Offer) value));
                    }
                }
            }

            @Override
            public void onCancelled(DbError error) {
                // do nothing, it just won't respond
            }
        }, c);
    }

}
