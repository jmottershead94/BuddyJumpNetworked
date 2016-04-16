package com.example.app.jason.ragerelease.app.Framework.Network;

import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;

import com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi.WiFiHandler;

/**
 * Created by Jason Mottershead on 04/04/2016.
 */

public class ConnectionManagement
{
    // Attributes.
    private WiFiHandler wifiHandler = null;
    private NetworkActivity currentActivity = null;

    // Methods.
    public ConnectionManagement(WifiP2pManager p2pManager, WifiManager manager, Channel channel, NetworkActivity activity)
    {
        currentActivity = activity;

        wifiHandler = new WiFiHandler(p2pManager, manager, channel, activity);
    }

    // Getters.
    // This will return our current wifi handler.
    public WiFiHandler getWifiHandler() { return wifiHandler; }

    // This will return our current network activity.
    public NetworkActivity getNetworkActivity() { return currentActivity; }

    // Setters.
    // This will set our new network activity.
    public void setNetworkActivity(NetworkActivity activity) { currentActivity = activity; }
}
