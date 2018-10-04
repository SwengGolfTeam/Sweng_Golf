package ch.epfl.sweng.sweng_golf;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public abstract class GoogleToolBox {
    public static GoogleApiClient setUp(Context currentContext){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();

        return new GoogleApiClient.Builder(currentContext)
                .enableAutoManage((FragmentActivity) currentContext, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        //TODO Manage this case
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
    }
}
