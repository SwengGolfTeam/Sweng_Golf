package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import static android.view.Window.FEATURE_ACTION_BAR;
import static android.view.Window.FEATURE_CUSTOM_TITLE;

public class ProfileActivity extends AppCompatActivity {

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window currentWindow = getWindow();
        currentWindow.requestFeature(FEATURE_ACTION_BAR); //doesn't seem to work

        setContentView(R.layout.activity_profile);

        userID = getIntent().getStringExtra(MainActivity.EXTRA_USERID);

        TextView name = findViewById(R.id.name);
        name.setText(FakeUserDatabase.accessTable(userID, "name"));
        TextView textView = findViewById(R.id.username);
        String username = FakeUserDatabase.accessTable(userID, "username");
        if (username != null && !username.isEmpty()) {
            String usernameString = "@" + username;
            textView.setText(usernameString);
        }
        TextView offersPosted = findViewById(R.id.offers1);
        offersPosted.setText(FakeUserDatabase.accessTable(userID, "offers_posted"));
        TextView offersAnswered = findViewById(R.id.offers2);
        offersAnswered.setText(FakeUserDatabase.accessTable(userID, "offers_answered"));
    }

    public void editProfile(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra(MainActivity.EXTRA_USERID, userID);
        startActivity(intent);
    }
}
