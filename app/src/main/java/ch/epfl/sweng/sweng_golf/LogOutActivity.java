package ch.epfl.sweng.sweng_golf;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


public class LogOutActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    TextView mail;
    TextView name;
    TextView uid;
    ImageView photo;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);

        Button button = findViewById(R.id.logout);
        mAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.name);
        mail = findViewById(R.id.mail);
        uid = findViewById(R.id.uid);
        photo = findViewById(R.id.photo);
        displayInformation(mAuth.getCurrentUser());
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Toast.makeText(LogOutActivity.this, "Goodbye", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LogOutActivity.this, SignInActivity.class));
                }
            }
        };
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });
    }

    /**
     * Display all the informations of a user
     * @param user The user
     */
    private void displayInformation(FirebaseUser user){
        name.setText(user.getDisplayName());
        mail.setText(user.getEmail());
        uid.setText(user.getUid());
        Picasso.with(this).load(user.getPhotoUrl()).into(photo);
    }
}

