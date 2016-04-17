// The package location of this class.
package com.example.app.jason.ragerelease.app.Framework.Network.External;

// All of the extra includes here.
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;

import com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi.WiFiClientTasks;
import com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi.WiFiServerTasks;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;

/**
 * Created by Jason Mottershead on 10/04/2016.
 */

// Network broadcast receiver IS A broadcast receiver, therefore inherits from it.
public class NetworkBroadcastReceiver extends BroadcastReceiver
{
    // Attributes.
    protected WifiManager wifiManager = null;
    protected WifiP2pManager wifiP2pManager = null;
    protected Channel wifiChannel = null;
    protected NetworkActivity currentActivity = null;
    protected WiFiServerTasks wifiServerTasks = null;
    protected WiFiClientTasks wifiClientTasks = null;

    // Methods.
    public NetworkBroadcastReceiver(WifiManager manager, WifiP2pManager p2pManager, Channel channel, NetworkActivity activity)
    {
        wifiManager = manager;
        wifiP2pManager = p2pManager;
        wifiChannel = channel;
        currentActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {};
}
