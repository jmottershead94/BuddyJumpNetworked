// The package location for this class.
package com.example.app.jason.ragerelease.app.GameStates;

// All of the extra includes here.
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;
import com.example.app.jason.ragerelease.app.Framework.AndroidOSHandlers.SensorHandler;

/**
 * Created by Jason Mottershead on 17/10/2015.
 */

// MainMenu IS AN Activity, therefore inherits from it.
// This class will allow access to any of the app features (to the game, options, credits, or back to the title screen).
public class MainMenu extends Activity
{
    // Attributes.
    // Private.
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
        final Button optionsButton = (Button) findViewById(R.id.optionsButton);
        final Button creditsButton = (Button) findViewById(R.id.creditsButton);
        final NavigationButton button = new NavigationButton();

        // Setting up the sensor.
        sensorHandler = new SensorHandler(this, SENSOR_SERVICE);

        // If any of the buttons are pressed on the main menu.
        // Take the user to the correct activity depending on the button pressed.
        button.isPressed(playButton, this, SelectionScreen.class);
        button.isPressed(optionsButton, this, Options.class);
        button.isPressed(creditsButton, this, Credits.class);
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
    //  When we are leaving the activity, we no     //
    //  longer want to listen out for sensor data,  //
    //  so unregister the sensor handler.           //
    //////////////////////////////////////////////////
    @Override
    protected void onPause()
    {
        super.onPause();
        sensorHandler.unregisterListener();
    }
}
