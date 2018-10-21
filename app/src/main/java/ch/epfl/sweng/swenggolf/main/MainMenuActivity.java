package ch.epfl.sweng.swenggolf.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.FirebaseAccount;
import ch.epfl.sweng.swenggolf.offer.CreateOfferActivity;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;
import ch.epfl.sweng.swenggolf.preference.ListPreferencesActivity;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;


public class MainMenuActivity extends AppCompatActivity {
    private FirebaseAccount account;
    private View nav;

    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_main_menu);
        android.support.v7.widget.Toolbar tb = findViewById(R.id.toolbar);
        nav = ((NavigationView) (this.findViewById(R.id.drawer))).getHeaderView(0);
        setSupportActionBar(tb);
        setUserDisplay();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_main, menu);
        return true;
    }

    public static final String EXTRA_USER = "ch.epfl.sweng.swenggolf.USER";

    /**
     * Launches the ProfileActivity.
     *
     * @param item the menu item that triggers the activity
     */
    public void loadProfileActivity(MenuItem item) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the CreateOfferActivity.
     *
     * @param item the menu item that triggers the activity
     */
    public void loadCreateOfferActivity(MenuItem item) {
        Intent intent = new Intent(this, CreateOfferActivity.class);
        // TODO implement username when login effective
        intent.putExtra("username", "God");
        startActivity(intent);
    }

    /**
     * Launches the ShowOffersActivity.
     *
     * @param item the menu item that triggers the activity
     */
    public void loadShowOffersActivity(MenuItem item) {
        Intent intent = new Intent(this, ListOfferActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the PreferenceListActivity.
     *
     * @param item the menu item that triggers the activity
     */
    public void loadPreferenceListActivity(MenuItem item) {
        Intent intent = new Intent(this, ListPreferencesActivity.class);
        startActivity(intent);
    }
}
