package ch.epfl.sweng.sweng_golf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loadOfferActivity(View view){
        Intent intent = new Intent(this, CreateOfferActivity.class);
        // TODO implement username when login effective
        intent.putExtra("username","God");
        startActivity(intent);
    }

}