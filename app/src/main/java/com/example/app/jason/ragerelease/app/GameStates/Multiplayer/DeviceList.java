// The package location for this class.
package com.example.app.jason.ragerelease.app.GameStates.Multiplayer;

// All of the extra includes here.
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

// Device list IS A network activity, therefore inherits from it.
// This class will display a list of peers on the same network.
public class DeviceList extends NetworkActivity
{
    // Attributes.
    // Private.
    private ListView newDevicesListView = null;     // This will allow us to fill the list view with new devices.
    private int playerMatchStatus = 0;              // The current match ID for the player.

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
        setContentView(R.layout.activity_device_list);

        // Set the activity result to canceled to start with, just in case the user backs out.
        setResult(Activity.RESULT_CANCELED);

        // Initialising our attributes.
        Button scanButton = (Button) findViewById(R.id.button_scan);
        newDevicesListView = (ListView) findViewById(R.id.new_devices);
        playerMatchStatus = getIntent().getIntExtra(NetworkConstants.EXTRA_PLAYER_MATCH_STATUS, 0);

        // Setting the on click listener for our scan button.
        scanButton.setOnClickListener(new View.OnClickListener() {
            //////////////////////////////////////////////////
            //                  On Click                    //
            //==============================================//
            // Here we will listen our for any clicks on    //
            // our button, and complete specific tasks      //
            // depending on what we need to do.             //
            //////////////////////////////////////////////////
            @Override
            public void onClick(View view)
            {
                // If our wifi is already enabled.
                if (wifiManager.isWifiEnabled())
                {
                    // Reset the message box values.
                    DebugInformation.resetMessageValues();

                    // Clear previous scan data.
                    newDevicesListView.setVisibility(View.INVISIBLE);
                    peers.clear();
                    peerNames.clear();

                    // Make the animated loading progress wheel visible.
                    findViewById(R.id.deviceListProgressBar).setVisibility(View.VISIBLE);

                    // Start a new scan.
                    searchForDevices();

                    // Set up the new device list view.
                    newDevicesListView.setAdapter(peerNames);
                    newDevicesListView.setOnItemClickListener(deviceClickListener);
                    newDevicesListView.setVisibility(View.VISIBLE);
                }
                // Otherwise, our wifi is not already enabled.
                else
                {
                    // The user has disconnected.
                    userDisconnected();
                }
            }
        });
    }

    // This will set listen out for any clicks onto our new devices list view.
    private AdapterView.OnItemClickListener deviceClickListener = new AdapterView.OnItemClickListener()
    {
        //////////////////////////////////////////////////
        //                On Item Click                 //
        //==============================================//
        // This will trigger whenever we click on an    //
        // item in the list view.                       //
        // In this case we will be selecting the device //
        // to join a game.                              //
        //////////////////////////////////////////////////
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
        {
            // If our wifi is already enabled.
            if(wifiManager.isWifiEnabled())
            {
                // Store the name of the device.
                String deviceName = ((TextView) view).getText().toString();
                //String address = info.substring(info.length() - 17);

                // Loop through all of the peers we have.
                for (WifiP2pDevice device : peers)
                {
                    // If our device name is within our list and we have clicked on the device.
                    if (deviceName == device.deviceName)
                    {
                        // Set our connected device.
                        wifiP2pDevice = device;
                    }
                }

                // Setting up the multiplayer selection activity.
                Intent selectionScreenActivity = new Intent(connectionApplication.getConnectionManagement().getNetworkActivity(), MultiplayerSelection.class);

                // Placing in the player match status and the connected device address.
                selectionScreenActivity.putExtra(NetworkConstants.EXTRA_PLAYER_MATCH_STATUS, playerMatchStatus);
                selectionScreenActivity.putExtra(NetworkConstants.EXTRA_DEVICE_ADDRESS, wifiP2pDevice.deviceAddress);

                // Display a message to the user telling them who they are connecting with for feedback.
                DebugInformation.displayShortToastMessage(connectionApplication.getConnectionManagement().getNetworkActivity(), "You are connecting with: " + deviceName);

                // Attempt to connect to the device.
                connectToPeer(wifiP2pDevice);

                // Past this point we are ok.
                setResult(Activity.RESULT_OK, selectionScreenActivity);

                // Go to the next game state.
                startActivity(selectionScreenActivity);
            }
            // Otherwise, our wifi is not already enabled.
            else
            {
                // The user has disconnected.
                userDisconnected();
            }
        }
    };
}
