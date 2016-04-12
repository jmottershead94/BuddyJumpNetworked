package com.example.app.jason.ragerelease.app.GameStates.Multiplayer;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.Debug.DebugInformation;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkConstants;

import java.util.Set;

/**
 * Created by Jason Mottershead on 05/04/2016.
 */

public class DeviceList extends NetworkActivity// implements WifiP2pManager.PeerListListener
{
    // Attributes.
    private Set<WifiP2pDevice> wifiP2pDevices = null;
    private ListView newDevicesListView = null;
    private int playerMatchStatus = 0;
//    private ArrayAdapter<String> peerNames = null;
//    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
//    private WifiP2pDevice wifiP2pDevice = new WifiP2pDevice();

    // Methods.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        setResult(Activity.RESULT_CANCELED);

        Button scanButton = (Button) findViewById(R.id.button_scan);
        //peerNames = new ArrayAdapter<String>(this, R.layout.device_name);
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

                findViewById(R.id.deviceListProgressBar).setVisibility(View.VISIBLE);

                // Start a new scan.
                searchForDevices();

                newDevicesListView.setAdapter(peerNames);
                newDevicesListView.setOnItemClickListener(deviceClickListener);
                newDevicesListView.setVisibility(View.VISIBLE);
                //view.setVisibility(View.GONE);
            }
        });

//        newDevicesListView.setAdapter(peerNames);
//        newDevicesListView.setOnItemClickListener(deviceClickListener);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
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

            Intent selectionScreenActivity = new Intent(connectionApplication.getConnectionManagement().getNetworkActivity(), MultiplayerSelection.class);
            selectionScreenActivity.putExtra(NetworkConstants.EXTRA_PLAYER_MATCH_STATUS, playerMatchStatus);
            selectionScreenActivity.putExtra(NetworkConstants.EXTRA_DEVICE_ADDRESS, wifiP2pDevice.deviceAddress);

            DebugInformation.displayShortToastMessage(connectionApplication.getConnectionManagement().getNetworkActivity(), "You are connecting with: " + deviceName);

            // Attempt to connect to the device.
            connectToPeer(wifiP2pDevice);

            setResult(Activity.RESULT_OK, selectionScreenActivity);
            startActivity(selectionScreenActivity);

            //finish();
        }
    };
}