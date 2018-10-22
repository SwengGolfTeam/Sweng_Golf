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

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.offer.CreateOfferActivity;
import ch.epfl.sweng.swenggolf.offer.ListOfferActivity;
import ch.epfl.sweng.swenggolf.preference.ListPreferencesActivity;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;


public class MainMenuActivity extends AppCompatActivity {
    private final User user = Config.getUser();
    private View nav;

    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_main_menu);
        android.support.v7.widget.Toolbar tb = findViewById(R.id.toolbar);
        nav = ((NavigationView) (this.findViewById(R.id.drawer))).getHeaderView(0);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadProfileActivity(v);
            }
        });
        setSupportActionBar(tb);
        setUserDisplay();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_main, menu);
        return true;
    }

    /**
     * Launches the ProfileActivity.
     *
     * @param view the current view
     */
    public void loadProfileActivity(View view) {
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
