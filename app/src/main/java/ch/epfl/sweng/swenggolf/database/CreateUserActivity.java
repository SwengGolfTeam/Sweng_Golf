package ch.epfl.sweng.swenggolf.database;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.profile.User;

import static android.util.Patterns.EMAIL_ADDRESS;


public class CreateUserActivity extends AppCompatActivity {

    private EditText name;
    private ImageView photo;
    private User user;
    private static final String PRESENTATION_TEXT = "Welcome"
            + " to Polytroc, please put your username. You can"
            + " change your picture, your description an many"
            + " other things after this step.";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ch.epfl.sweng.swenggolf.R.layout.activity_create_user);
        name = findViewById(ch.epfl.sweng.swenggolf.R.id.name);
        name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(User.USERNAME_MAX_LENGTH)});
        TextView presentation = findViewById(R.id.presentation);
        presentation.setText(PRESENTATION_TEXT);
        photo = findViewById(ch.epfl.sweng.swenggolf.R.id.photo);
        user = Config.getUser();
        displayInformation(user);
    }

    /**
     * Launches the MainMenuActivity.
     */
    private void quit() {
        startActivity(new Intent(CreateUserActivity.this, MainMenuActivity.class));
    }

    /**
     * Display all the informations of a localUser.
     *
     * @param user The localUser
     */

    private void displayInformation(User user) {
        name.setText(user.getUserName());
        if (!Config.isTest()) {
            Picasso.with(this).load(user.getPhoto()).into(photo);
        }
    }


    /**
     * Launches the MainMenuActivity.
     *
     * @param view the current view
     */
    public void onClick(View view) {
        String userName = name.getText().toString();

        // Handle the exception if the EditText fields are null
        if (!(userName.length() < User.USERNAME_MIN_LENGTH)) {
            User u = User.userChanged(user, userName, user.getEmail());
            DatabaseUser.addUser(u);
            Config.setUser(u);
            quit();
        } else {
            Toast.makeText(this, R.string.incorrect_user_creation, Toast.LENGTH_SHORT).show();
        }
    }
}

