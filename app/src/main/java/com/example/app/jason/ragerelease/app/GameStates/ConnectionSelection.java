// The package location for this class.
package com.example.app.jason.ragerelease.app.GameStates;

// All of the extra includes here.
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;

/**
 * Created by Jason Mottershead on 22/03/2016.
 */

// Connection Selection IS AN activity, therefore inherits from it.
public class ConnectionSelection extends Activity
{
    // Attributes.
    private static final String PREFS_NAME = "MyPrefsFile";
    private final String multiplayerKeyName = "multiplayer";
    private boolean multiplayerStatus = false;

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

        // We are now accessing multiplayer, pass this message along.
        //savedInstanceState.putBoolean(multiplayerKeyName, true);

        if(savedInstanceState != null)
        {
            multiplayerStatus = savedInstanceState.getBoolean(multiplayerKeyName);
        }

        // Loading multiplayer status for repeated use.
        SharedPreferences multiplayerSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        multiplayerStatus = multiplayerSettings.getBoolean(multiplayerKeyName, true);

        Toast.makeText(getApplicationContext(), "Multiplayer Status: " + multiplayerStatus, Toast.LENGTH_SHORT).show();

        // Setting up each button to access them from the connection selection xml file.
        final Button bluetoothButton = (Button) findViewById(R.id.bluetoothButton);
        final Button wifiButton = (Button) findViewById(R.id.wifiButton);
        final Button mainMenuButton = (Button) findViewById(R.id.connectionSelectionMainMenuButton);
        final NavigationButton button = new NavigationButton();

        // If any of the buttons are pressed on the connection selection screen.
        button.isPressed(bluetoothButton, this, MatchMaker.class);
        button.isPressed(wifiButton, this, MatchMaker.class);
        button.isPressed(mainMenuButton, this, MainMenu.class);
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
        final CharSequence saveMessage = "Multiplayer should be active.";

        // Save changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is killed or restarted.
        savedInstanceState.putBoolean(multiplayerKeyName, true);

        // Save the current state.
        super.onSaveInstanceState(savedInstanceState);

        //Toast.makeText(this, saveMessage, Toast.LENGTH_SHORT).show();
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
}
