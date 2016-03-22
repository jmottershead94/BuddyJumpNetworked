// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

/**
 * Created by Jason Mottershead on 22/03/2016.
 */

// This class will handle all of the connection states.
public class ConnectionState
{
    // Methods.
    // Provide information for our current network.
    public static NetworkInfo getNetworkInfo(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    // If there are any connections.
    public static boolean isConnected(Context context)
    {
        NetworkInfo information = ConnectionState.getNetworkInfo(context);
        return (information != null && information.isConnected());
    }

    // If there are any connections to WiFi.
    public static boolean isConnectedWiFi(Context context)
    {
        NetworkInfo information = ConnectionState.getNetworkInfo(context);
        return (information != null && information.isConnected() && information.getType() == ConnectivityManager.TYPE_WIFI);
    }

    // If there are any connections to a mobile network.
    public static boolean isConnectedMobile(Context context)
    {
        NetworkInfo information = ConnectionState.getNetworkInfo(context);
        return (information != null && information.isConnected() && information.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    // If there are any connections to bluetooth.
    public static boolean isConnectedBluetooth(Context context)
    {
        NetworkInfo information = ConnectionState.getNetworkInfo(context);
        return (information != null && information.isConnected() && information.getType() == ConnectivityManager.TYPE_BLUETOOTH);
    }
}
