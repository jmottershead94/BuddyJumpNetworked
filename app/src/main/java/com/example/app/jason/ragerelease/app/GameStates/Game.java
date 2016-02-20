// The package location of this class.
package com.example.app.jason.ragerelease.app.GameStates;

// All of the extra includes here.
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.ImageAdapter;
import com.example.app.jason.ragerelease.app.Framework.Level;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;
import com.example.app.jason.ragerelease.app.Framework.Player;
import com.example.app.jason.ragerelease.app.Framework.Resources;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Jason Mottershead on 17/10/2015.
 */

// Game IS AN Activity, therefore inherits from it.
public class Game extends Activity
{
    // Attributes.
    // Private attributes.
    // Standard library attributes.
    private static final long desiredFPS = 60;                                  // The desired frame rate for the game.
    private static final String PREFS_NAME = "MyPrefsFile";                     // Where the options will be saved to, whether they are true or false.
    private int playerImage = 0;                                                // The current player image.
    private int companionImage = 0;                                             // The current companion image.

    // Android attributes.
    private RelativeLayout background = null;                                   // Gives access to the relative layout background for the game.
    private Button pauseButton = null;                                          // Gives access to the pause button for the player.

    // JBox2D attributes.
    private World world;                                                        // Gives access to the jbox2D physics engine.
    private Vec2 gravity = null;                                                // Will be applied to dynamic objects.

    // My framework attributes.
    private MainThread gameThread = null;                                       // The main game thread that the game will be running mostly on.
    private Level level = null;                                                 // Provides levels for the player to play in.
    private Resources resources = null;                                         // Gives access to certain repeated resources (context, the game background, screen width , screen height, and the world), and narrows down parameters passed down.

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
        setContentView(R.layout.activity_game);

        // Initialise the game.
        init();

        // Setting the game thread to run.
        gameThread.setRunning(true);
        gameThread.start();
    }

    //////////////////////////////////////////////////////////
    //======================================================//
    //					    createWorld						//
    //======================================================//
    // This will create the jbox2D world, so that the       //
    // jbox2D library can be applied to this project.       //
    // This will also create gravity for any dynamic        //
    // objects.                                             //
    //////////////////////////////////////////////////////////
    public void createWorld()
    {
        gravity = new Vec2(0.0f, -10.0f);
        world = new World(gravity, false);
    }

    //////////////////////////////////////////////////
    //                   Init                       //
    //==============================================//
    //  This will initialise the game, load in      //
    //  options, set up Box2D, and set up the game  //
    //  thread.                                     //
    //////////////////////////////////////////////////
    private void init()
    {
        // Load in options here...
        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        playerImage = gameSettings.getInt("mplayerImage", 0);
        companionImage = gameSettings.getInt("mcompanionImage", 0);

        // Setting up the screen dimensions.
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        // Setting up JBox2D.
        createWorld();

        // Initialising variables.
        background = (RelativeLayout) findViewById(R.id.gameBackground);
        pauseButton = (Button) findViewById(R.id.pauseButton);
        level = new Level();
        gameThread = new MainThread(this, desiredFPS);
        resources = new Resources(this, getApplicationContext(), background, displayMetrics.widthPixels, displayMetrics.heightPixels, world);

        // Initialising the level.
        //level.init(resources, this, playerImages[playerImageIndex], enemyImages[enemyImageIndex]);
        level.init(resources, this, playerImage, companionImage);
    }

    //////////////////////////////////////////////////
    //				        Update   				//
    //==============================================//
    // This function will be called every frame.    //
    // All update calls will be processed here.     //
    //////////////////////////////////////////////////
    public void update(float dt)
    {
        // Setting up the time step and iterations for jbox2D.
        float timeStep = 1.0f / desiredFPS;
        int velocityIterations = 6;
        int positionIterations = 4;

        checkGameOver();

        // If the player has not paused the game and the game is not yet over.
        if(!level.player.isPaused() && !level.player.isGameOver())
        {
            // Update the physics engine.
            resources.getWorld().step(timeStep, velocityIterations, positionIterations);

            // All other update calls here.
            // Update the level.
            level.update(dt);
        }

        // If the game is paused, the player should be able to unpause the game.
        // They can still control the pause menu even if the game is paused.
        level.player.uiControls(pauseButton);
    }

    //////////////////////////////////////////////////
    //                 Check Game Over              //
    //==============================================//
    //  This will check to see if the player in the //
    //  level is in a game over state.              //
    //  If so, we should change over to the game    //
    //  over activity.                              //
    //////////////////////////////////////////////////
    private void checkGameOver()
    {
        if(level.player.isGameOver())
        {
            // Return to the main menu.
            Intent intent = new Intent(this, GameOver.class);

            // Clear the current level.
            level.levelGenerator.clearLevel();
            level.player.setGameOver(false);
            level.player.setPaused(false);

            // Go back to the main menu.
            startActivity(intent);
        }
    }

    //////////////////////////////////////////////////////////
    //======================================================//
    //				        render   						//
    //======================================================//
    // This function will update the game canvas when the   //
    // screen updates, when the screen is "dirty".          //
    // We do this on the UI thread so that there are no     //
    // conflicts with the original thread that added the    //
    // views in the first place.                            //
    // If this were to run normally without running on      //
    // the UI thread, errors would occur.                   //
    //////////////////////////////////////////////////////////
    public void render()
    {
        // Creating a new thread.
        new Thread()
        {
            // When this new thread runs.
            @Override
            public void run()
            {
                // Try to...
                try
                {
                    // Make sure that the new level objects are added to the background on the correct thread.
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // Add all of the randomly generated objects whenever I want to the level.
                            level.addToView();
                            level.player.distanceText.bringToFront();
                        }
                    });

                    // Slight loading time, without this, below catch gives an error.
                    Thread.sleep(500);
                }
                // Catch any expections with this thread.
                catch (InterruptedException e)
                {
                    // Print a stack trace so we know where we went wrong.
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //////////////////////////////////////////////////
    //              On Save Instance State          //
    //==============================================//
    //  This will save the current player distance. //
    //  This is called if the phone orientation     //
    //  changes, or if for any reason the phone     //
    //  is forced out of this activity and into     //
    //  another application (i.e. like a phone      //
    //  call).                                      //
    //////////////////////////////////////////////////
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        // Save UI changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is killed or restarted.
        savedInstanceState.putInt("mplayerDistance", level.player.distance);
        savedInstanceState.putInt("mlevelNumber", level.levelNumber);

        super.onSaveInstanceState(savedInstanceState);
    }

    //////////////////////////////////////////////////
    //                  On Pause                    //
    //==============================================//
    //  This will save the current player distance, //
    //  and save it to the device for future        //
    //  reference.                                  //
    //  This will be called when we are leaving     //
    //  this activity.                              //
    //  When another activity is in the foreground. //
    //////////////////////////////////////////////////
    @Override
    protected void onPause()
    {
        super.onPause();

        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = gameSettings.edit();

        editor.putInt("mplayerDistance", level.player.distance);
        editor.putInt("mlevelNumber", level.levelNumber);

        editor.apply();
    }
}
