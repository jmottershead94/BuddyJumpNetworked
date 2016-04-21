// The package location for this class.
package com.example.app.jason.ragerelease.app.GameStates.Multiplayer;

// All of the extra includes here.
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.Debug.DebugInformation;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkConstants;
import com.example.app.jason.ragerelease.app.GameStates.MainMenu;
import com.example.app.jason.ragerelease.app.GameStates.PlayerSelection;

/**
 * Created by Jason Mottershead on 11/04/2016.
 */

// Multiplayer selection IS A network activity, therefore inherits from it.
// This class will allow both players to start the game with their current sprite image.
public class MultiplayerSelection extends NetworkActivity implements View.OnClickListener
{
    // Attributes.
    // Private.
    private Button companionSelectionButton = null;                 // The companion sprite selection button.
    private Button playGameButton = null;                           // The button used to start the game.
    private int playerMatchStatus = 0;                              // The current match ID for the player.
    private int playerImage = 0;                                    // The current player sprite image.
    private static final String PREFS_NAME = "MyPrefsFile";         // Where the options will be saved to, whether they are true or false.
    private final String peerImageIndexKey = "peerImageKey";        // The key used to store the current peer image index.
    private int peerImage = 0;                                      // The current peer sprite image.
    private int[] playerImages =                                    // The different images both players can use.
    {
            R.drawable.p1_front, R.drawable.p2_front,
            R.drawable.p3_front, R.drawable.p4_front,
            R.drawable.p5_front, R.drawable.p6_front,
            R.drawable.p7_front, R.drawable.p8_front
    };
    private boolean checkPeerStatus = false;                        // This will notify us of when we are able to check the status of the other peer.
    private int numberOfPlayersReady = 1;                           // The current number of players that are ready.
    private final Handler checkPeerReady = new Handler();           // Our handler for handling whether or not both players are ready.
    private Runnable runnable = new Runnable()                      // Our runnable for providing the means to check if both players are ready.
    {
        //////////////////////////////////////////////////
        //                      Run                     //
        //==============================================//
        // This will check the enabled status for both  //
        // bluetooth and wifi, and set our button       //
        // text accordingly.                            //
        // We will also handle our message box response //
        // here.                                        //
        //////////////////////////////////////////////////
        @Override
        public void run()
        {
            // If we should check our peer values.
            if(checkPeerStatus)
            {
                // If we are hosting a match.
                if (playerMatchStatus == NetworkConstants.HOST_ID)
                {
                    // Check to see if our peer is ready.
                    if (connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getServerTask().isPlayerReady())
                    {
                        // If so, increase the number of ready players.
                        numberOfPlayersReady++;
                    }
                }
                // Otherwise, if we are joining a match.
                else if (playerMatchStatus == NetworkConstants.JOIN_ID)
                {
                    // Check to see if our peer is ready.
                    if (connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getClientTask().isPlayerReady())
                    {
                        // If so, increase the number of ready players.
                        numberOfPlayersReady++;
                    }
                }

                // If we have two players that are ready.
                if(numberOfPlayersReady == 2)
                {
                    // Stop this handler from running anymore.
                    checkPeerReady.removeCallbacks(this);

                    // Start the multiplayer game.
                    startGame();
                }
            }

            // Check this every second.
            checkPeerReady.postDelayed(this, 1000);
        }
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

        // Accessing shared preferences.
        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Getting the player image index.
        playerImage = gameSettings.getInt("mplayerImageIndex", 0);

        // Initialising the attributes.
        final TextView title = (TextView) findViewById(R.id.gameTextView);
        final Button playerSelectionButton = (Button) findViewById(R.id.playerSelectionButton);
        final Button mainMenuButton = (Button) findViewById(R.id.mainMenuButton);
        final NavigationButton button = new NavigationButton();
        playGameButton = (Button) findViewById(R.id.playGameButton);
        companionSelectionButton = (Button) findViewById(R.id.companionSelectionButton);
        playerMatchStatus = getIntent().getIntExtra(NetworkConstants.EXTRA_PLAYER_MATCH_STATUS, 0);

        // If the main menu button has been pressed.
        // Navigate the user back to the main menu.
        button.isPressed(mainMenuButton, this, MainMenu.class);

        // Setting the title of the activity.
        title.setText("Lobby");

        // You can only use the sprite that you have already selected.
        playerSelectionButton.setVisibility(View.GONE);

        // In multiplayer there are no companions.
        companionSelectionButton.setVisibility(View.GONE);

        // Setting on click listener for the button.
        playGameButton.setOnClickListener(this);

        // Check to see if both peers are ready.
        checkPeerReady.post(runnable);
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
    //  This will save the current peer image index //
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

        // Accessing shared preferences.
        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = gameSettings.edit();

        // If we are hosting a game.
        if(playerMatchStatus == NetworkConstants.HOST_ID)
        {
            // If our wifi is already enabled.
            if(wifiManager.isWifiEnabled())
            {
                // Saving the peer image index.
                editor.putInt(peerImageIndexKey, playerImages[connectionApplication.getServerPeerIndexImage()]);
            }
        }
        // Otherwise, if we are joining a game.
        else if(playerMatchStatus == NetworkConstants.JOIN_ID)
        {
            // If our wifi is already enabled.
            if(wifiManager.isWifiEnabled())
            {
                // Saving the peer image index.
                editor.putInt(peerImageIndexKey, playerImages[connectionApplication.getClientPeerIndexImage()]);
            }
        }

        // Save the changes.
        editor.apply();
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
        // If we want to play the game.
        if(view == playGameButton)
        {
            // If wifi is already enabled.
            if(wifiManager.isWifiEnabled())
            {
                // We should now check the status of our peers.
                checkPeerStatus = true;

                // If we are hosting a match.
                if(playerMatchStatus == NetworkConstants.HOST_ID)
                {
                    // Display a message to the user, telling them that they should wait for a response from their peer.
                    DebugInformation.displayShortToastMessage(this, "Waiting for your peer to respond");

                    // Set the current network state to tell our peer that we are ready.
                    connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getServerTask().setState(NetworkConstants.STATE_TELL_PEER_READY);
                }
                // Otherwise, we are joining a match.
                else if(playerMatchStatus == NetworkConstants.JOIN_ID)
                {
                    // Display a message to the user, telling them that they should wait for a response from their peer.
                    DebugInformation.displayShortToastMessage(this, "Waiting for your peer to respond");

                    // Set the current network state to tell our peer that we are ready.
                    connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getClientTask().setState(NetworkConstants.STATE_TELL_PEER_READY);
                }
            }
        }
    }

    //////////////////////////////////////////////////
    //                   Start Game                 //
    //==============================================//
    // In here we will set up the start of the      //
    // multiplayer game.                            //
    // We will first send over the image index to   //
    // our peer, and then we will set up game       //
    // messages.                                    //
    //////////////////////////////////////////////////
    private void startGame()
    {
        // Setting up the multiplayer game activity.
        final Intent gameActivity = new Intent(this, MultiplayerGame.class);

        // If we are hosting a match.
        if (playerMatchStatus == NetworkConstants.HOST_ID)
        {
            // Send the current player image index to the other player via the server thread.
            connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getServerTask().setServerPeerImage(playerImage);
            connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getServerTask().setState(NetworkConstants.STATE_SEND_IMAGE_MESSAGE);
            gameActivity.putExtra(NetworkConstants.EXTRA_PLAYER_MATCH_STATUS, NetworkConstants.HOST_ID);
        }
        // Otherwise, we are joining a match.
        else if (playerMatchStatus == NetworkConstants.JOIN_ID)
        {
            // Send the current player image index to the other player via the client thread.
            connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getClientTask().setClientPeerImage(playerImage);
            connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getClientTask().setState(NetworkConstants.STATE_SEND_IMAGE_MESSAGE);
            gameActivity.putExtra(NetworkConstants.EXTRA_PLAYER_MATCH_STATUS, NetworkConstants.JOIN_ID);
        }

        // Activity reference in order to display toast messages.
        final Activity activityReference = this;

        // Create a delay for setting up game messages in time for the multiplayer game.
        final Handler gameDelayHandler = new Handler();
        gameDelayHandler.postDelayed(new Runnable()
        {
            //////////////////////////////////////////////////
            //                      Run                     //
            //==============================================//
            // In this function we will set up the game     //
            // messages to start running in order to have   //
            // them set up correctly for the multiplayer    //
            // game.                                        //
            //////////////////////////////////////////////////
            @Override
            public void run()
            {
                // If our wifi is already enabled.
                if(wifiManager.isWifiEnabled())
                {
                    // If we are hosting a match.
                    if (playerMatchStatus == NetworkConstants.HOST_ID)
                    {
                        // Show the peer image number.
                        DebugInformation.displayShortToastMessage(activityReference, "Client Image: " + connectionApplication.getServerPeerIndexImage());

                        // Set the current network state to start sending game messages.
                        connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getServerTask().setGameIsRunning(true);
                        connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getServerTask().setState(NetworkConstants.STATE_SEND_GAME_MESSAGES);
                    }
                    // Otherwise, we are joining a match.
                    else if (playerMatchStatus == NetworkConstants.JOIN_ID)
                    {
                        // Show the peer image number.
                        DebugInformation.displayShortToastMessage(activityReference, "Server Image: " + connectionApplication.getClientPeerIndexImage());

                        // Set the current network state to start sending game messages.
                        connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getClientTask().setGameIsRunning(true);
                        connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getClientTask().setState(NetworkConstants.STATE_SEND_GAME_MESSAGES);
                    }
                }
                // Otherwise, our wifi is not already enabled.
                else
                {
                    // The user has disconnected.
                    userDisconnected();
                }
            }
        }, 6000);

        // Create a delay for starting the multiplayer game activity.
        final Handler startGameHandler = new Handler();
        startGameHandler.postDelayed(new Runnable()
        {
            //////////////////////////////////////////////////
            //                      Run                     //
            //==============================================//
            // In this function we will start the           //
            // multiplayer game activity.                   //
            //////////////////////////////////////////////////
            @Override
            public void run()
            {
                // If our wifi is already enabled.
                if(wifiManager.isWifiEnabled())
                {
                    // Start the multiplayer game activity.
                    startActivity(gameActivity);
                }
                // Otherwise, our wifi is not already enabled.
                else
                {
                    // The user has disconnected.
                    userDisconnected();
                }
            }
        }, 12000);
    }
}
