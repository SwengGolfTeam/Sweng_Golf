package ch.epfl.sweng.swenggolf.profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import ch.epfl.sweng.swenggolf.database.FakeUserDatabase;
import ch.epfl.sweng.swenggolf.main.MainActivity;
import ch.epfl.sweng.swenggolf.R;


public class EditProfileActivity extends AppCompatActivity {
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        uid = intent.getStringExtra(MainActivity.EXTRA_USERID);

        EditText editText = findViewById(R.id.edit_username);
        String username = FakeUserDatabase.accessTable(uid, "username");
        if (username != null) {
            editText.setText(username);
            editText.setSelection(username.length());
        }

    }

    /**
     * Saves the changes and returns to the profile activity.
     *
     * @param view the current view
     */
    public void saveChangesAndReturn(View view) {
        EditText editText = findViewById(R.id.edit_username);
        String username = editText.getText().toString();
        FakeUserDatabase.setUsername(uid, username);

        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(MainActivity.EXTRA_USERID, uid);
        startActivity(intent);
    }
}
