package com.example.app.jason.ragerelease.app.Framework.Network;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.Debug.DebugInformation;

import java.util.ArrayList;
import java.util.List;

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
    protected ArrayAdapter<String> peerNames = null;
    protected List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    protected WifiP2pDevice wifiP2pDevice = new WifiP2pDevice();

    // Methods.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiChannel = wifiP2pManager.initialize(this, getMainLooper(), null);
        connectionApplication = (ConnectionApplication)this.getApplicationContext();
        peerNames = new ArrayAdapter<String>(this, R.layout.device_name);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        connectionApplication.setConnectionManagement(wifiP2pManager, wifiManager, wifiChannel, this);
        registerReceiver(connectionApplication.getConnectionManagement().getWifiHandler().getWifiBroadcastReceiver(), connectionApplication.getConnectionManagement().getWifiHandler().getWifiFilter());
        //wifiManager.setWifiEnabled(true);

        if(!wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(true);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(connectionApplication.getConnectionManagement().getWifiHandler().getWifiBroadcastReceiver());
        //wifiManager.setWifiEnabled(false);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if(wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(false);
        }
    }

    protected void connectToPeer(WifiP2pDevice connectedDevice)
    {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = connectedDevice.deviceAddress;
        wifiP2pManager.connect(wifiChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // We have successfully connected.
                DebugInformation.displayShortToastMessage(connectionApplication.getConnectionManagement().getNetworkActivity(), "Connected!");
            }

            @Override
            public void onFailure(int reason) {
                // We have not connected.
                // Retry the connection?
                DebugInformation.displayShortToastMessage(connectionApplication.getConnectionManagement().getNetworkActivity(), "Not connected!");
            }
        });
    }

    protected void searchForDevices()
    {
        wifiP2pManager.discoverPeers(wifiChannel, new WifiP2pManager.ActionListener()
        {
            @Override
            public void onSuccess()
            {
                DebugInformation.displayShortToastMessage(connectionApplication.getConnectionManagement().getNetworkActivity(), "Peer discovered!");

                peers.addAll(connectionApplication.getConnectionManagement().getWifiHandler().getWifiBroadcastReceiver().getPeers().getDeviceList());
                getDeviceName();
            }

            @Override
            public void onFailure(int i)
            {
                //newDevicesListView.setVisibility(View.INVISIBLE);
            }
        });
    }

    protected void getDeviceName()
    {
        for(int i = 0; i < peers.size(); i++)
        {
            peerNames.add(peers.get(i).deviceName);
            DebugInformation.displayShortToastMessage(this, "Peer name: " + peerNames.getItem(i));
        }
    }

    protected void connectionInformation()
    {
        ConnectionInfoListener connectionInfoListener = new ConnectionInfoListener()
        {
            @Override
            public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo)
            {

            }
        };
    }
}
