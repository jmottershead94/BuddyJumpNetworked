// The package location for this class.
package com.example.app.jason.ragerelease.app.GameStates;

// All of the extra includes here.
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;

/**
 * Created by Jason Mottershead on 14/10/2015.
 */

// TitleScreen IS AN Activity, therefore inherits from it.
// This class will create a title screen, and will provide access to the main menu.
public class TitleScreen extends Activity
{
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
        setContentView(R.layout.activity_title_screen);

        // Setting up the title screen button.
        final Button startButton = (Button) findViewById(R.id.startButton);
        final NavigationButton button = new NavigationButton();

        // If the start button has been pressed.
        // Navigate to the main menu activity.
        button.isPressed(startButton, this, MainMenu.class);
    }
}
