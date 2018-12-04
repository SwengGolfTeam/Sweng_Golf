package ch.epfl.sweng.swenggolf.network;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.FrameLayout;

import ch.epfl.sweng.swenggolf.R;

/**
 * Class that monitors the Internet connection of the App.
 */
public class Network {
    private static boolean isConnected = true;

    /**
     * Hidden constructor.
     */
    private Network(){}

    /**
     * Checks with the System (Android) if there is an active Internet connection.
     * @param context The context of the application
     */
    public static void updateStatus(Context context){
        ConnectivityManager cm = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
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
        View rootView = ((Activity)context).getWindow().getDecorView()
                .findViewById(android.R.id.content);
        FrameLayout banner = rootView.findViewById(R.id.banner);

        if(isConnected){
            banner.setVisibility(View.GONE);
        } else {
            banner.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Displays the Dialog if no internet connection.
     * @param context The context of the application
     */
    public static void checkAndDialog(Context context){
        if(!getStatus()){
            showGoToSettingsDialog(context);
        }
    }

    /**
     * Display the Alert Dialog for the redirection to settings.
     */
    private static void showGoToSettingsDialog(final Context context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        dialogBuilder.setTitle("No Internet Connection")
                .setMessage("Do you want to go to the settings to enable it?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent wifiIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        context.startActivity(wifiIntent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // user cancelled the dialog
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        Dialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
