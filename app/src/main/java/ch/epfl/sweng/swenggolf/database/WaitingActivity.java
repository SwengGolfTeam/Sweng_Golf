package ch.epfl.sweng.swenggolf.database;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        user = new UserFirebase(FirebaseAuth.getInstance().getCurrentUser());
        myRef = FirebaseDatabase.getInstance().getReference();

        isExisting();
    }


    private void goToMainMenu(){
        startActivity(new Intent(WaitingActivity.this, MainMenuActivity.class));
    }

    private void goToCreate(){
        startActivity(new Intent(WaitingActivity.this, CreateUserActivity.class));
    }


    public void isExisting(){
        FirebaseDatabase.getInstance().getReference().child("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String myUid = user.getUserId();
                        boolean tmp = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (myUid.equals(snapshot.getKey())){
                                tmp = true;
                                break;
                            }
                        }
                        if(tmp){
                            goToMainMenu();
                        }else {
                            goToCreate();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
}
