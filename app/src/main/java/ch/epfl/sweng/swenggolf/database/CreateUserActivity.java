package ch.epfl.sweng.swenggolf.database;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;


public class CreateUserActivity extends AppCompatActivity {

    private EditText mail;
    private EditText name;
    private ImageView photo;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ch.epfl.sweng.swenggolf.R.layout.activity_create_user);
        name = findViewById(ch.epfl.sweng.swenggolf.R.id.name);
        mail = findViewById(ch.epfl.sweng.swenggolf.R.id.mail);
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
        mail.setText(user.getEmail());
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
        String userMail = mail.getText().toString();

        //handle the exception if the EditText fields are null
        if (!userName.isEmpty() && !userMail.isEmpty()) {
            User u = User.userChanged(user, userName, userMail);
            DatabaseUser.addUser(u);
            Config.setUser(u);
            quit();
        } else {
            Toast.makeText(this, "Please, enter correct info", Toast.LENGTH_SHORT).show();
            onClick(view);
        }
    }
}

