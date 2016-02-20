// The package location of this class.
package com.example.app.jason.ragerelease.app.GameStates;

// All of the extra includes here.
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.AndroidOSHandlers.SMSHandler;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;

/**
 * Created by Jason Mottershead on 21/11/2015.
 */

// GameOver IS AN Activity, therefore inherits from it.
// This class will provide a game over screen.
public class GameOver extends Activity implements View.OnClickListener
{
    // Attributes.
    // Private.
    private static final String PREFS_NAME = "MyPrefsFile";
    private Button textAFriendButton = null;

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
        setContentView(R.layout.activity_game_over);

        // Load in options here...
        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final int distance = gameSettings.getInt("mplayerDistance", 0);
        final int levelNumber = gameSettings.getInt("mlevelNumber", 0);

        // Setting up the button to go back to the main menu.
        final Button mainMenuButton = (Button) findViewById(R.id.gameOverMainMenuButton);
        final NavigationButton button = new NavigationButton();
        final TextView distanceText = (TextView) findViewById(R.id.gameOverDistanceText);
        final TextView levelNumberText = (TextView) findViewById(R.id.gameOverLevelNumberText);
        textAFriendButton = (Button) findViewById(R.id.textFriendButton);

        // Getting the final distance score for the player.
        distanceText.setText("Final Distance: " + distance);
        levelNumberText.setText("Level Number: " + levelNumber);

        // If the main menu button has been pressed.
        // Navigate the user back to the main menu.
        button.isPressed(mainMenuButton, this, MainMenu.class);

        // Setting the on click listener for this button.
        textAFriendButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        // If we have clicked on the text a friend button.
        if(view == textAFriendButton)
        {
            // Go to the sms application.
            final SMSHandler smsHandler = new SMSHandler(this);
        }
    }
}