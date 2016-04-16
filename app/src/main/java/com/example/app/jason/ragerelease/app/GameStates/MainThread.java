// The package location for this class.
package com.example.app.jason.ragerelease.app.GameStates;

import android.content.SharedPreferences;

import com.example.app.jason.ragerelease.app.GameStates.Multiplayer.MultiplayerGame;
import com.example.app.jason.ragerelease.app.GameStates.SinglePlayer.SinglePlayerGame;

/**
 * Created by Jason Mottershead on 08/07/2015.
 */

// MainThread IS A Thread, therefore inherits from it.
public class MainThread extends Thread
{
    // Attributes.
    // Private attributes.
    private static long FPS = 0;
    private boolean isRunning = false;
    private SinglePlayerGame singlePlayerGameView = null;
    private MultiplayerGame multiplayerGameView = null;
    private boolean multiplayerStatus = false;
    private static final String PREFS_NAME = "MyPrefsFile";
    private final String multiplayerKeyName = "multiplayer";

    // Methods.
    //////////////////////////////////////////////////
    //					Constructor				    //
    //==============================================//
    // This will set up the main thread attributes, //
    // some passed down from the single player game //
    // activity.                                    //
    //////////////////////////////////////////////////
    public MainThread(final SinglePlayerGame singlePlayerGame, final long desiredFPS)
    {
        // Setting up the main thread constructor.
        super();

        // Initialising local attributes.
        singlePlayerGameView = singlePlayerGame;
        FPS = desiredFPS;
        multiplayerStatus = false;
    }

    //////////////////////////////////////////////////
    //					Constructor				    //
    //==============================================//
    // This will set up the main thread attributes, //
    // some passed down from the multiplayer game   //
    // activity.                                    //
    //////////////////////////////////////////////////
    public MainThread(final MultiplayerGame multiplayerGame, final long desiredFPS)
    {
        // Setting up the main thread constructor.
        super();

        // Initialising local attributes.
        multiplayerGameView = multiplayerGame;
        FPS = desiredFPS;

        // Loading multiplayer status for repeated use.
        SharedPreferences multiplayerSettings = multiplayerGame.getSharedPreferences(PREFS_NAME, multiplayerGame.MODE_PRIVATE);
        multiplayerStatus = multiplayerSettings.getBoolean(multiplayerKeyName, true);
    }

    //////////////////////////////////////////////////
    //					   Run					    //
    //==============================================//
    // This will update the game every frame and be //
    // used to keep a steady frame rate. Hopefully  //
    // keep around 60FPS.                           //
    //////////////////////////////////////////////////
    @Override
    public void run()
    {
        // Setting up attributes to make the main thread work correctly.
        float dt = 0.0f;
        int skipTicks = 1000 / (int) FPS;
        int maxFrameSkip = 10;
        long nextGameTick = System.currentTimeMillis();
        int loops = 0;

        // While the singlePlayerGame thread is currently running.
        while (isRunning)
        {
            loops = 0;

            // While the current time is greater than the next game frame/tick.
            // AND the number of loops is less than the maximum number of frames skipped.
            while ((System.currentTimeMillis() > nextGameTick) && (loops < maxFrameSkip))
            {
                if(multiplayerStatus)
                {
                    multiplayerGameView.update(dt);
                }
                else
                {
                    // Update the singlePlayerGame.
                    singlePlayerGameView.update(dt);
                }

                // Add on any skipped frames.
                nextGameTick += skipTicks;

                // Increment the loop.
                loops++;
            }

            // Used to work out any disposition in the skipped frames.
            dt = ((float) ((System.currentTimeMillis() + skipTicks) - nextGameTick) / (float) skipTicks);
        }
    }

    // Setters.
    // This function will set the private running attribute of this thread.
    public void setRunning(final boolean running) {
        isRunning = running;
    }
}