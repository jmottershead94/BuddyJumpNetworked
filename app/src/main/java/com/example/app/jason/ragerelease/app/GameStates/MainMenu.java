// The package location for this class.
package com.example.app.jason.ragerelease.app.GameStates;

// All of the extra includes here.
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;
import com.example.app.jason.ragerelease.app.Framework.Network.Internal.AndroidOSHandlers.SensorHandler;

/**
 * Created by Jason Mottershead on 17/10/2015.
 */

// MainMenu IS AN Activity, therefore inherits from it.
// This class will allow access to any of the app features (to the game, options, credits, or back to the title screen).
public class MainMenu extends Activity
{
    // Attributes.
    // Private.
    private static final String PREFS_NAME = "MyPrefsFile";
    private final String multiplayerKeyName = "multiplayer";
    private SensorHandler sensorHandler = null;

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
        setContentView(R.layout.activity_main_menu);

        // Setting up each button to access them from the main menu xml file.
        final Button playButton = (Button) findViewById(R.id.playButton);
        final Button multiplayerButton = (Button) findViewById(R.id.multiplayerButton);
        final Button optionsButton = (Button) findViewById(R.id.optionsButton);
        final Button creditsButton = (Button) findViewById(R.id.creditsButton);
        final Button scoresButton = (Button) findViewById(R.id.scoresButton);
        final NavigationButton button = new NavigationButton();

        // Setting up the sensor.
        sensorHandler = new SensorHandler(this, SENSOR_SERVICE);

        // If any of the buttons are pressed on the main menu.
        // Take the user to the correct activity depending on the button pressed.
        button.isPressed(playButton, this, SinglePlayerSelection.class);
        button.isPressed(multiplayerButton, this, ConnectionSelection.class);
        button.isPressed(optionsButton, this, Options.class);
        button.isPressed(creditsButton, this, Credits.class);
        button.isPressed(scoresButton, this, HighScores.class);


    }

    //////////////////////////////////////////////////
    //                  On Resume                   //
    //==============================================//
    //  When we have come back to the activity, we  //
    //  should register our listener for the sensor //
    //  data, because we want to listen out for any //
    //  sensor data when we are in this activity.   //
    //////////////////////////////////////////////////
    @Override
    protected void onResume()
    {
        super.onResume();
        sensorHandler.registerListener();
    }

    //////////////////////////////////////////////////
    //                  On Pause                    //
    //==============================================//
    //  This will save the current multiplayer      //
    //  state, and save it to the device            //
    //  for future reference.                       //
    //  And also unregister the sensor listener.    //
    //  This will be called when we are leaving     //
    //  this activity.                              //
    //  When another activity is in the foreground. //
    //////////////////////////////////////////////////
    @Override
    protected void onPause()
    {
        super.onPause();

        sensorHandler.unregisterListener();

        SharedPreferences multiplayerSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = multiplayerSettings.edit();

        // Saving the player option status.
        editor.putBoolean(multiplayerKeyName, false);

        editor.apply();
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
        final CharSequence saveMessage = "Multiplayer shouldn't be active.";

        // Save changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is killed or restarted.
        savedInstanceState.putBoolean(multiplayerKeyName, false);

        // Save the current state.
        super.onSaveInstanceState(savedInstanceState);
    }
}
