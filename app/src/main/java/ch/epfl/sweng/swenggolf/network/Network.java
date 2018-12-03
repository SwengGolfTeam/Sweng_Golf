package ch.epfl.sweng.swenggolf.network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.FrameLayout;

import ch.epfl.sweng.swenggolf.R;


public class Network {
    public static boolean getStatus(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());

        return isConnected;
    }

    public static void setMessageVisibility(Context context, boolean connected){
        View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        FrameLayout banner = rootView.findViewById(R.id.banner);

        if(connected){
            banner.setVisibility(View.GONE);
        } else {
            banner.setVisibility(View.VISIBLE);
        }
    }


}
