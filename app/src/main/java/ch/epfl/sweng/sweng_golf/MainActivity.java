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
    /* Personnal Id of the google account
    *   We will use him to know who is connected
    * */
    private TextView googleId;
    /* User name of the google account*/
    private TextView googleName;
    /* mail of the google account*/
    private TextView gogleMail;

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUp();
        googleId = findViewById(R.id.id);
        googleName =  findViewById(R.id.name);
        gogleMail =  findViewById(R.id.email);
        Button logout = findViewById(R.id.log_out);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignOut();
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
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
    }

    /*At the launching of the apllication*/
    @Override
    protected void onStart() {
        super.onStart();
        /*Check for an immediate result with isDone(); or set a callback to handle asynchronous results.*/
        OptionalPendingResult<GoogleSignInResult> optionalPendingResult = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        /*if it is already done */
        if (optionalPendingResult.isDone()) {
            GoogleSignInResult result = optionalPendingResult.get();
            resultHandler(result);
        } else {
            /* If we have to wait*/
            optionalPendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    resultHandler(googleSignInResult);
                }
            });
        }
    }

    /**
     * Handle the result by display google account's information on screen
     * @param result the result of the signin attempt
     */
    private void resultHandler(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            assert account != null;
            googleId.setText(account.getId());
            googleName.setText(account.getDisplayName());
            gogleMail.setText(account.getEmail());
        } else {
            goToLogin();
        }
    }

    /**
     * Go to the login page
     * */
    private void goToLogin() {
        startActivity(new Intent(this, SignIn.class));
    }

    /**
     * Signout of the google account
     */
    public void SignOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goToLogin();
                }
                //TODO
            }
        });
    }


}