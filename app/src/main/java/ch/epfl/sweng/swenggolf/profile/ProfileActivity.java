package ch.epfl.sweng.swenggolf.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;


public class ProfileActivity extends FragmentConverter {

    private User user;

    @Override
    public void onCreateView(LayoutInflater inflater, ViewGroup container,
                              )

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        user = Config.getUser();

        Toolbar toolbar = findViewById(R.id.profileToolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backHome = new Intent(ProfileActivity.this, MainMenuActivity.class);
                startActivity(backHome);
            }
        });

        displayUserData();
    }

    private void displayUserData() {
        TextView name = findViewById(R.id.name);
        name.setText(user.getUserName());
        ImageView imageView = findViewById(R.id.ivProfile);
        displayPicture(imageView, user, this);

        // TODO count the number of offers posted+answered and display them

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
}
