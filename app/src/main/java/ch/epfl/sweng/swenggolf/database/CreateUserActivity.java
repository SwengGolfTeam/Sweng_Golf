package ch.epfl.sweng.swenggolf.database;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import ch.epfl.sweng.swenggolf.Database;
import ch.epfl.sweng.swenggolf.TestMode;
import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.UserLocal;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;


public class CreateUserActivity extends AppCompatActivity {

    private EditText mail;
    private EditText name;
    private ImageView photo;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ch.epfl.sweng.swenggolf.R.layout.activity_create_user);
        name = findViewById(ch.epfl.sweng.swenggolf.R.id.name);
        mail = findViewById(ch.epfl.sweng.swenggolf.R.id.mail);
        photo = findViewById(ch.epfl.sweng.swenggolf.R.id.photo);
        user = TestMode.getUser();
        if(null == user){ quit();}
        else{
            displayInformation(user);
        }
    }


    /**
     * Launches the MainMenuActivity.
     */
    private void quit(){
        startActivity(new Intent(CreateUserActivity.this, MainMenuActivity.class));
    }

    /**
     * Display all the informations of a localUser.
     * @param user The localUser
     */
    private void displayInformation(User user){
        name.setText(user.getUserName());
        mail.setText(user.getEmail());
        if(!TestMode.isTest()) {
            Picasso.with(this).load(user.getPhoto()).into(photo);
        }
    }




    public void onClick(View view) {
        String userName = name.getText().toString();
        String userMail = mail.getText().toString();

        //handle the exception if the EditText fields are null
        if(!userName.isEmpty() && !userMail.isEmpty()){
            Database d = TestMode.getDatabase();
            d.addUser(UserLocal.userChanged(user, userName, userMail));
            quit();
        }
        else{
            //TODO Handle this case
            onClick(view);
        }
    }
}

