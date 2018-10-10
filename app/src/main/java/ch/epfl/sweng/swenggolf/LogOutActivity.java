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

    private TextView mail;
    private TextView name;
    private TextView uid;
    private ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ch.epfl.sweng.swenggolf.R.layout.activity_log_out);
        FirebaseUser fu = (FirebaseAuth.getInstance()).getCurrentUser();

        name = findViewById(ch.epfl.sweng.swenggolf.R.id.name);
        mail = findViewById(ch.epfl.sweng.swenggolf.R.id.mail);
        uid = findViewById(ch.epfl.sweng.swenggolf.R.id.uid);
        photo = findViewById(ch.epfl.sweng.swenggolf.R.id.photo);
        if(null == fu){ quitLogOut();}
        else{ displayInformation(new User(fu));}
    }

    /**
     * Launches the MainMenuActivity.
     *
     * @param view the current view
     */
    public void clickToQuit(View view){
        quitLogOut();
    }

    /**
     * Launches the MainMenuActivity.
     */
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

