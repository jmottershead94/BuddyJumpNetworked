// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework.Network;

// All of the extra includes here.
import android.app.Application;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;

import com.example.app.jason.ragerelease.app.Framework.Player;

/**
 * Created by Jason Mottershead on 04/04/2016.
 */

// Connection application IS AN application, therefore extends from it.
// This class should allow us to keep/maintain a WiFi connection between different activities.
public class ConnectionApplication extends Application
{
    // Attributes.
    // Private.
    private ConnectionManagement connectionManagement = null;           // This will manage our connections and should keep values between activities.

    // Methods.
    // Setters.
    // This will set up our connection manager.
    public void setConnectionManagement(WifiP2pManager p2pManager, WifiManager manager, Channel channel, NetworkActivity activity)  { connectionManagement = new ConnectionManagement(p2pManager, manager, channel, activity); }

    // This will set up our server player instance.
    // Which will be used in order to track player tapping.
    public void setServerPlayer(Player gamePlayer)                                                                                  { connectionManagement.getWifiHandler().getWifiP2PBroadcastReceiver().getServerTask().setPlayer(gamePlayer); }

    // This will set up our client player instance.
    // Which will be used in order to track the player tapping.
    public void setClientPlayer(Player gamePlayer)                                                                                  { connectionManagement.getWifiHandler().getWifiP2PBroadcastReceiver().getClientTask().setPlayer(gamePlayer); }

    // Getters.
    // This will return our connection management instance.
    public ConnectionManagement getConnectionManagement()   { return connectionManagement; }

    // This will return our server image index.
    public int getServerPeerIndexImage()                    { return connectionManagement.getWifiHandler().getWifiP2PBroadcastReceiver().getServerTask().getPeerImageIndexInt(); }

    // This will return our client image index.
    public int getClientPeerIndexImage()                    { return connectionManagement.getWifiHandler().getWifiP2PBroadcastReceiver().getClientTask().getPeerImageIndexInt(); }



}
