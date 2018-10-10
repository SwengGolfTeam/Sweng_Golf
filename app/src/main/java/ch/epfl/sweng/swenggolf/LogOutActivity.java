package ch.epfl.sweng.swenggolf;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
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
        name = findViewById(ch.epfl.sweng.swenggolf.R.id.name);
        mail = findViewById(ch.epfl.sweng.swenggolf.R.id.mail);
        uid = findViewById(ch.epfl.sweng.swenggolf.R.id.uid);
        photo = findViewById(ch.epfl.sweng.swenggolf.R.id.photo);
        User user;
        if(TestMode.isTEST()){
            user = TestMode.getUser();
        }
        else {
            user = new UserFirebase(FirebaseAuth.getInstance().getCurrentUser());
        }
        if(null == user){ quitLogOut();}
        else{
            displayInformation(user);
        }
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
     * Display all the informations of a localUser.
     * @param user The localUser
     */
    private void displayInformation(User user){
        name.setText(user.getUserName());
        mail.setText(user.getEmail());
        uid.setText(user.getUserId());
        if(!TestMode.isTEST()) {
            Picasso.with(this).load(user.getPhoto()).into(photo);
        }
    }
}

