package ch.epfl.sweng.swenggolf.database;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.swenggolf.Database;
import ch.epfl.sweng.swenggolf.DatabaseFirebase;
import ch.epfl.sweng.swenggolf.TestMode;
import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.UserFirebase;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;

public class WaitingActivity extends AppCompatActivity {

    User user;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ch.epfl.sweng.swenggolf.R.layout.activity_waiting);
        user = TestMode.getUser();
        myRef = TestMode.getRef();
        isExisting();
    }



    private void goToMainMenu(){
        startActivity(new Intent(WaitingActivity.this, MainMenuActivity.class));
    }

    private void goToCreate(){
        startActivity(new Intent(WaitingActivity.this, CreateUserActivity.class));
    }

    public void isExisting1(){
        Database d = new DatabaseFirebase(FirebaseDatabase.getInstance().getReference());
        if (d.containsUser(user)){
            goToMainMenu();
        }
        else {
            System.out.println("Hello2");
            goToCreate();
        }
    }

    public void isExisting(){

        DatabaseReference userNameRef = myRef.child("users").child(user.getUserId());
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    goToCreate();
                } else {
                    goToMainMenu();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        userNameRef.addListenerForSingleValueEvent(eventListener);
    }
}
