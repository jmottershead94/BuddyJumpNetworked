package com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;

import com.example.app.jason.ragerelease.app.Framework.Debug.DebugInformation;
import com.example.app.jason.ragerelease.app.Framework.Network.External.NetworkBroadcastReceiver;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;

/**
 * Created by Jason Mottershead on 10/04/2016.
 */

public class WiFiBroadcastReceiver extends NetworkBroadcastReceiver
{
    // Attributes.


    // Methods.
    public WiFiBroadcastReceiver(WifiManager manager, WifiP2pManager p2pManager, Channel channel, NetworkActivity activity)
    {
        super(manager, p2pManager, channel, activity);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Get our current action in the activity.
        String action = intent.getAction();

        if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action))
        {
            // Obtain the current wifi state.
            int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);

            // If we have wifi turned on.
            if(state == WifiManager.WIFI_STATE_ENABLED)
            {
                // Wifi is enabled.
                DebugInformation.displayShortToastMessage(currentActivity, "Wifi is enabled");
            }
            // Otherwise, wifi is turned off.
            else
            {
                // Wifi is disabled.
                DebugInformation.displayShortToastMessage(currentActivity, "Wifi is not enabled");
            }
        }
    }

}
