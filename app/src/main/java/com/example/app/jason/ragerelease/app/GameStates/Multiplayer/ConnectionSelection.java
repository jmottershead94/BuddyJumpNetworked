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
public class ConnectionSelection extends NetworkActivity implements View.OnClickListener
{
    // Attributes.
    private static final String PREFS_NAME = "MyPrefsFile";
    private final String multiplayerKeyName = "multiplayer";
    private boolean multiplayerStatus = false;
    private Button bluetoothButton = null;
    private Button wifiButton = null;
    private BluetoothHandler bluetoothHandler = null;
    private static final String activateBluetoothMessage = "Activate Bluetooth";
    private static final String activateWiFiMessage = "Activate WiFi";
    private static final String acceptedBluetoothMessage = "Continue With Bluetooth";
    private static final String acceptedWiFiMessage = "Continue With WiFi";

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

        if(savedInstanceState != null)
        {
            multiplayerStatus = savedInstanceState.getBoolean(multiplayerKeyName);
        }

        // Loading multiplayer status for repeated use.
        SharedPreferences multiplayerSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        multiplayerStatus = multiplayerSettings.getBoolean(multiplayerKeyName, true);

        // Setting up each button to access them from the connection selection xml file.
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

        // Constantly check the status of our bluetooth adapter to change the text accordingly.
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

        SharedPreferences multiplayerSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = multiplayerSettings.edit();

        // Saving the player option status.
        editor.putBoolean(multiplayerKeyName, true);

        editor.apply();
    }

    public void checkEnabledStatus()
    {
        // Setting up a handler and runnable to handle button text changes.
        final Handler handler = new Handler();
        handler.post(new Runnable()
        {
            // What happens after delay.
            @Override
            public void run()
            {
                // Check to see if bluetooth is already enabled.
                if(bluetoothHandler.getBluetoothAdapter().isEnabled())
                {
                    // If so, use the correct text message for the button.
                    bluetoothButton.setText(acceptedBluetoothMessage);
                }
                else
                {
                    // If so, use the correct text message for the button.
                    bluetoothButton.setText(activateBluetoothMessage);
                }

                // Do the same for WiFi.
                if(wifiManager.isWifiEnabled())
                {
                    wifiButton.setText(acceptedWiFiMessage);
                }
                else
                {
                    wifiButton.setText(activateWiFiMessage);

                    // If we have messageReply the message.
                    if(DebugInformation.messageReply == DebugInformation.ACCEPTED_MESSAGE)
                    {
                        // Attempt to turn on WiFi.
                        wifiButton.setText(acceptedWiFiMessage);

                        // Turn on wifi connection.
                        // Look for proper way to ask user permission - later.
                        connectionApplication.getConnectionManagement().getWifiHandler().turnOn();

                        DebugInformation.resetMessageValues();
                    }
                    else if(DebugInformation.messageReply == DebugInformation.DECLINED_MESSAGE)
                    {
                        DebugInformation.resetMessageValues();
                    }
                }

                handler.postDelayed(this, 1000);
            }
        });
    }

    public void onClick(View view)
    {
        Intent matchMakerActivity = new Intent(this, MatchMaker.class);

        // If we want bluetooth access.
        if(view == bluetoothButton)
        {
            // Attempt to turn bluetooth on.
            bluetoothHandler.TurnOn(this);

            // If bluetooth has been enabled.
            if(bluetoothHandler.getBluetoothAdapter().isEnabled())
            {
                // Move to the next singlePlayerGame state.
                startActivity(matchMakerActivity);
            }
        }
        // Otherwise, we are using wifi.
        else if(view == wifiButton)
        {
            if(!wifiManager.isWifiEnabled())
            {
                DebugInformation.resetMessageValues();

                // Display a message box to the user.
                DebugInformation.displayMessageBox(this, "WiFi", "Turn on wifi?", "Yes", "No");
            }
            else
            {
                startActivity(matchMakerActivity);
            }
        }
    }
}
