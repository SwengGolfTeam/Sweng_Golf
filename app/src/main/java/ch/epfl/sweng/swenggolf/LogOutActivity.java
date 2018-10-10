package ch.epfl.sweng.swenggolf;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


public class LogOutActivity extends AppCompatActivity {

    /*private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;*/
    private TextView mail;
    private TextView name;
    private TextView uid;
    private ImageView photo;


    /*@Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ch.epfl.sweng.swenggolf.R.layout.activity_log_out);
        FirebaseUser fu = (/*mAuth = */FirebaseAuth.getInstance()).getCurrentUser();

       /* mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Toast.makeText(LogOutActivity.this, "Goodbye", Toast.LENGTH_SHORT).show();
                    quitLogOut();
                }
            }
        };*/
        Button button = findViewById(R.id.go_to_main_menu);
        name = findViewById(ch.epfl.sweng.swenggolf.R.id.name);
        mail = findViewById(ch.epfl.sweng.swenggolf.R.id.mail);
        uid = findViewById(ch.epfl.sweng.swenggolf.R.id.uid);
        photo = findViewById(ch.epfl.sweng.swenggolf.R.id.photo);
        if(null == fu){ quitLogOut();}
        else{ displayInformation(new User(fu));}
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*mAuth.signOut();*/
                quitLogOut();
            }
        });
    }
    private void quitLogOut(){
        startActivity(new Intent(LogOutActivity.this, MainActivity.class));
    }
    /**
     * Display all the informations of a user
     * @param user The user
     */
    private void displayInformation(User user){
        name.setText(user.getName());
        mail.setText(user.getMail());
        uid.setText(user.getUid());
        Picasso.with(this).load(user.getPhoto()).into(photo);
    }
}

