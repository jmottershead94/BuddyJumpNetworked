// The package location for this activity.
package com.example.app.jason.ragerelease.app.GameStates.Multiplayer;

// All of the extra includes here.
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.Debug.DebugInformation;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkConstants;
import com.example.app.jason.ragerelease.app.GameStates.MainMenu;

/**
 * Created by Jason Mottershead on 22/03/2016.
 */

// Match Maker IS A network activity, therefore inherits from it.
// This class will allow the player to choose if they are hosting or joining a game.
public class MatchMaker extends NetworkActivity implements View.OnClickListener
{
    // Attributes.
    // Private.
    private Button hostButton = null;   // The button that will allow us to host a game.
    private Button joinButton = null;   // The button that will allow us to join a game.

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
        setContentView(R.layout.activity_match_maker);

        // Initialising our attributes.
        final Button mainMenuButton = (Button) findViewById(R.id.matchMakerMainMenuButton);
        final Button backButton = (Button) findViewById(R.id.matchMakerBackButton);
        final NavigationButton button = new NavigationButton();
        hostButton = (Button) findViewById(R.id.hostButton);
        joinButton = (Button) findViewById(R.id.joinButton);

        // Setting the on click listeners for our buttons.
        hostButton.setOnClickListener(this);
        joinButton.setOnClickListener(this);

        // Checking the enabled status for our buttons.
        checkEnabledStatusButton(hostButton);
        checkEnabledStatusButton(joinButton);

        // If any of the buttons are pressed on the match maker screen.
        button.isPressed(mainMenuButton, this, MainMenu.class);
        button.isPressed(backButton, this, ConnectionSelection.class);
    }

    //////////////////////////////////////////////////
    //                  On Click                    //
    //==============================================//
    // Here we will listen our for any clicks on    //
    // our buttons, and complete specific tasks     //
    // depending on what we need to do.             //
    //////////////////////////////////////////////////
    @Override
    public void onClick(View view)
    {
        // Setting up the next activity.
        // This will be different depending on if you are hosting or joining a game.
        Intent nextActivity = null;

        // If our wifi is already enabled.
        if(wifiManager.isWifiEnabled())
        {
            // If the user wants to host a match.
            if (view == hostButton)
            {
                // Make the next activity the multiplayer selection screen.
                nextActivity = new Intent(this, MultiplayerSelection.class);

                // Discover other devices.
                searchForDevices();

                // Display a message to the user telling them that they are hosting.
                DebugInformation.displayShortToastMessage(this, "Hosting - wait here for player 2...");

                // Place in the host ID for the player match status in an intent.
                nextActivity.putExtra(NetworkConstants.EXTRA_PLAYER_MATCH_STATUS, NetworkConstants.HOST_ID);
            }
            // Otherwise, if the user wants to join a match.
            else if (view == joinButton)
            {
                // Make the next activity the device list screen.
                nextActivity = new Intent(this, DeviceList.class);

                // Display a message to the user telling them that they are joining.
                DebugInformation.displayShortToastMessage(this, "Joining - scan and select devices");

                // Place in the join ID for the player match status in an intent.
                nextActivity.putExtra(NetworkConstants.EXTRA_PLAYER_MATCH_STATUS, NetworkConstants.JOIN_ID);
            }

            // Go to the next game state.
            startActivity(nextActivity);
        }
        // Otherwise, our wifi is not already enabled.
        else
        {
            // If we press on either the host or join button.
            if(view == hostButton || view == joinButton)
            {
                // The user has disconnected.
                userDisconnected();
            }
        }
    }
}
