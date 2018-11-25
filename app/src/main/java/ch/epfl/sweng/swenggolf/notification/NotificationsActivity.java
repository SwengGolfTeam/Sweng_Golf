package ch.epfl.sweng.swenggolf.notification;

import android.database.sqlite.SQLiteException;
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

        checkUserPoints();

        fetchNotifications(inflated);

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
                    Collections.reverse(notifications); // so that they appear the most recent on top
                    mAdapter.setNotifications(notifications);
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

    private void checkUserPoints() {
        LocalDatabase localDb = new LocalDatabase(getContext(), null, 1);
        int currentLevel = Badge.computeLevel(Config.getUser().getPoints());
        int previousLevel = localDb.readLevel();
        if (currentLevel > previousLevel) {
            Notification n = new Notification(NotificationType.LEVEL_GAINED, null, null);
            NotificationManager.addPendingNotification(Config.getUser().getUserId(), n);
            localDb.writeLevel(currentLevel);
        }
    }

    private ItemClickListener getClickListener() {
        return new ItemClickListener() {

            @Override
            public void onClick(View view, int position) {
                Notification notification = notifications.get(position);
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
