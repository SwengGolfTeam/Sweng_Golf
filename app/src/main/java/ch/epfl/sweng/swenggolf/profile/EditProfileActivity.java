package ch.epfl.sweng.swenggolf.profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.database.FakeUserDatabase;
import ch.epfl.sweng.swenggolf.main.MainActivity;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;

public class EditProfileActivity extends AppCompatActivity {
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        user = intent.getParcelableExtra(MainMenuActivity.EXTRA_USER);

        EditText editText = findViewById(R.id.edit_name);
        String userName = user.getUserName();
        if (userName != null) {
            editText.setText(userName);
            editText.setSelection(userName.length());
        }

    }

    /**
     * Saves the changes and returns to the profile activity.
     * @param view the current view
     */
    public void saveChangesAndReturn(View view) {
        EditText editText = findViewById(R.id.edit_name);
        String username = editText.getText().toString();
        user.setUserName(username);

        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(MainMenuActivity.EXTRA_USER, user);
        startActivity(intent);
    }

}
