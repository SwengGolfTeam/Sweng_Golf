package ch.epfl.sweng.swenggolf.main;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.leaderboard.Leaderboard;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.network.Network;
import ch.epfl.sweng.swenggolf.network.NetworkReceiver;
import ch.epfl.sweng.swenggolf.notification.NotificationsActivity;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;
import ch.epfl.sweng.swenggolf.offer.ListOwnOfferActivity;
import ch.epfl.sweng.swenggolf.offer.create.CreateOfferActivity;
import ch.epfl.sweng.swenggolf.preference.ListPreferencesActivity;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

import static ch.epfl.sweng.swenggolf.tools.FragmentConverter.FRAGMENTS_TO_SKIP;

/**
 * Activity which represents the actual main menu.
 * All the Fragments of the application are displayed in
 * the center of this activity.
 */
public class MainMenuActivity extends AppCompatActivity {

    private final User user = Config.getUser();
    private FragmentManager manager;
    private View nav;

    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        NetworkReceiver.registerReceiver(this, new NetworkReceiver());
        setContentView(R.layout.activity_main_menu);
        setToolBar();
        nav = ((NavigationView) (this.findViewById(R.id.drawer))).getHeaderView(0);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadProfileActivity(null);
            }
        });
        setUserDisplay();
        FragmentManager.enableDebugLogging(true);
        if (savedInstances == null) {
            launchFragment();
        }
    }

    private void setToolBar() {
        android.support.v7.widget.Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void launchFragment() {
        Fragment offerList = new ListOfferActivity();
        manager = getSupportFragmentManager();
        FragmentTransaction transaction =
                manager.beginTransaction()
                        .add(R.id.centralFragment, offerList);
        transaction.commit();
    }

    private void setUserDisplay() {
        if (user != null) {
            setUserName();
            setUserMail();
            setUserPic();
        }
    }

    private void setValue(String textValue, TextView textField) {
        if (textValue != null && !textValue.isEmpty() && textField != null) {
            textField.setText(textValue);
        }
    }

    private void setUserName() {
        setValue(user.getUserName(), (TextView) nav.findViewById(R.id.username));
    }

    private void setUserMail() {
        setValue(user.getEmail(), (TextView) nav.findViewById(R.id.usermail));
    }

    private void setUserPic() {
        int errorDrawable = android.R.drawable.btn_dialog;
        if (!user.getPhoto().isEmpty()) {
            ImageView userpicView = nav.findViewById(R.id.userpic);
            Picasso.with(this).load(user.getPhoto()).error(errorDrawable).into(userpicView);
        }
    }

    private void replaceCentralFragment(Fragment fragment) {
        //drain the backstack
        int backStackSize = manager.getBackStackEntryCount();
        for (int i = 0; i < backStackSize; ++i) {
            manager.popBackStack();
        }

        manager.beginTransaction().replace(R.id.centralFragment, fragment)
                .addToBackStack(null)
                .commit();
        DrawerLayout drawerLayout = findViewById(R.id.side_menu);
        drawerLayout.closeDrawers();
    }

    /**
     * Launches the ProfileActivity.
     *
     * @param item the menu item that triggers the activity
     */
    public void loadProfileActivity(MenuItem item) {
        replaceCentralFragment(FragmentConverter.createShowProfileWithProfile(user));
    }

    /**
     * Launches the ShowOffersActivity.
     *
     * @param item the menu item that triggers the activity
     */
    public void loadShowOffersActivity(MenuItem item) {
        replaceCentralFragment(new ListOfferActivity());
    }

    /**
     * Launches the ListOwnOfferActivity.
     *
     * @param item the menu item that triggers the activity
     */
    public void loadListOwnOfferActivity(MenuItem item) {
        replaceCentralFragment(new ListOwnOfferActivity());
    }

    /**
     * Launches the LeaderBoardActivity.
     *
     * @param item the menu item that triggers the activity
     */
    public void loadLeaderboard(MenuItem item) {
        replaceCentralFragment(new Leaderboard());
    }

    /**
     * Launches the PreferenceListActivity.
     *
     * @param item the menu item that triggers the activity
     */
    public void loadPreferenceListActivity(MenuItem item) {
        replaceCentralFragment(new ListPreferencesActivity());
    }

    /**
     * Launches the NotificationsActivity.
     *
     * @param item the menu item that triggers the activity
     */
    public void loadNotificationsActivity(MenuItem item) {
        replaceCentralFragment(new NotificationsActivity());
    }

    /**
     * Launches the createOfferActivity.
     *
     * @param item the menu item that triggers the activity
     */
    public void createOfferActivity(MenuItem item) {
        replaceCentralFragment(new CreateOfferActivity());
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            skipFragments();
            super.onBackPressed();
        } else {
            moveTaskToBack(true);
        }

    }

    private void skipFragments() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.centralFragment);
        final Bundle bundle = fragment.getArguments();
        if (bundle != null && bundle.containsKey(FRAGMENTS_TO_SKIP)) {
            int nbr = bundle.getInt(FRAGMENTS_TO_SKIP);
            for (int i = 0; i < nbr; ++i) {
                getSupportFragmentManager().popBackStackImmediate();
            }
        }

    }
}
