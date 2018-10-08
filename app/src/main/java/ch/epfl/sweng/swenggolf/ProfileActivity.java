package ch.epfl.sweng.swenggolf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    DummyUser user = new DummyUser("Hervé Bogoss", "1234");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DummyUser.setUsername(user.getUid(), "God"); // should be done somewhere else

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final TextView name = findViewById(R.id.name); // is the final useful? (found on an example on Android website)
        name.setText(user.getDisplayName());
        final TextView tvUsername = findViewById(R.id.username);
        String username = DummyUser.accessTable(user.getUid(), "username");
        if (username != null) {
            String usernameString = "@" + username;
            tvUsername.setText(usernameString);
        }
        final TextView offers_posted = findViewById(R.id.offers1);
        offers_posted.setText(DummyUser.accessTable(user.getUid(), "offers_posted"));
        final TextView offers_answered = findViewById(R.id.offers2);
        offers_answered.setText(DummyUser.accessTable(user.getUid(), "offers_answered"));
    }
}
