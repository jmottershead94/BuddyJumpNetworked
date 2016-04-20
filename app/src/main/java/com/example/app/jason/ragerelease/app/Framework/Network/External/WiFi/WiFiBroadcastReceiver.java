// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi;

// All of the extra includes here.
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;

import com.example.app.jason.ragerelease.app.Framework.Network.External.NetworkBroadcastReceiver;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;
import com.example.app.jason.ragerelease.app.GameStates.Multiplayer.ConnectionSelection;

/**
 * Created by Jason Mottershead on 10/04/2016.
 */

// Wifi broadcast receiver IS A network broadcast receiver, therefore inherits from it.
// This will be responsible for handling all wifi connection notifications.
public class WiFiBroadcastReceiver extends NetworkBroadcastReceiver
{
    // Methods.
    //////////////////////////////////////////////////
    //                  Constructor                 //
    //==============================================//
    // This will set up the network broadcast       //
    // receiver for wifi connections.               //
    //////////////////////////////////////////////////
    public WiFiBroadcastReceiver(WiFiHandler wiFi, WifiManager manager, WifiP2pManager p2pManager, Channel channel, NetworkActivity activity)
    {
        super(manager, p2pManager, channel, activity);

        // Initialising our attribute.
        wiFiHandler = wiFi;
    }

    //////////////////////////////////////////////////
    //                  On Receive                  //
    //==============================================//
    // This will be used to check if our wifi       //
    // connection is on or off.                     //
    //////////////////////////////////////////////////
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Get our current action in the activity.
        String action = intent.getAction();

        // If our wifi connection has changed state (if it is on or off).
        if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action))
        {
            // Obtain the current wifi state.
            int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);

            // If we have wifi turned on.
            if(state == WifiManager.WIFI_STATE_ENABLED)
            {
                // Wifi is enabled.

            }
            // Otherwise, wifi is turned off.
            else
            {
                // Wifi is disabled.
                // Turn on the connection again?
                // wifiManager.setWifiEnabled(true);
            }
        }
    }



}
