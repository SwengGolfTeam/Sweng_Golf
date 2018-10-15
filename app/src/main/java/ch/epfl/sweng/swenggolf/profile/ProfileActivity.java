package ch.epfl.sweng.swenggolf.profile;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.database.DatabaseConnection;
import ch.epfl.sweng.swenggolf.main.MainActivity;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;

public class ProfileActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        user = getIntent().getParcelableExtra(MainMenuActivity.EXTRA_USER);

        Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);

    }

    private void prepareProfileData() {
        DatabaseConnection db = DatabaseConnection.getInstance();
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> userData = new HashMap<>();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    userData.put(d.getKey(), d.getValue(String.class));
                }
                displayUserData(userData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DBERR", "databaseError:" + databaseError.getMessage());
            }
        };
        db.readObject("users", user.getUserId(), listener);

    }

    private void displayUserData(Map<String, String> userData) {
        if (userData != null) {
            TextView name = findViewById(R.id.name);
            name.setText(user.getUserName());
            displayCustomPicture(user.getPhoto());

            // TODO count the number of offers posted+answered and display them
        }

    }

    private void displayCustomPicture(String uriString) {
        ImageView imageView = findViewById(R.id.ivProfile);
        try {
            Uri photoUri = Uri.parse(uriString);
            Picasso.with(this).load(photoUri).into(imageView);
        } catch (NullPointerException e) {
            // TODO display error message and leaves the default option?
        }
    }

    /**
     * Launches the EditProfileActivity.
     * @param view the current view
     */
    public void editProfile(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra(MainMenuActivity.EXTRA_USER, user);
        startActivity(intent);
    }
}
