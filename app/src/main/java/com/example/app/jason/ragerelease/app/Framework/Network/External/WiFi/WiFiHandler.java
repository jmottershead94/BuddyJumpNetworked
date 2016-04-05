package com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;

import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;

/**
 * Created by Jason Mottershead on 04/04/2016.
 */

public class WiFiHandler
{
    // Attributes.
    private IntentFilter wifiFilter = new IntentFilter();
    private WiFiBroadcastReceiver wifiBroadcastReceiver = null;
    private WifiManager wifiManager = null;
    private NetworkActivity currentActivity = null;

    // Methods.
    public WiFiHandler(WifiP2pManager p2pManager, WifiManager manager, Channel channel, NetworkActivity activity)
    {
        // Setting up the filters for the broadcast receiver to listen out for.
        wifiFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        wifiFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        wifiFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        wifiFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        // Setting the wifi manager to access turning wifi on or off.
        wifiManager = manager;

        currentActivity = activity;

        // Initialising the wifi broadcast receiver.
        wifiBroadcastReceiver = new WiFiBroadcastReceiver(p2pManager, channel, activity);
    }

    public void turnOn()
    {
        if(!wifiManager.isWifiEnabled())
        {
            Intent enableBluetoothIntent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
            currentActivity.startActivityForResult(enableBluetoothIntent, WiFiConstants.REQUEST_ENABLE_WIFI);
            wifiManager.setWifiEnabled(true);
        }
    }

    public void turnOff()
    {
        if(wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(false);
        }
    }

    // Getters.
    // This will return our wifi filters.
    public IntentFilter getWifiFilter()                     { return wifiFilter; }

    // This will return a wifi broadcast receiver.
    public WiFiBroadcastReceiver getWifiBroadcastReceiver() { return wifiBroadcastReceiver; }
}
