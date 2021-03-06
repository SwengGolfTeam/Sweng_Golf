package ch.epfl.sweng.swenggolf.database;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.network.Network;

/**
 * Sign-in Activity where the user can log in his
 * google account in order to access to the application.
 * (https://firebase.google.com/docs/auth/android/google-signin)
 */
public class SignInActivity extends AppCompatActivity {

    /*(Random) Number linked with the Sign in process*/
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth = null;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ch.epfl.sweng.swenggolf.R.layout.activity_sign_in);

        if (Config.isTest()) {
            goToLogOut();
        }

        /*Button used to sign in*/
        SignInButton button = findViewById(ch.epfl.sweng.swenggolf.R.id.sign_in_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Network.updateStatus(SignInActivity.this);
                Network.checkAndDialog(SignInActivity.this);
                if (mAuth == null & Network.getStatus()) {
                    mAuth = FirebaseAuth.getInstance();
                    initializeMAuthListener();
                    mAuth.addAuthStateListener(mAuthListener);
                    signIn();
                }
            }
        });

    }


    /**
     * initialize mAuthListener.
     **/
    private void initializeMAuthListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    goToWait();
                }
            }
        };
    }


    /**
     * Start the logoutActivity.
     */
    private void goToLogOut() {
        startActivity(new Intent(SignInActivity.this, CreateUserActivity.class));
    }

    /**
     * Start the logoutActivity.
     */
    private void goToWait() {
        startActivity(new Intent(SignInActivity.this, WaitingActivity.class));
    }

    /**
     * Create and initialize a Google Sign in Client.
     *
     * @return The corresponding google client
     */
    private GoogleSignInClient setGoogleSignInClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("515318437233-vsola1ge"
                + "0locvd4ft2jsc9i5esma30ro.apps.googleusercontent.com").requestEmail().build();

        return GoogleSignIn.getClient(this, gso);
    }

    /**
     * Launch the google signing in display.
     */
    private void signIn() {
        Intent signInIntent = setGoogleSignInClient().getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert null != account;
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(SignInActivity.this,
                        "Authentification went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Make an authentification in firebase with google account.
     *
     * @param acct The google account
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this,
                                    "Welcome", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignInActivity.this,
                                    "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

