package ch.epfl.sweng.sweng_golf;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView emailTextView;

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUp();
        nameTextView =  findViewById(R.id.nameTextView);
        emailTextView =  findViewById(R.id.emailTextView);
        Button logout = findViewById(R.id.log_out);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut(v);
            }
        });
    }

    /**
     * Method to set up the Google API client
     */
    private void setUp(){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        //TODO Manage this case
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
    }


    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> optionalPendingResult = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (optionalPendingResult.isDone()) {
            GoogleSignInResult result = optionalPendingResult.get();
            resultHandler(result);
        } else {
            optionalPendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    resultHandler(googleSignInResult);
                }
            });
        }
    }

    private void resultHandler(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            assert account != null;
            nameTextView.setText(account.getDisplayName());
            emailTextView.setText(account.getEmail());
        } else {
            logOut();
        }
    }

    private void logOut() {
        startActivity(new Intent(this, SignIn.class));
    }

    public void logOut(View view) {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    logOut();
                }
                //TODO
            }
        });
    }


}