package ch.epfl.sweng.swenggolf.network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.FrameLayout;

import ch.epfl.sweng.swenggolf.R;

/**
 * Class that monitors the Internet connection of the App.
 */
public class Network {
    private static boolean isConnected = false;

    /**
     * Checks with the System (Android) if there is an active Internet connection.
     * @param context The context of the application
     */
    public static void updateStatus(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
    }

    /**
     * Tells if there is a Network connection.
     * @return true if yes, false otherwise
     */
    public static boolean getStatus(){
        return isConnected;
    }

    /**
     * Displays/hides the message depending on the status of the Internet connection.
     * @param context The context in which the message is
     */
    public static void setMessageVisibility(Context context){
        View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        FrameLayout banner = rootView.findViewById(R.id.banner);

        if(isConnected){
            banner.setVisibility(View.GONE);
        } else {
            banner.setVisibility(View.VISIBLE);
        }
    }
}
