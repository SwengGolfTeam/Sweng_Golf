package ch.epfl.sweng.swenggolf.profile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.swenggolf.database.DatabaseConnection;
import ch.epfl.sweng.swenggolf.database.FakeUserDatabase;
import ch.epfl.sweng.swenggolf.main.MainActivity;
import ch.epfl.sweng.swenggolf.R;

public class ProfileActivity extends AppCompatActivity {

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        uid = getIntent().getStringExtra(MainActivity.EXTRA_USERID);

        Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);

        prepareProfileData();
        displayUserData(null);
    }

    private void prepareProfileData() {
        DatabaseConnection db = DatabaseConnection.getInstance();
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> userData = new HashMap<>();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    userData.put(d.getKey(), d.getValue(String.class));
                }
                displayUserData(userData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DBERR", "databaseError:" + databaseError.getMessage());
            }
        };
        db.readObject("users", uid, listener);
        
    }

    private void displayUserData(Map<String, String> userData) {
        if (userData != null) {
            TextView name = findViewById(R.id.name);
            name.setText(userData.get("username"));
            // TODO add a username ?
        /*TextView textView = findViewById(R.id.username);
        String username = FakeUserDatabase.accessTable(uid, "username");
        if (username != null && !username.isEmpty()) {
            String usernameString = "@" + username;
            textView.setText(usernameString);
        }*/
            // TODO count the number of offers posted+answered
        /*TextView offersPosted = findViewById(R.id.offers1);
        offersPosted.setText(FakeUserDatabase.accessTable(uid, "offers_posted"));
        TextView offersAnswered = findViewById(R.id.offers2);
        offersAnswered.setText(FakeUserDatabase.accessTable(uid, "offers_answered"));*/
        }

    }

    /**
     * Launches the EditProfileActivity.
     * @param view the current view
     */
    public void editProfile(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra(MainActivity.EXTRA_USERID, uid);
        startActivity(intent);
    }
}
