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
            displayElement((EditText) findViewById(R.id.edit_name), user.getUserName());
            displayElement((EditText) findViewById(R.id.edit_pref), user.getPreference());
            ImageView imageView = findViewById(R.id.ivProfile);
            ProfileActivity.displayPicture(imageView, user, this);
        }
    }

    private void displayElement(EditText editText, String elem) {
        editText.setText(elem);
        editText.setSelection(elem.length());
    }

    /**
     * Saves the changes and returns to the profile activity.
     *
     * @param view the current view
     */
    public void saveChangesAndReturn(View view) {
        // save new name
        EditText editedName = findViewById(R.id.edit_name);
        String name = editedName.getText().toString();
        user.setUserName(name);

        // save new preferences
        EditText editedPref = findViewById(R.id.edit_pref);
        String pref = editedPref.getText().toString();
        user.setPreference(pref);

        DatabaseUser.addUser(user);

        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
