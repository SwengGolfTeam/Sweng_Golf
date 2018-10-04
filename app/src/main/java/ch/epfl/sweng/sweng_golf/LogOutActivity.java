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
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        TextView name = findViewById(R.id.name);

        name.setText(user.getDisplayName());

        TextView mail = findViewById(R.id.mail);
        mail.setText(user.getEmail());

        TextView uid = findViewById(R.id.uid);
        uid.setText(user.getUid());

        ImageView photo = findViewById(R.id.photo);
        Picasso.with(this).load(user.getPhotoUrl()).into(photo);

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

}

