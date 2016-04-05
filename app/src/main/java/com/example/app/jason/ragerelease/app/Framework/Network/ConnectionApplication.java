package com.example.app.jason.ragerelease.app.Framework.Network;

import android.app.Application;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;

/**
 * Created by Jason Mottershead on 04/04/2016.
 */

// Connection application IS AN application, therefore extends from it.
// This class should hopefully allow us to keep/maintain a WiFi connection between
// different activities.
public class ConnectionApplication extends Application
{
    // Attributes.
    private ConnectionManagement connectionManagement = null;

    // Methods.
    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    // Setters.
    public void setConnectionManagement(WifiP2pManager p2pManager, WifiManager manager, Channel channel, NetworkActivity activity)
    {
        connectionManagement = new ConnectionManagement(p2pManager, manager, channel, activity);
    }

    // Getters.
    // This will return our current instance of connection management.
    public ConnectionManagement getConnectionManagement() { return connectionManagement; }
}
