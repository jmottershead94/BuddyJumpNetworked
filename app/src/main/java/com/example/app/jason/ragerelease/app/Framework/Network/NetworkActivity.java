package com.example.app.jason.ragerelease.app.Framework.Network;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;

import com.example.app.jason.ragerelease.app.Framework.Debug.DebugInformation;

/**
 * Created by Jason Mottershead on 04/04/2016.
 */

public class NetworkActivity extends Activity
{
    // Attributes.
    protected WifiP2pManager wifiP2pManager = null;
    protected WifiManager wifiManager = null;
    protected Channel wifiChannel = null;
    protected ConnectionApplication connectionApplication = null;

    // Methods.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiChannel = wifiP2pManager.initialize(this, getMainLooper(), null);
        connectionApplication = (ConnectionApplication)this.getApplicationContext();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        connectionApplication.setConnectionManagement(wifiP2pManager, wifiManager, wifiChannel, this);
        registerReceiver(connectionApplication.getConnectionManagement().getWifiHandler().getWifiBroadcastReceiver(), connectionApplication.getConnectionManagement().getWifiHandler().getWifiFilter());
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(connectionApplication.getConnectionManagement().getWifiHandler().getWifiBroadcastReceiver());
    }

    protected void connectToPeer(WifiP2pDevice connectedDevice)
    {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = connectedDevice.deviceAddress;
        wifiP2pManager.connect(wifiChannel, config, new WifiP2pManager.ActionListener()
        {
            @Override
            public void onSuccess()
            {
                // We have successfully connected.
                DebugInformation.displayShortToastMessage(connectionApplication.getConnectionManagement().getNetworkActivity(), "Connected!");
            }

            @Override
            public void onFailure(int reason)
            {
                // We have not connected.
                // Retry the connection?
                DebugInformation.displayShortToastMessage(connectionApplication.getConnectionManagement().getNetworkActivity(), "Not connected!");
            }
        });
    }
}
