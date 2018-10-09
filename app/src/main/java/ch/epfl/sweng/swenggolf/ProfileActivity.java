package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        userID = intent.getStringExtra(MainActivity.EXTRA_USERID);
        DummyUser user = MainActivity.userDatabase.get(userID);

        final TextView name = findViewById(R.id.name); // is the final useful? (found on an example on Android website)
        name.setText(user.getDisplayName());
        final TextView tvUsername = findViewById(R.id.username);
        String username = DummyUser.accessTable(user.getUid(), "username");
        if (username != null && !username.isEmpty()) {
            String usernameString = "@" + username;
            tvUsername.setText(usernameString);
        }
        final TextView offers_posted = findViewById(R.id.offers1);
        offers_posted.setText(DummyUser.accessTable(user.getUid(), "offers_posted"));
        final TextView offers_answered = findViewById(R.id.offers2);
        offers_answered.setText(DummyUser.accessTable(user.getUid(), "offers_answered"));
    }

    public void editProfile(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra(MainActivity.EXTRA_USERID, userID);
        startActivity(intent);
    }
}
