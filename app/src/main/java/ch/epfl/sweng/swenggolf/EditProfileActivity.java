package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditProfileActivity extends AppCompatActivity {
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent(); // doesn't work if we just get back from edit profile
        userID = intent.getStringExtra(MainActivity.EXTRA_USERID);

        EditText editText = findViewById(R.id.edit_username);
        String username = FakeUserDatabase.accessTable(userID, "username");
        if (username != null) {
            editText.setText(username);
            editText.setSelection(username.length());
        }

    }

    public void saveChangesAndReturn(View view) {
        EditText editText = findViewById(R.id.edit_username);
        String username = editText.getText().toString();
        FakeUserDatabase.setUsername(userID, username);

        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(MainActivity.EXTRA_USERID, userID);
        startActivity(intent);
    }
}
