package ch.epfl.sweng.swenggolf.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;


public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean status = Network.getStatus(context);
        Log.d("NETWORK CHANGE", String.valueOf(status));

        Network.setMessageVisibility(context, status);
    }

    // Needed for API>=24 because receiver in AndroidManifest.xml is deprecated if not registered
    // See https://developer.android.com/training/monitoring-device-state/connectivity-monitoring#java
    public static void registerReceiver(Context context, BroadcastReceiver broadcastReceiver){
        context.registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
}
