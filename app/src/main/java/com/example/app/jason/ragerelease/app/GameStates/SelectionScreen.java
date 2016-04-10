// The package location of this class.
package com.example.app.jason.ragerelease.app.GameStates;

// All of the extra includes here.
import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Network;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;

/**
 * Created by Jason Mottershead on 17/10/2015.
 */

// Single Player Selection Screen IS AN Activity, therefore inherits from it.
// This will provide the player with the options of selecting their character sprites.
public class SelectionScreen extends NetworkActivity
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
        setContentView(R.layout.activity_selection_screen);

        // Loading multiplayer status for repeated use.
        SharedPreferences multiplayerSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        multiplayerStatus = multiplayerSettings.getBoolean(multiplayerKeyName, false);

        // Initialising variables.
        final Button playerSelectionButton = (Button) findViewById(R.id.playerSelectionButton);
        final Button mainMenuButton = (Button) findViewById(R.id.mainMenuButton);
        final Button playGameButton = (Button) findViewById(R.id.playGameButton);
        final NavigationButton button = new NavigationButton();
        Button companionSelectionButton = (Button) findViewById(R.id.companionSelectionButton);

        // If the main menu button has been pressed.
        // Navigate the user back to the main menu.
        button.isPressed(playerSelectionButton, this, PlayerSelection.class);
        button.isPressed(companionSelectionButton, this, CompanionSelection.class);
        button.isPressed(mainMenuButton, this, MainMenu.class);
        button.isPressed(playGameButton, this, Game.class);

        // If we have accessed this activity from multiplayer.
        if(multiplayerStatus)
        {
            // Remove the companion button.
            companionSelectionButton.setVisibility(View.GONE);
        }
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
}
