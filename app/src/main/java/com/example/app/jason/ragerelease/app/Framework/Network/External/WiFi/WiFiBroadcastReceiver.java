package com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;

import com.example.app.jason.ragerelease.app.Framework.Debug.DebugInformation;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;

import java.util.List;

/**
 * Created by Jason Mottershead on 04/04/2016.
 */

public class WiFiBroadcastReceiver extends BroadcastReceiver
{
    // Attributes.
    private WifiP2pManager wifiP2pManager = null;
    private Channel wifiChannel = null;
//    private PeerListListener peerListListener = null;
    private NetworkActivity currentActivity = null;
    private WifiP2pDeviceList peers = null;

    // Methods.
    public WiFiBroadcastReceiver(WifiP2pManager manager, Channel channel, NetworkActivity activity)
    {
        wifiP2pManager = manager;
        wifiChannel = channel;
        currentActivity = activity;
        peers = new WifiP2pDeviceList();
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Get our current action in the activity.
        String action = intent.getAction();

        // If there is a change in the WiFi P2P status.
        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action))
        {
            // Check to see if wifi is enabled and notify appropriate activity.
            //DebugInformation.displayShortToastMessage(currentActivity, "Wifi Peer State Changed");

            // Obtain the current wifi state.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);

            // If we have wifi turned on.
            if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED)
            {
                // Wifi is enabled.
                DebugInformation.displayShortToastMessage(currentActivity, "Wifi is on");
            }
            // Otherwise, wifi is turned off.
            else
            {
                // Wifi is disabled.
                DebugInformation.displayShortToastMessage(currentActivity, "Wifi is off");
            }
        }
        // Otherwise, if there is a change in the list of available peers.
        else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action))
        {
            // Call WifiP2pManager.requestPeers() to get a list of current peers.
            //DebugInformation.displayShortToastMessage(currentActivity, "Wifi Peer List Changed");
            if(wifiP2pManager != null)
            {
                wifiP2pManager.requestPeers(wifiChannel, peerListListener);
            }
        }
        // Otherwise, if the state of WiFi P2P connectivity has changed.
        else if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action))
        {
            // Respond to new connection or disconnections.
            //DebugInformation.displayShortToastMessage(currentActivity, "Wifi Connection Changed");

        }
        // Otherwise, if this device's details have changed.
        else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action))
        {
            // Respond to this device's wifi state changing.
            //DebugInformation.displayShortToastMessage(currentActivity, "Wifi State Changing");

        }
    }

    private PeerListListener peerListListener = new PeerListListener()
    {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList)
        {
            peers = wifiP2pDeviceList;
        }
    };

    // Getters.
    // Getting our peers.
    public WifiP2pDeviceList getPeers() { return peers; }
}
