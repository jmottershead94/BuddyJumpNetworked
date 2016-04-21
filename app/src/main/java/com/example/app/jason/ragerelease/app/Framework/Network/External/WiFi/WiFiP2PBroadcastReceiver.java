// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi;

// All of the extra includes here.
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

/**
 * Created by Jason Mottershead on 04/04/2016.
 */

// Wifi p2p broadcast receiver IS A network broadcast receiver, therefore inherits from it.
// This class will be responsible for handling all of our wifi p2p state changes.
public class WiFiP2PBroadcastReceiver extends NetworkBroadcastReceiver
{
    // Attributes.
    // Private.
    private WifiP2pDeviceList peers = null;     // This will hold the list of wifi p2p devices on our network.
    private int playerMatchStatus = 0;          // The current match ID of the player (host or join).

    // Methods.
    //////////////////////////////////////////////////
    //                  Constructor                 //
    //==============================================//
    // This will set up the network broadcast       //
    // receiver for wifi connections.               //
    // As well as assign our current match ID.      //
    //////////////////////////////////////////////////
    public WiFiP2PBroadcastReceiver(WifiManager manager, WifiP2pManager p2pManager, Channel channel, NetworkActivity activity)
    {
        super(manager, p2pManager, channel, activity);

        // Get the current match ID from the intent.
        int currentMatchID = currentActivity.getIntent().getIntExtra(NetworkConstants.EXTRA_PLAYER_MATCH_STATUS, 0);

        // If we have a match ID.
        if(currentMatchID != 0)
        {
            // Assign our current match status to this ID.
            playerMatchStatus = currentMatchID;
        }

        // Initialise our attribute.
        peers = new WifiP2pDeviceList();
    }

    //////////////////////////////////////////////////
    //                  On Receive                  //
    //==============================================//
    // This will be used to check if our wifi       //
    // p2p states.                                  //
    // Here we will manage connection changes,      //
    // peer discoveries and wifi states.            //
    //////////////////////////////////////////////////
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Get our current action in the activity.
        String action = intent.getAction();

        // If there is a change in the WiFi P2P status.
        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action))
        {
            // Obtain the current wifi state.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);

            // If we have wifi p2p enabled.
            if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED)
            {
                // Reset our message box values.
                DebugInformation.resetMessageValues();
            }
            // Otherwise, wifi p2p is not enabled.
            else
            {
                // Do nothing.
            }
        }
        // Otherwise, if there is a change in the list of available peers.
        else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action))
        {
            // Check to see if we have a wifi p2p manager.
            if(wifiP2pManager != null)
            {
                // If so, request all of the peers on this channel and with this peer listener.
                wifiP2pManager.requestPeers(wifiChannel, peerListListener);
            }
        }
        // Otherwise, if the state of WiFi P2P connectivity has changed.
        else if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action))
        {
            // Check to see if we have a wifi p2p manager.
            if (wifiP2pManager == null)
            {
                // If we don't, exit this function.
                return;
            }

            // Getting access to the network information from the wifi p2p manager.
            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            // If our network is connected.
            if (networkInfo.isConnected())
            {
                // We are requesting connection information from the connected wifi channel, and with this connection info listener.
                wifiP2pManager.requestConnectionInfo(wifiChannel, connectionInfoListener);
            }
        }
        // Otherwise, if this device's details have changed.
        else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action))
        {
            // Respond to this device's wifi state changing.
        }
    }

    // The peer listener which will listen our for any peers that are available.
    private PeerListListener peerListListener = new PeerListListener()
    {
        //////////////////////////////////////////////////
        //               On Peers Available             //
        //==============================================//
        // This will trigger whenever there is a peer   //
        // available.                                   //
        //////////////////////////////////////////////////
        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList)
        {
            // Set our peer list to the wifi p2p device list.
            peers = wifiP2pDeviceList;
        }
    };

    // The connection info listener that will listen our for any new connection information.
    private ConnectionInfoListener connectionInfoListener = new ConnectionInfoListener()
    {
        //////////////////////////////////////////////////
        //           On Connection Info Available       //
        //==============================================//
        // This will trigger whenever we get any new    //
        // connected information from a peer.           //
        //////////////////////////////////////////////////
        @Override
        public void onConnectionInfoAvailable(final WifiP2pInfo wifiP2pInfo)
        {
            // If we have wifi turned on.
            if(wifiManager.isWifiEnabled())
            {
                // Getting access to the group owner address.
                String groupOwnerAddress = wifiP2pInfo.groupOwnerAddress.getHostAddress();

                // If the player is hosting.
                if (playerMatchStatus == NetworkConstants.HOST_ID)
                {
                    // Set up the server tasks - not so different from the client.
                    // The socket is set up differently.
                    wifiServerTasks = new WiFiServerTasks(currentActivity);
                    wifiServerTasks.start();
                }
                // If the player is joining.
                else if (playerMatchStatus == NetworkConstants.JOIN_ID)
                {
                    // Set up the client tasks - not so different from the server.
                    // The socket is set up differently.
                    wifiClientTasks = new WiFiClientTasks(currentActivity, groupOwnerAddress);
                    wifiClientTasks.start();
                }
            }
        }
    };

    // Getters.
    // Get our list of peers.
    public WifiP2pDeviceList getPeers()             { return peers; }

    // Get our server task.
    public WiFiServerTasks getServerTask()          { return wifiServerTasks; }

    // Get our client task.
    public WiFiClientTasks getClientTask()          { return wifiClientTasks; }
}
