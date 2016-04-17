// The package location of this class.
package com.example.app.jason.ragerelease.app.GameStates.Multiplayer;

// All of the extra includes here.
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.Level;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkConstants;
import com.example.app.jason.ragerelease.app.Framework.Resources;
import com.example.app.jason.ragerelease.app.GameStates.GameOver;
import com.example.app.jason.ragerelease.app.GameStates.MainThread;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

/**
 * Created by Jason Mottershead on 12/04/2016.
 */

// Multiplayer game IS A network activity, therefore inherits from it.
// This class will be in charge of handling the multiplayer game session.
public class MultiplayerGame extends NetworkActivity
{
    // Attributes.
    // Private attributes.
    // Standard library attributes.
    private static final long desiredFPS = 60;                                  // The desired frame rate for the singlePlayerGame.
    private static final String PREFS_NAME = "MyPrefsFile";                     // Where the options will be saved to, whether they are true or false.
    private final String peerImageIndexKey = "peerImageKey";                    // The key used to store the current peer image index.
    private int playerImage = 0;                                                // The current player image.
    private int peerImage = 0;                                                  // The current companion image.
    private int playerMatchStatus = 0;
    private Integer peerImageInteger = 0;

    // Android attributes.
    private RelativeLayout background = null;                                   // Gives access to the relative layout background for the singlePlayerGame.
    private Button pauseButton = null;                                          // Gives access to the pause button for the player.

    // JBox2D attributes.
    private World world;                                                        // Gives access to the jbox2D physics engine.
    private Vec2 gravity = null;                                                // Will be applied to dynamic objects.

    // My framework attributes.
    private MainThread gameThread = null;                                       // The main singlePlayerGame thread that the singlePlayerGame will be running mostly on.
    private Level level = null;                                                 // Provides levels for the player to play in.
    private Resources resources = null;                                         // Gives access to certain repeated resources (context, the singlePlayerGame background, screen width , screen height, and the world), and narrows down parameters passed down.

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

        // Initialise the singlePlayerGame.
        init();

        // Setting the game thread to run.
        gameThread.setRunning(true);
        gameThread.start();
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
        savedInstanceState.putInt(NetworkConstants.EXTRA_PEER_INDEX, peerImage);

        super.onSaveInstanceState(savedInstanceState);
    }

    //////////////////////////////////////////////////
    //            On Restore Instance State         //
    //==============================================//
    //  This will place the current selected image  //
    //  index back into the local int attribute.    //
    //  This will be called once the application    //
    //  returns to this activity when being         //
    //  previously forced out of it. From a phone   //
    //  call or a screen rotation.                  //
    //////////////////////////////////////////////////
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        // Just in case the application is killed off.
        super.onRestoreInstanceState(savedInstanceState);

        // Once the activity has been restored, place the previous image index into the current one.
        // So that we have not lost the number for it.
        peerImage = savedInstanceState.getInt(NetworkConstants.EXTRA_PEER_INDEX);
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
        //playerMatchStatus = gameSettings.getInt(NetworkConstants.EXTRA_PLAYER_MATCH_STATUS, 0);
        playerMatchStatus = getIntent().getIntExtra(NetworkConstants.EXTRA_PLAYER_MATCH_STATUS, 0);

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
        //connectionApplication.getConnectionManagement().setNetworkActivity(this);
        //resources = new Resources(this, getApplicationContext(), background, displayMetrics.widthPixels, displayMetrics.heightPixels, world);
        resources = new Resources(this, getApplicationContext(), background, displayMetrics.widthPixels, displayMetrics.heightPixels, world, connectionApplication);

        pauseButton.setVisibility(View.GONE);

        //peerImageInteger = getIntent().getIntExtra(NetworkConstants.EXTRA_PEER_INDEX, 0);

        // Initialising the level.
        //level.init(resources, this, playerImages[playerImageIndex], enemyImages[enemyImageIndex]);
        //level.init(resources, this, playerImage, companionImage);

        if(playerMatchStatus == NetworkConstants.HOST_ID)
        {
            peerImageInteger = connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getServerAsyncTask().getPeerImageIndexInt();

            //DebugInformation.displayShortToastMessage(this, "Client image index: " + peerImageInteger);

            // Initialise the level.
            level.init(resources, this, playerImage, playerMatchStatus, connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getServerAsyncTask().getPeerImageIndexInt(), this);

            //level.init(resources, this, playerImage, playerMatchStatus, peerImageInteger, this);
        }
        else if(playerMatchStatus == NetworkConstants.JOIN_ID)
        {
            peerImageInteger = connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getClientAsyncTask().getPeerImageIndexInt();

            //DebugInformation.displayShortToastMessage(this, "Server image index: " + peerImageInteger);

            // Initialise the level.
            level.init(resources, this, playerImage, playerMatchStatus, connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getClientAsyncTask().getPeerImageIndexInt(), this);

            //level.init(resources, this, playerImage, playerMatchStatus, peerImageInteger, this);
        }

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

        // If the player has not paused the singlePlayerGame and the singlePlayerGame is not yet over.
        if(!level.player.isGameOver())
        {
            // Update the physics engine.
            resources.getWorld().step(timeStep, velocityIterations, positionIterations);

            // All other update calls here.
            // Update the level.
            level.update(dt);
        }

        // If the game is paused, the player should be able to unpause the singlePlayerGame.
        // They can still control the pause menu even if the singlePlayerGame is paused.
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
}
