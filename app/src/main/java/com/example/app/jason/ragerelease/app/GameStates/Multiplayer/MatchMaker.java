// The package location of this activity.
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
public class MatchMaker extends NetworkActivity implements View.OnClickListener
{
    // Attributes.
    private Button hostButton = null;
    private Button joinButton = null;

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

        // Setting up each button to access them from the match maker xml file.
        final Button mainMenuButton = (Button) findViewById(R.id.matchMakerMainMenuButton);
        final Button backButton = (Button) findViewById(R.id.matchMakerBackButton);
        final NavigationButton button = new NavigationButton();
        hostButton = (Button) findViewById(R.id.hostButton);
        joinButton = (Button) findViewById(R.id.joinButton);

        hostButton.setOnClickListener(this);
        joinButton.setOnClickListener(this);

        // If any of the buttons are pressed on the match maker screen.
        button.isPressed(mainMenuButton, this, MainMenu.class);
        button.isPressed(backButton, this, ConnectionSelection.class);
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

    @Override
    public void onClick(View view)
    {
        Intent nextActivity = null;

        // If the user wants to host a match.
        if(view == hostButton)
        {
            nextActivity = new Intent(this, MultiplayerSelection.class);

            searchForDevices();

            // Start a server connection.
            DebugInformation.displayShortToastMessage(this, "Hosting");
            nextActivity.putExtra(NetworkConstants.EXTRA_PLAYER_MATCH_STATUS, NetworkConstants.HOST_ID);
        }
        // Otherwise, if the user wants to join a match.
        else if(view == joinButton)
        {
            nextActivity = new Intent(this, DeviceList.class);

            //searchForDevices();

            // Start a client connection.
            DebugInformation.displayShortToastMessage(this, "Joining");
            nextActivity.putExtra(NetworkConstants.EXTRA_PLAYER_MATCH_STATUS, NetworkConstants.JOIN_ID);
        }

        startActivity(nextActivity);
    }
}