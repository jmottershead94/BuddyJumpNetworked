// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework;

// All of the extra includes here.
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.app.jason.ragerelease.app.Framework.Maths.Vector2;

import org.jbox2d.common.Vec2;

/**
 * Created by Jason Mottershead on 16/07/2015.
 */

// This class will allow te player to interact with the game.
public class Player
{
    // Attributes.
    // Public attributes.
    public boolean beingTouched = false;
    public boolean tap = false;
    public int distance = 0;
    public TextView distanceText = null;
    public Vector2 touchPosition = null;

    // Private attributes.
    private static boolean paused = false;
    private static boolean gameOver = false;
    private Level level = null;
    private Resources resources = null;

    // Methods.
    //////////////////////////////////////////////////
    //					Constructor                 //
    //==============================================//
    // This will initialise the player object,      //
    // which will call the dynamic body             //
    // initialiser, and will then call the sprite   //
    // initialiser. Local variables will be         //
    // initialised here.                            //
    //////////////////////////////////////////////////
    public Player(final Resources gameResources, final Level gameLevel)
    {
        resources = gameResources;
        touchPosition = new Vector2(0.0f, 0.0f);
        level = gameLevel;

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins((resources.getScreenWidth() - (resources.getScreenWidth() / 3)), (resources.getScreenHeight() / 16), 0, 0);

        distanceText = new TextView(resources.getContext());
        distanceText.setText("Distance: " + distance);
        distanceText.setTextColor(Color.WHITE);
        distanceText.setTextSize(20.0f);
        distanceText.setLayoutParams(layoutParams);
        resources.getBackground().addView(distanceText);
    }

    //////////////////////////////////////////////////
    //                  UI Controls                 //
    //==============================================//
    //  This will allow the player to interact with //
    //  the pause menu.                             //
    //////////////////////////////////////////////////
    public void uiControls(final Button buttonPause)
    {
        // Checking any button click events.
        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click response code here...
                // Display Pause Menu.
                paused = true;
                buttonPause.setVisibility(View.INVISIBLE);
                pauseMenu(buttonPause);
            }
        });
    }

    //////////////////////////////////////////////////
    //                Pause Menu                    //
    //==============================================//
    //  This will draw the pause menu for the       //
    //  player.                                     //
    //////////////////////////////////////////////////
    private void pauseMenu(final Button buttonPause)
    {
        final Button buttonResume = new Button(resources.getContext());

        // Adding the resume button to the display.
        buttonResume.setText("Resume");
        buttonResume.setClickable(true);

        // Only adds in a resume button for the pause menu.
        resources.getBackground().addView(buttonResume);

        // Clicking on the resume button.
        buttonResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Take away pause menu here.
                buttonResume.setVisibility(View.GONE);
                buttonPause.setVisibility(View.VISIBLE);
                paused = false;
            }
        });
    }

    // Setters.
    // This will set the current game over state.
    public static void setGameOver(boolean dead)    { gameOver = dead; }

    // This will set the current pause state.
    public static void setPaused(boolean pause)     { paused = pause; }

    // Getters.
    // Is the game paused or not.
    public static boolean isPaused()                { return paused; }

    // Is the game over.
    public static boolean isGameOver()              { return gameOver; }
}
