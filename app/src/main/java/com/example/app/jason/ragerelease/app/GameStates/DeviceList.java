package com.example.app.jason.ragerelease.app.GameStates;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.Debug.DebugInformation;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Jason Mottershead on 05/04/2016.
 */

public class DeviceList extends NetworkActivity// implements WifiP2pManager.PeerListListener
{
    // Attributes.
    private Set<WifiP2pDevice> wifiP2pDevices = null;
    private ListView newDevicesListView = null;
    private ArrayAdapter<String> peerNames = null;
    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    private int playerMatchStatus = 0;
    private WifiP2pDevice wifiP2pDevice = new WifiP2pDevice();

    // Methods.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        setResult(Activity.RESULT_CANCELED);

        Button scanButton = (Button) findViewById(R.id.button_scan);
        peerNames = new ArrayAdapter<String>(this, R.layout.device_name);
        newDevicesListView = (ListView) findViewById(R.id.new_devices);
        playerMatchStatus = getIntent().getIntExtra(NetworkConstants.EXTRA_PLAYER_MATCH_STATUS, 0);

        scanButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Clear previous scan data.
                newDevicesListView.setVisibility(View.INVISIBLE);
                peers.clear();
                peerNames.clear();

                // Start a new scan.
                searchForDevices();
                //view.setVisibility(View.GONE);
            }
        });

//        newDevicesListView.setAdapter(peerNames);
//        newDevicesListView.setOnItemClickListener(deviceClickListener);
    }

//    @Override
//    public void onPeersAvailable(WifiP2pDeviceList peerList)
//    {
//        peers.addAll(peerList.getDeviceList());
//        getDeviceName();
//        newDevicesListView.setAdapter(peerNames);
//        newDevicesListView.setOnItemClickListener(deviceClickListener);
//        newDevicesListView.setVisibility(View.VISIBLE);
//        //newDevicesListView.notify
//
//        if(peers.size() == 0)
//        {
//            DebugInformation.displayShortToastMessage(this, "No more peers.");
//            findViewById(R.id.deviceListProgressBar).setVisibility(View.INVISIBLE);
//            return;
//        }
//    }

    private void searchForDevices()
    {
        findViewById(R.id.deviceListProgressBar).setVisibility(View.VISIBLE);

        wifiP2pManager.discoverPeers(wifiChannel, new WifiP2pManager.ActionListener()
        {
            @Override
            public void onSuccess()
            {
                DebugInformation.displayShortToastMessage(connectionApplication.getConnectionManagement().getNetworkActivity(), "Peer discovered!");

                peers.addAll(connectionApplication.getConnectionManagement().getWifiHandler().getWifiBroadcastReceiver().getPeers().getDeviceList());
                getDeviceName();
                newDevicesListView.setAdapter(peerNames);
                newDevicesListView.setOnItemClickListener(deviceClickListener);
                newDevicesListView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int i)
            {
                //newDevicesListView.setVisibility(View.INVISIBLE);
            }
        });

//        if(peers.size() == 0)
//        {
//            DebugInformation.displayShortToastMessage(connectionApplication.getConnectionManagement().getNetworkActivity(), "No more peers.");
//            findViewById(R.id.deviceListProgressBar).setVisibility(View.INVISIBLE);
//            return;
//        }
    }

    private void getDeviceName()
    {
        for(int i = 0; i < peers.size(); i++)
        {
            peerNames.add(peers.get(i).deviceName);
            DebugInformation.displayShortToastMessage(this, "Peer name: " + peerNames.getItem(i));
        }
    }

    private AdapterView.OnItemClickListener deviceClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
        {
            String deviceName = ((TextView) view).getText().toString();

            //String address = info.substring(info.length() - 17);
            for(WifiP2pDevice device : peers)
            {
                // If our device name is within our list and we have clicked on the device.
                if(deviceName == device.deviceName)
                {
                    // Set our connected device.
                    wifiP2pDevice = device;
                }
            }

            Intent selectionScreenActivity = new Intent(connectionApplication.getConnectionManagement().getNetworkActivity(), SelectionScreen.class);
            selectionScreenActivity.putExtra(NetworkConstants.EXTRA_PLAYER_MATCH_STATUS, playerMatchStatus);
            selectionScreenActivity.putExtra(NetworkConstants.EXTRA_DEVICE_ADDRESS, wifiP2pDevice.deviceAddress);

            DebugInformation.displayShortToastMessage(connectionApplication.getConnectionManagement().getNetworkActivity(), "You want to connect with: " + deviceName);

            // Attempt to connect to the device.
            connectToPeer(wifiP2pDevice);

            setResult(Activity.RESULT_OK, selectionScreenActivity);
            startActivity(selectionScreenActivity);

            //finish();
        }
    };

    private BroadcastReceiver wifiBroadcastPeerReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();

            if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action))
            {
                //if()
            }
        }
    };
}
