package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_USERID = "ch.epfl.sweng.swenggolf.USERID";
    // in an attempt to mimic a database (maybe it won't work like this I don't know)
    protected static final Map<String, DummyUser> userDatabase = new HashMap<>();
    private DummyUser user = new DummyUser("Hervé Bogoss", "1234");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userDatabase.put(user.getUid(), user);
    }

    public void openProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(EXTRA_USERID, user.getUid());
        startActivity(intent);
    }
}
