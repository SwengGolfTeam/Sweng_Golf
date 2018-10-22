package ch.epfl.sweng.swenggolf.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;


public class EditProfileActivity extends AppCompatActivity {
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        user = Config.getUser();
        if (user != null) {
            EditText editText = findViewById(R.id.edit_name);
            String userName = user.getUserName();
            editText.setText(userName);
            editText.setSelection(userName.length());

            ImageView imageView = findViewById(R.id.ivProfile);
            ProfileActivity.displayPicture(imageView, user, this);
        }
    }

    /**
     * Saves the changes and returns to the profile activity.
     *
     * @param view the current view
     */
    public void saveChangesAndReturn(View view) {
        EditText editText = findViewById(R.id.edit_name);
        String name = editText.getText().toString();

        user.setUserName(name);
        DatabaseUser.addUser(user);

        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);

    }

}
