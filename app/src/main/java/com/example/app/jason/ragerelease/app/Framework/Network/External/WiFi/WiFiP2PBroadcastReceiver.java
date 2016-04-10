package com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;

import com.example.app.jason.ragerelease.app.Framework.Debug.DebugInformation;
import com.example.app.jason.ragerelease.app.Framework.Network.External.NetworkBroadcastReceiver;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkConstants;

import java.net.InetAddress;
import java.util.List;

/**
 * Created by Jason Mottershead on 04/04/2016.
 */

public class WiFiP2PBroadcastReceiver extends NetworkBroadcastReceiver
{
    // Attributes.
    private WifiP2pDeviceList peers = null;
    private int playerMatchStatus = 0;

    // Methods.
    public WiFiP2PBroadcastReceiver(WifiManager manager, WifiP2pManager p2pManager, Channel channel, NetworkActivity activity)
    {
        super(manager, p2pManager, channel, activity);

//        wifiManager = manager;
//        wifiP2pManager = p2pManager;
//        wifiChannel = channel;
//        currentActivity = activity;
        int currentMatchID = currentActivity.getIntent().getIntExtra(NetworkConstants.EXTRA_PLAYER_MATCH_STATUS, 0);

        if(currentMatchID != 0)
        {
            playerMatchStatus = currentMatchID;
        }

        peers = new WifiP2pDeviceList();
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Get our current action in the activity.
        String action = intent.getAction();

//        if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action))
//        {
//            // Obtain the current wifi state.
//            int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
//
//            // If we have wifi turned on.
//            if(state == WifiManager.WIFI_STATE_ENABLED)
//            {
//                // Wifi is enabled.
//                DebugInformation.displayShortToastMessage(currentActivity, "Wifi P2P is enabled");
//            }
//            // Otherwise, wifi is turned off.
//            else
//            {
//                // Wifi is disabled.
//                DebugInformation.displayShortToastMessage(currentActivity, "Wifi P2P is not enabled");
//            }
//        }

        // Otherwise, if there is a change in the WiFi P2P status.
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
                //DebugInformation.displayShortToastMessage(currentActivity, "Wifi P2P is enabled");
            }
            // Otherwise, wifi is turned off.
            else
            {
                // Wifi is disabled.
                //DebugInformation.displayShortToastMessage(currentActivity, "Wifi P2P is not enabled");
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
            DebugInformation.displayShortToastMessage(currentActivity, "Wifi Connection Changed");

            // If we have declared a match ID.
            //if(playerMatchStatus == NetworkConstants.HOST_ID)
            //{

            if (wifiP2pManager == null)
            {
                DebugInformation.displayShortToastMessage(currentActivity, "Wifi P2P manager is null");
                return;
            }

            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected())
            {
                DebugInformation.displayShortToastMessage(currentActivity, "Network information connected.");

                // We are connected to the other device!
                // Info to find the group owner IP.
                DebugInformation.displayShortToastMessage(currentActivity, "Connected to other device");
                wifiP2pManager.requestConnectionInfo(wifiChannel, connectionInfoListener);
            }
            //}

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

    private ConnectionInfoListener connectionInfoListener = new ConnectionInfoListener()
    {
        @Override
        public void onConnectionInfoAvailable(final WifiP2pInfo wifiP2pInfo)
        {
            String groupOwnerAddress = wifiP2pInfo.groupOwnerAddress.getHostAddress();

            if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner && (playerMatchStatus == NetworkConstants.HOST_ID))
            {
                // If we have formed a group and we are the group owner.
                // Set up the server thread, accept incoming data connections?
                DebugInformation.displayShortToastMessage(currentActivity, "Should start server thread.");

                wifiServerAsyncTask = new WiFiServerAsyncTask(currentActivity);
            }
            else if (wifiP2pInfo.groupFormed && (playerMatchStatus == NetworkConstants.JOIN_ID))
            {
                // The other device acts as a client.
                // Create a client thread here?
                DebugInformation.displayShortToastMessage(currentActivity, "Should start client thread.");

                wifiClientAsyncTask = new WiFiClientAsyncTask(currentActivity, groupOwnerAddress);
            }
        }
    };

    // Getters.
    // Getting our peers.
    public WifiP2pDeviceList getPeers() { return peers; }
}
