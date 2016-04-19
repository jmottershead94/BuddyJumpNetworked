// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework.Network;

// All of the extra includes here.
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.Debug.DebugInformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason Mottershead on 04/04/2016.
 */

// Network activity IS AN activity, therefore inherits from it.
// This class will contain all generic network functions.
public class NetworkActivity extends Activity
{
    // Attributes.
    // Protected.
    protected WifiP2pManager wifiP2pManager = null;                         // Used to manage our peer-to-peer wifi connection.
    protected WifiManager wifiManager = null;                               // Used to manage our wifi connection.
    protected Channel wifiChannel = null;                                   // Connects the application to the wifi p2p framework.
    protected ConnectionApplication connectionApplication = null;           // The application class for this application in order to try and keep data values between activities.
    protected ArrayAdapter<String> peerNames = null;                        // This will store the list of available peer names when we are searching for devices.
    protected List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();   // This will store a list of wifi p2p devices when we are searching for devices.
    protected WifiP2pDevice wifiP2pDevice = new WifiP2pDevice();            // This will store our connected wifi p2p device.

    // Methods.
    //////////////////////////////////////////////////
    //                  On Create                   //
    //==============================================//
    //  This will set the layout and create the     //
    //  activity on the first step into the Android //
    //  lifecycle.                                  //
    //////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Initialising our attributes.
        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiChannel = wifiP2pManager.initialize(this, getMainLooper(), null);
        connectionApplication = (ConnectionApplication)getApplicationContext();
        peerNames = new ArrayAdapter<String>(this, R.layout.device_name);
    }

    //////////////////////////////////////////////////
    //                  On Resume                   //
    //==============================================//
    // When we have come back to the activity, we   //
    // should register our listener for the wifi    //
    // data, because we want to listen out for any  //
    // wifi data when we are in this activity.      //
    //////////////////////////////////////////////////
    @Override
    protected void onResume()
    {
        super.onResume();

        // Setting up our connection management to use this activity.
        connectionApplication.setConnectionManagement(wifiP2pManager, wifiManager, wifiChannel, this);

        // Registering our broadcast receivers for wifi and wifi p2p.
        registerReceiver(connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver(), connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PFilter());
        registerReceiver(connectionApplication.getConnectionManagement().getWifiHandler().getWifiBroadcastReceiver(), connectionApplication.getConnectionManagement().getWifiHandler().getWifiFilter());
    }

    //////////////////////////////////////////////////
    //                  On Pause                    //
    //==============================================//
    // This will unregister the wifi broadcast      //
    // receivers.                                   //
    //////////////////////////////////////////////////
    @Override
    protected void onPause()
    {
        super.onPause();

        // Unregistering our wifi broadcast receivers.
        unregisterReceiver(connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver());
        unregisterReceiver(connectionApplication.getConnectionManagement().getWifiHandler().getWifiBroadcastReceiver());
    }

    //////////////////////////////////////////////////
    //              Search For Devices              //
    //==============================================//
    // This will search for any devices on the wifi //
    // connection.                                  //
    //////////////////////////////////////////////////
    protected void searchForDevices()
    {
        final Activity activityReference = this;

        // This will attempt to discover any peers on the wifi connection.
        wifiP2pManager.discoverPeers(wifiChannel, new WifiP2pManager.ActionListener()
        {
            // We have discovered a peer on the same network.
            @Override
            public void onSuccess()
            {
                // Add all of the peers that we have found into our own device list for processing.
                peers.addAll(connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getPeers().getDeviceList());

                // Retrieve the names off all of the devices.
                getDeviceName();
            }

            // We have not discovered any peers on the same network.
            @Override
            public void onFailure(int i)
            {
                // Display a message to the user, telling them that the search for peers was unsuccessful.
                DebugInformation.displayShortToastMessage(activityReference, "No more peers, retrying...");

                // Try searching again.
                searchForDevices();
            }
        });
    }

    //////////////////////////////////////////////////
    //                Connect To Peer               //
    //==============================================//
    // This will connect to our selected device.    //
    //////////////////////////////////////////////////
    protected void connectToPeer(final WifiP2pDevice connectedDevice)
    {
        // Setting up the wifi configuration.
        final Activity activityReference = this;
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = connectedDevice.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        // Connect to our selected device.
        wifiP2pManager.connect(wifiChannel, config, new WifiP2pManager.ActionListener()
        {
            // We are connecting to our peer.
            @Override
            public void onSuccess()
            {
                // Display a message to the user, telling them that they are connecting to their device.
                DebugInformation.displayShortToastMessage(activityReference, "Connecting...");
            }

            // We have not connected to our peer.
            @Override
            public void onFailure(int reason)
            {
                // Display a message to the user, telling them that they have not connected to their device.
                DebugInformation.displayShortToastMessage(activityReference, "Not connected, retrying...");

                // Retry the connection.
                connectToPeer(connectedDevice);
            }
        });
    }

    //////////////////////////////////////////////////
    //               Get Device Name                //
    //==============================================//
    // This will obtain all of the device names     //
    // from our peers.                              //
    //////////////////////////////////////////////////
    protected void getDeviceName()
    {
        // Loop through each of our peers.
        for(int i = 0; i < peers.size(); i++)
        {
            // Add the name of the device to our own string list.
            peerNames.add(peers.get(i).deviceName);

            // Display a message to the user, telling them the name of the device.
            DebugInformation.displayShortToastMessage(this, "Peer name: " + peerNames.getItem(i));
        }
    }
}
