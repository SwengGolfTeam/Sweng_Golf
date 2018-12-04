package ch.epfl.sweng.swenggolf.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

/**
 * A Receiver that gets called by the System (Android) if there is a change with the Internet
 * connection.
 */
public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Network.updateStatus(context);
        Log.d("NETWORK CHANGE", String.valueOf(Network.getStatus()));
        Network.setMessageVisibility(context);
    }


    /**
     * Registers the Receiver at runtime, needed for API>=24 (can not do it in AndroidManifest.xml).
     * See https://developer.android.com/training/monitoring-device-state/connectivity-monitoring
     * @param context The Context from where it is registered
     * @param broadcastReceiver The Receiver to register
     */
    public static void registerReceiver(Context context, BroadcastReceiver broadcastReceiver){
        context.registerReceiver(broadcastReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        Network.updateStatus(context);
    }
}
