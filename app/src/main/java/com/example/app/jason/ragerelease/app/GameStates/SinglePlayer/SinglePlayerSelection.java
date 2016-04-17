// The package location of this class.
package com.example.app.jason.ragerelease.app.GameStates.SinglePlayer;

// All of the extra includes here.
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkConstants;
import com.example.app.jason.ragerelease.app.GameStates.MainMenu;
import com.example.app.jason.ragerelease.app.GameStates.PlayerSelection;

/**
 * Created by Jason Mottershead on 11/04/2016.
 */

// Single player selection IS AN activity, therefore inherits from it.
// This class will be used to allow the player to select their characters for single player use.
public class SinglePlayerSelection extends Activity
{
    // Attributes.
    protected Button companionSelectionButton = null;
    private static final String PREFS_NAME = "MyPrefsFile";         // Where the options will be saved to, whether they are true or false.
    private int[] playerImages =
    {
            R.drawable.p1_front, R.drawable.p2_front,
            R.drawable.p3_front, R.drawable.p4_front,
            R.drawable.p5_front, R.drawable.p6_front,
            R.drawable.p7_front, R.drawable.p8_front
    };

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
        button.isPressed(playGameButton, this, SinglePlayerGame.class);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = gameSettings.edit();

        // Saving the player option status.
        editor.putInt("mplayerImage", playerImages[gameSettings.getInt("mplayerImageIndex", 0)]);

        editor.apply();
    }
}
