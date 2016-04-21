// The package location for this class.
package com.example.app.jason.ragerelease.app.GameStates.Multiplayer;

// All of the extra includes here.
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.Handler;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.Debug.DebugInformation;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;
import com.example.app.jason.ragerelease.app.Framework.Network.External.Bluetooth.BluetoothHandler;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;
import com.example.app.jason.ragerelease.app.GameStates.MainMenu;

/**
 * Created by Jason Mottershead on 22/03/2016.
 */

// Connection Selection IS A network activity, therefore inherits from it.
// This will allow us to select our connection type.
public class ConnectionSelection extends NetworkActivity implements View.OnClickListener
{
    // Attributes.
    // Private.
    private static final String PREFS_NAME = "MyPrefsFile";                             // The preference file name for shared preference.
    private final String multiplayerKeyName = "multiplayer";                            // The key to access the multiplayer status.
    private boolean multiplayerStatus = false;                                          // The multiplayer status to help separate the game from single player to multiplayer.
    private Button bluetoothButton = null;                                              // The button that will allow us to access bluetooth connection.
    private Button wifiButton = null;                                                   // The button that will allow us to access wifi connection.
    private BluetoothHandler bluetoothHandler = null;                                   // Access to our bluetooth checkEnabledHandler.
    private static final String activateBluetoothMessage = "Activate Bluetooth";        // The text to tell the user that they can activate bluetooth.
    private static final String activateWiFiMessage = "Activate WiFi";                  // The text to tell the user that they can activate wifi.
    private static final String acceptedBluetoothMessage = "Continue With Bluetooth";   // The text to tell the user that they are going to use bluetooth.
    private static final String acceptedWiFiMessage = "Continue With WiFi";             // The text to tell the user that they are going to use wifi.
    private final Handler checkEnabledHandler = new Handler();                          // The handler for checking whether or not we are connected to wifi/bluetooth.
    private Runnable runnable = new Runnable()                                          // The runnable which will run our enabled checks.
    {
        //////////////////////////////////////////////////
        //                      Run                     //
        //==============================================//
        // This will check the enabled status for both  //
        // bluetooth and wifi, and set our button       //
        // text accordingly.                            //
        // We will also handle our message box response //
        // here.                                        //
        //////////////////////////////////////////////////
        @Override
        public void run()
        {
            // If our bluetooth is already enabled.
            if(bluetoothHandler.getBluetoothAdapter().isEnabled())
            {
                // Set the continue with bluetooth message.
                bluetoothButton.setText(acceptedBluetoothMessage);
            }
            // Otherwise, bluetooth is not enabled.
            else
            {
                // Set the activate bluetooth message.
                bluetoothButton.setText(activateBluetoothMessage);
            }

            // If our wifi is already enabled.
            if(wifiManager.isWifiEnabled())
            {
                // Set the continue with wifi message.
                wifiButton.setText(acceptedWiFiMessage);
            }
            // Otherwise, wifi is not enabled.
            else
            {
                // Set the activate wifi message.
                wifiButton.setText(activateWiFiMessage);

                // If we have accepted the message.
                if(DebugInformation.messageReply == DebugInformation.ACCEPTED_MESSAGE)
                {
                    // Attempt to turn on WiFi.
                    wifiButton.setText(acceptedWiFiMessage);

                    // Turn on wifi connection.
                    connectionApplication.getConnectionManagement().getWifiHandler().turnOn();

                    // Reset our message values.
                    DebugInformation.resetMessageValues();
                }
                // Otherwise, if we have declined the message.
                else if(DebugInformation.messageReply == DebugInformation.DECLINED_MESSAGE)
                {
                    // Do nothing, just reset our message values.
                    DebugInformation.resetMessageValues();
                }
            }

            // Run this every second.
            checkEnabledHandler.postDelayed(this, 1000);
        }
    };

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
        setContentView(R.layout.activity_connection_selection);

        // Retrieving the multiplayer status.
        if(savedInstanceState != null)
        {
            multiplayerStatus = savedInstanceState.getBoolean(multiplayerKeyName);
        }

        // Loading multiplayer status for repeated use.
        SharedPreferences multiplayerSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        multiplayerStatus = multiplayerSettings.getBoolean(multiplayerKeyName, true);

        // Initialising our attributes.
        final Button mainMenuButton = (Button) findViewById(R.id.connectionSelectionMainMenuButton);
        final NavigationButton button = new NavigationButton();
        bluetoothButton = (Button) findViewById(R.id.bluetoothButton);
        wifiButton = (Button) findViewById(R.id.wifiButton);
        bluetoothHandler = new BluetoothHandler(getApplicationContext());

        // On click listeners for the connection type buttons.
        bluetoothButton.setOnClickListener(this);
        wifiButton.setOnClickListener(this);

        // If any of the buttons are pressed on the connection selection screen.
        button.isPressed(mainMenuButton, this, MainMenu.class);

        // Constantly check the status of our wifi/bluetooth adapter to change the text accordingly.
        checkEnabledStatus();
    }

    //////////////////////////////////////////////////
    //              On Save Instance State          //
    //==============================================//
    //  This will save the current game status,     //
    //  e.g. if we are using multiplayer.           //
    //  This is called if the phone orientation     //
    //  changes, or if for any reason the phone     //
    //  is forced out of this activity and into     //
    //  another application (i.e. like a phone      //
    //  call).                                      //
    //////////////////////////////////////////////////
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        // Save changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is killed or restarted.
        savedInstanceState.putBoolean(multiplayerKeyName, true);

        // Save the current state.
        super.onSaveInstanceState(savedInstanceState);
    }

    //////////////////////////////////////////////////
    //              On Restore Instance State       //
    //==============================================//
    //  This will save the current game status,     //
    //  e.g. if we are using multiplayer.           //
    //  This is called if the phone orientation     //
    //  changes, or if for any reason the phone     //
    //  is forced out of this activity and into     //
    //  another application (i.e. like a phone      //
    //  call).                                      //
    //////////////////////////////////////////////////
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        // Just in case the application is killed off.
        super.onRestoreInstanceState(savedInstanceState);

        // Get the current multiplayer status.
        multiplayerStatus = savedInstanceState.getBoolean(multiplayerKeyName);
    }

    //////////////////////////////////////////////////
    //                  On Pause                    //
    //==============================================//
    //  This will save the current multiplayer      //
    //  state, and save it to the device            //
    //  for future reference.                       //
    //  This will be called when we are leaving     //
    //  this activity.                              //
    //  When another activity is in the foreground. //
    //////////////////////////////////////////////////
    @Override
    protected void onPause()
    {
        super.onPause();

        // Accessing shared preferences.
        SharedPreferences multiplayerSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = multiplayerSettings.edit();

        // Saving the player option status.
        editor.putBoolean(multiplayerKeyName, true);

        // Applying the change to the shared preference file.
        editor.apply();
    }

    //////////////////////////////////////////////////
    //              Check Enabled Status            //
    //==============================================//
    // This will set our handler off running with   //
    // the runnable defined earlier.                //
    //////////////////////////////////////////////////
    public void checkEnabledStatus()
    {
        // Setting up a handler and runnable to handle button text changes.
        checkEnabledHandler.post(runnable);
    }

    //////////////////////////////////////////////////
    //                  On Click                    //
    //==============================================//
    // Here we will listen our for any clicks on    //
    // our buttons, and complete specific tasks     //
    // depending on what we need to do.             //
    //////////////////////////////////////////////////
    public void onClick(View view)
    {
        // Setting up the match maker activity.
        Intent matchMakerActivity = new Intent(this, MatchMaker.class);

        // If we want bluetooth access.
        if(view == bluetoothButton)
        {
            // Display a message to the user, telling them that bluetooth is under development.
            DebugInformation.displayShortToastMessage(this, "Bluetooth connection is currently under development");
        }
        // Otherwise, we are using wifi.
        else if(view == wifiButton)
        {
            // If our wifi is not already enabled.
            if(!wifiManager.isWifiEnabled())
            {
                // Reset our message values.
                DebugInformation.resetMessageValues();

                // Display a message box to the user.
                DebugInformation.displayMessageBox(this, "WiFi", "Turn on wifi?", "Yes", "No");
            }
            // Otherwise, our wifi is already enabled.
            else
            {
                // Stop this handler.
                checkEnabledHandler.removeCallbacks(runnable);

                // Go to the next game state.
                startActivity(matchMakerActivity);
            }
        }
    }
}
