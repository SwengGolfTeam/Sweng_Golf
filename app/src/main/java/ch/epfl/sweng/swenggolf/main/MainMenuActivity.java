package ch.epfl.sweng.swenggolf.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.FirebaseAccount;
import ch.epfl.sweng.swenggolf.offer.CreateOfferActivity;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;
import ch.epfl.sweng.swenggolf.offer.ShowOfferActivity;
import ch.epfl.sweng.swenggolf.preference.ListPreferencesActivity;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;


public class MainMenuActivity extends AppCompatActivity {
    private FirebaseAccount account;
    private FragmentManager manager;
    private View nav;

    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_main_menu);
        nav = ((NavigationView) (this.findViewById(R.id.drawer))).getHeaderView(0);
        setToolBar();
        setUserDisplay();
        launchFragment();
    }

    private void setToolBar() {
        android.support.v7.widget.Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void launchFragment() {
        Fragment offerList = new ListOfferActivity();
        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction().add(R.id.centralFragment, offerList, "list_offer");
        transaction.addToBackStack("list_offer");
        transaction.commit();
    }

    private boolean setUserDisplay() {
        account = FirebaseAccount.getCurrentUserAccount();
        if (account != null) {
            boolean name = setUserName();
            boolean mail = setUserMail();
            setUserPic();
            return name && mail;
        } else {
            return false;
        }
    }

    private boolean setValue(String textValue, TextView textField) {
        if (textValue != null && !textValue.isEmpty() && textField != null) {
            textField.setText(textValue);
            return true;
        } else {
            return false;
        }
    }

    private boolean setUserName() {
        return setValue(account.getName(), (TextView) nav.findViewById(R.id.username));
    }

    private boolean setUserMail() {
        return setValue(account.getId(), (TextView) nav.findViewById(R.id.usermail));
    }

    private void setUserPic() {
        int errorDrawable = android.R.drawable.btn_dialog;
        ImageView userpicView = nav.findViewById(R.id.userpic);
        Picasso.with(this).load(account.getPhotoUrl()).error(errorDrawable).into(userpicView);
    }

    public static final String EXTRA_USER = "ch.epfl.sweng.swenggolf.USER";

    private void replaceCentralFragment(Fragment fragment) {
        manager.beginTransaction().replace(R.id.centralFragment, fragment).commit();
        DrawerLayout drawerLayout = findViewById(R.id.side_menu);
        drawerLayout.closeDrawers();
    }

    /**
     * Launches the ProfileActivity.
     *
     * @param item the menu item that triggers the activity
     */
    public void loadProfileActivity(MenuItem item) {
        replaceCentralFragment(new ProfileActivity());
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
     * Launches the PreferenceListActivity.
     *
     * @param item the menu item that triggers the activity
     */
    public void loadPreferenceListActivity(MenuItem item) {
        replaceCentralFragment(new ListPreferencesActivity());
    }

    /**
     * Launches the createOfferActivity.
     *
     * @param item the menu item that triggers the activity
     */
    public void createOfferActivity(MenuItem item) {
        replaceCentralFragment(new CreateOfferActivity());
    }
}
