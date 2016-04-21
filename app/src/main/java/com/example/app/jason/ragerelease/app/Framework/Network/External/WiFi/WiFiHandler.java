// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi;

// All of the extra includes here.
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;

import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;

/**
 * Created by Jason Mottershead on 04/04/2016.
 */

// This class will be responsible for handling all of our wifi connections and setting up the means to use wifi.
public class WiFiHandler
{
    // Attributes.
    // Private.
    private IntentFilter wifiP2PFilter = new IntentFilter();            // The intent filter to register actions for wifi P2P.
    private IntentFilter wifiFilter = new IntentFilter();               // The intent filter to register actions for wifi.
    private WiFiP2PBroadcastReceiver wifiP2PBroadcastReceiver = null;   // The wifi P2P broadcast receiver to listen out for wifi P2P state changes.
    private WiFiBroadcastReceiver wifiBroadcastReceiver = null;         // The wifi broadcast receiver to listen our for wifi state changes.
    private WifiManager wifiManager = null;                             // Our wifi manager to be able to turn wifi on or off and check if it is on or off.
    private NetworkActivity currentActivity = null;                     // The current network activity we are in.

    // Methods.
    //////////////////////////////////////////////////
    //                  Constructor                 //
    //==============================================//
    // This will set up means to use wifi.          //
    //////////////////////////////////////////////////
    public WiFiHandler(WifiP2pManager p2pManager, WifiManager manager, Channel channel, NetworkActivity activity)
    {
        // Setting up the filters for the broadcast receiver to listen out for.
        // Wifi filters.
        wifiFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

        // Wifi P2P filters.
        wifiP2PFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        wifiP2PFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        wifiP2PFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        wifiP2PFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        // Initialising our attributes.
        wifiManager = manager;
        currentActivity = activity;
        wifiBroadcastReceiver = new WiFiBroadcastReceiver(this, manager, p2pManager, channel, activity);
        wifiP2PBroadcastReceiver = new WiFiP2PBroadcastReceiver(manager, p2pManager, channel, activity);
    }

    //////////////////////////////////////////////////
    //                  Turn On                     //
    //==============================================//
    // This will allow us to turn on wifi.          //
    //////////////////////////////////////////////////
    public void turnOn()
    {
        // If our wifi is not already enabled.
        if(!wifiManager.isWifiEnabled())
        {
            // Set up an intent to allow us to pick our wifi connection.
            Intent enableWiFiIntent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);

            // Start the wifi selection activity.
            currentActivity.startActivityForResult(enableWiFiIntent, WiFiConstants.REQUEST_ENABLE_WIFI);

            // Turn on wifi.
            wifiManager.setWifiEnabled(true);
        }
    }

    //////////////////////////////////////////////////
    //                  Turn Off                    //
    //==============================================//
    // This will allow us to turn off wifi.         //
    //////////////////////////////////////////////////
    public void turnOff()
    {
        // If our wifi is already enabled.
        if(wifiManager.isWifiEnabled())
        {
            // Turn off wifi.
            wifiManager.setWifiEnabled(false);
        }
    }

    // Getters.
    // Get the wifi p2p filters.
    public IntentFilter getWifiP2PFilter()                          { return wifiP2PFilter; }

    // Get the wifi filters.
    public IntentFilter getWifiFilter()                             { return wifiFilter; }

    // Get the wifi broadcast receiver.
    public WiFiBroadcastReceiver getWifiBroadcastReceiver()         { return wifiBroadcastReceiver; }

    // This will return a wifi broadcast receiver.
    public WiFiP2PBroadcastReceiver getWifiP2PBroadcastReceiver()   { return wifiP2PBroadcastReceiver; }
}
