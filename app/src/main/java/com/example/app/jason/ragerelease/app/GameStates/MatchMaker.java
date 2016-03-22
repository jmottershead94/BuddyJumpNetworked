// The package location of this activity.
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

// Match Maker IS AN activity, therefore inherits from it.
public class MatchMaker extends Activity
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
        setContentView(R.layout.activity_match_maker);

        // Setting up each button to access them from the match maker xml file.
        final Button hostButton = (Button) findViewById(R.id.hostButton);
        final Button joinButton = (Button) findViewById(R.id.joinButton);
        final Button mainMenuButton = (Button) findViewById(R.id.matchMakerMainMenuButton);
        final Button backButton = (Button) findViewById(R.id.matchMakerBackButton);
        final NavigationButton button = new NavigationButton();

        // If any of the buttons are pressed on the match maker screen.
        button.isPressed(hostButton, this, SelectionScreen.class);
        button.isPressed(joinButton, this, SelectionScreen.class);
        button.isPressed(mainMenuButton, this, MainMenu.class);
        button.isPressed(backButton, this, ConnectionSelection.class);
    }


}
