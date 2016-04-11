// The package location of this class.
package com.example.app.jason.ragerelease.app.GameStates;

// All of the extra includes here.
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;

/**
 * Created by Jason Mottershead on 11/04/2016.
 */

// Single player selection IS AN activity, therefore inherits from it.
// This class will be used to allow the player to select their characters for single player use.
public class SinglePlayerSelection extends Activity
{
    // Attributes.
    protected Button companionSelectionButton = null;

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
        setContentView(R.layout.activity_selection_screen);

        // Initialising variables.
        final Button playerSelectionButton = (Button) findViewById(R.id.playerSelectionButton);
        final Button mainMenuButton = (Button) findViewById(R.id.mainMenuButton);
        final Button playGameButton = (Button) findViewById(R.id.playGameButton);
        final NavigationButton button = new NavigationButton();
        companionSelectionButton = (Button) findViewById(R.id.companionSelectionButton);

        // If the main menu button has been pressed.
        // Navigate the user back to the main menu.
        button.isPressed(playerSelectionButton, this, PlayerSelection.class);
        button.isPressed(companionSelectionButton, this, CompanionSelection.class);
        button.isPressed(mainMenuButton, this, MainMenu.class);
        button.isPressed(playGameButton, this, Game.class);
    }
}
