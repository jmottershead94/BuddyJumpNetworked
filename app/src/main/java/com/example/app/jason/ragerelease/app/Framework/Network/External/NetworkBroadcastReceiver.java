// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework.Network.External;

// All of the extra includes here.
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Handler;
import android.os.Message;

import com.example.app.jason.ragerelease.app.Framework.Debug.DebugInformation;
import com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi.WiFiClientTasks;
import com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi.WiFiConstants;
import com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi.WiFiHandler;
import com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi.WiFiServerTasks;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;
import com.example.app.jason.ragerelease.app.GameStates.Multiplayer.ConnectionSelection;

/**
 * Created by Jason Mottershead on 10/04/2016.
 */

// Network broadcast receiver IS A broadcast receiver, therefore inherits from it.
// This will be responsible for handling all of the generic network broadcast functions.
public class NetworkBroadcastReceiver extends BroadcastReceiver
{
    // Attributes.
    // Protected.
    protected WifiManager wifiManager = null;           // This will provide access to the wifi manager.
    protected WifiP2pManager wifiP2pManager = null;     // This will provide access to the wifi p2p manager.
    protected Channel wifiChannel = null;               // This will provide access to our wifi channel.
    protected NetworkActivity currentActivity = null;   // This will provide access to our current network activity.
    protected WiFiHandler wiFiHandler = null;           // This will provide access to our wifi handler.
    protected WiFiServerTasks wifiServerTasks = null;   // This will provide access to the wifi server tasks.
    protected WiFiClientTasks wifiClientTasks = null;   // This will provide access to the wifi client tasks.

    // Methods.
    //////////////////////////////////////////////////
    //                  Constructor                 //
    //==============================================//
    // This will initialise the network broadcast   //
    // attributes.                                  //
    //////////////////////////////////////////////////
    public NetworkBroadcastReceiver(WifiManager manager, WifiP2pManager p2pManager, Channel channel, NetworkActivity activity)
    {
        // Initialising our network broadcast attributes.
        wifiManager = manager;
        wifiP2pManager = p2pManager;
        wifiChannel = channel;
        currentActivity = activity;
    }

    //////////////////////////////////////////////////
    //                  On Receive                  //
    //==============================================//
    // This will be overridden in the other         //
    // broadcast receivers.                         //
    //////////////////////////////////////////////////
    @Override
    public void onReceive(Context context, Intent intent) {};
}
