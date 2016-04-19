// The package location of this class.
package com.example.app.jason.ragerelease.app.Framework.Network;

// All of the extra includes here.
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;

import com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi.WiFiHandler;

/**
 * Created by Jason Mottershead on 04/04/2016.
 */

// This class will be responsible for handling all of our connection data.
public class ConnectionManagement
{
    // Attributes.
    // Private.
    private WiFiHandler wifiHandler = null;             // This will store and handle wifi data.
    private NetworkActivity currentActivity = null;     // This will store our network activity reference.

    // Methods.
    //////////////////////////////////////////////////
    //                  Constructor                 //
    //==============================================//
    // This will initialise our attributes, and set //
    // up the wifi handler.                         //
    //////////////////////////////////////////////////
    public ConnectionManagement(WifiP2pManager p2pManager, WifiManager manager, Channel channel, NetworkActivity activity)
    {
        // Initialising our attributes.
        currentActivity = activity;
        wifiHandler = new WiFiHandler(p2pManager, manager, channel, activity);
    }

    // Getters.
    // This will return our current wifi handler instance.
    public WiFiHandler getWifiHandler()                      { return wifiHandler; }

    // This will return our current network activity instance.
    public NetworkActivity getNetworkActivity()              { return currentActivity; }
}
