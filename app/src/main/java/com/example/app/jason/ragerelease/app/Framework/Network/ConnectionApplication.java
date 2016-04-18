package com.example.app.jason.ragerelease.app.Framework.Network;

import android.app.Application;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;

import com.example.app.jason.ragerelease.app.Framework.Player;

/**
 * Created by Jason Mottershead on 04/04/2016.
 */

// Connection application IS AN application, therefore extends from it.
// This class should hopefully allow us to keep/maintain a WiFi connection between
// different activities.
public class ConnectionApplication extends Application
{
    // Attributes.
    private static ConnectionApplication connectionApplication = null;
    private ConnectionManagement connectionManagement = null;

    // Methods.
    @Override
    public void onCreate()
    {
        super.onCreate();
        connectionApplication = this;
    }

    // This will return.
    public static ConnectionApplication getConnectionApplication() { return connectionApplication; }

    // Setters.
    public void setConnectionManagement(WifiP2pManager p2pManager, WifiManager manager, Channel channel, NetworkActivity activity)
    {
        connectionManagement = new ConnectionManagement(p2pManager, manager, channel, activity);
    }

    // Getters.
    // This will return our current instance of connection management.
    public ConnectionManagement getConnectionManagement() { return connectionManagement; }

    // This will return our server image index.
    public int getServerPeerIndexImage() { return connectionManagement.getWifiHandler().getWifiP2PBroadcastReceiver().getServerTask().getPeerImageIndexInt(); }

    // This will return our client image index.
    public int getClientPeerIndexImage() { return connectionManagement.getWifiHandler().getWifiP2PBroadcastReceiver().getClientTask().getPeerImageIndexInt(); }

    public void setServerPlayer(Player gamePlayer)
    {
        connectionManagement.getWifiHandler().getWifiP2PBroadcastReceiver().getServerTask().setPlayer(gamePlayer);
    }

    public void setClientPlayer(Player gamePlayer)
    {
        connectionManagement.getWifiHandler().getWifiP2PBroadcastReceiver().getClientTask().setPlayer(gamePlayer);
    }

}
