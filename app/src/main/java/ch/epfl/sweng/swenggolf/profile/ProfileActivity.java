package ch.epfl.sweng.swenggolf.profile;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;

public class ProfileActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        user = getIntent().getParcelableExtra(MainMenuActivity.EXTRA_USER);
        if (user == null) { // if not authenticated (e.g. tests)
            user = new User();
        }

        Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backHome = new Intent(ProfileActivity.super.getApplicationContext(), MainMenuActivity.class);
                startActivity(backHome);
            }
        });

        displayUserData();
    }

    private void displayUserData() {
        TextView name = findViewById(R.id.name);
        name.setText(user.getUserName());
        ImageView imageView = findViewById(R.id.ivProfile);
        if (!user.getPhoto().isEmpty()) {
            Uri photoUri = Uri.parse(user.getPhoto());
            Picasso.with(this).load(photoUri).into(imageView);
        }

        // TODO count the number of offers posted+answered and display them

    }


    /**
     * Launches the EditProfileActivity.
     *
     * @param view the current view
     */
    public void editProfile(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra(MainMenuActivity.EXTRA_USER, user);
        startActivity(intent);
    }
}
