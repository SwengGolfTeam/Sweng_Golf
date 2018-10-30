package ch.epfl.sweng.swenggolf.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;

import static ch.epfl.sweng.swenggolf.database.DbError.NONE;


public class ProfileActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        user = getIntent().getParcelableExtra("ch.epfl.sweng.swenggolf.user");
        if (user == null) {
            throw new NullPointerException("The user given to ProfileActivity can not be null");
        }
        Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backHome = new Intent(ProfileActivity.this, MainMenuActivity.class);
                startActivity(backHome);
            }
        });

        showEditButton();

        displayUserData();
    }

    private void showEditButton() {
        if (user.getUserId().equals(Config.getUser().getUserId())) {
            ImageButton button = findViewById(R.id.edit);
            button.setVisibility(View.VISIBLE);
        }
    }

    private void displayUserData() {
        TextView name = findViewById(R.id.name);
        name.setText(user.getUserName());
        ImageView imageView = findViewById(R.id.ivProfile);
        displayPicture(imageView, user, this);

        TextView preference = findViewById(R.id.preference1);
        preference.setText(user.getPreference());
        TextView description = findViewById(R.id.description);
        description.setText(user.getDescription());

    }

    protected static void displayPicture(ImageView imageView, User user, Context context) {
        if (!user.getPhoto().isEmpty()) {
            Uri photoUri = Uri.parse(user.getPhoto());
            Picasso.with(context).load(photoUri).into(imageView);
        }
    }


    /**
     * Launches the EditProfileActivity.
     *
     * @param view the current view
     */
    public void editProfile(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    /**
     * Follow the user showed in the profile.
     *
     * @param view the current view
     */
    public void follow(View view) {
        User currentUser = Config.getUser();
        CompletionListener listener = new CompletionListener() {
            @Override
            public void onComplete(DbError error) {
                if (error == NONE) {
                    Toast.makeText(ProfileActivity.this, getResources()
                                    .getString(R.string.now_following) + " " + user.getUserName(),
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(ProfileActivity.this, getResources()
                                    .getString(R.string.error_following) + " " + user.getUserName(),
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        };
        Database.getInstance().write("/followers/" + currentUser.getUserId(), user.getUserId(), user.getUserId(),
                listener);
    }
}
