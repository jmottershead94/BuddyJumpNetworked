// The package location of this class.
package com.example.app.jason.ragerelease.app.GameStates;

// All of the extra includes here.
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.CharacterSelection;

/**
 * Created by Jason Mottershead on 17/10/2015.
 */

// Player Selection IS A Character Selection, therefore inherits from it.
// This will allow us to place in any images we want the player to be able to chose from.
public class PlayerSelection extends CharacterSelection
{
    // Attributes.
    // Private.
    private int currentPlayerImageIndex = 0;
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
        setContentView(R.layout.activity_character_selection);

        // Initialise the character selection screen.
        // What the message should say, and what the preference file attribute should be called.
        init("player", "mplayerImageIndex", "mplayerImage");
        applyOptions(this, playerImages, true);

        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentPlayerImageIndex = gameSettings.getInt("mplayerImageIndex", 0);

        Toast.makeText(this, "Player currently using " + currentPlayerImageIndex, Toast.LENGTH_SHORT).show();
    }
}
