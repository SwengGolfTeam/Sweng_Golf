package ch.epfl.sweng.swenggolf.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;


public class EditProfileActivity extends AppCompatActivity {
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        user = intent.getParcelableExtra(MainMenuActivity.EXTRA_USER);

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

        //TODO make the write in database inside user class ?
        if (!user.getUserId().isEmpty()) { // when in test mode for instance
            Config.getUser().setUserName(name);
            CompletionListener listener = new CompletionListener() {
                @Override
                public void onComplete(DbError error) {
                    if (error != DbError.NONE) {
                        Log.e("EditProfileActivity", "could not access to database");
                    }
                }
            };
            Database.getInstance().write("/users/" + user.getUserId(), "userName", name, listener);
        }

        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(MainMenuActivity.EXTRA_USER, user);
        startActivity(intent);
    }

}
