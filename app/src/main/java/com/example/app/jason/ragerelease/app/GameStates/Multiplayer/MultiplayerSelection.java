// The package location of this class.
package com.example.app.jason.ragerelease.app.GameStates.Multiplayer;

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
 * Created by Win8 on 11/04/2016.
 */

// Multiplayer selection IS A network activity, therefore inherits from it.
// This class will allow both players to select their character sprites.
public class MultiplayerSelection extends NetworkActivity implements View.OnClickListener
{
    // Attributes.
    protected Button companionSelectionButton = null;
    private Button playGameButton = null;
    private int playerMatchStatus = 0;
    private int playerImage = 0;
    private static final String PREFS_NAME = "MyPrefsFile";         // Where the options will be saved to, whether they are true or false.
    private final String peerImageIndexKey = "peerImageKey";        // The key used to store the current peer image index.
    private int peerImage = 0;
    private Integer peerImageIndexInteger = 0;
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

        // Load in options here...
        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        //playerImage = gameSettings.getInt("mplayerImage", 0);
        playerImage = gameSettings.getInt("mplayerImageIndex", 0);

        // Initialising variables.
        final TextView title = (TextView) findViewById(R.id.gameTextView);
        final Button playerSelectionButton = (Button) findViewById(R.id.playerSelectionButton);
        final Button mainMenuButton = (Button) findViewById(R.id.mainMenuButton);
        final NavigationButton button = new NavigationButton();
        playGameButton = (Button) findViewById(R.id.playGameButton);
        companionSelectionButton = (Button) findViewById(R.id.companionSelectionButton);
        playerMatchStatus = getIntent().getIntExtra(NetworkConstants.EXTRA_PLAYER_MATCH_STATUS, 0);

        // If the main menu button has been pressed.
        // Navigate the user back to the main menu.
        button.isPressed(playerSelectionButton, this, PlayerSelection.class);
        button.isPressed(mainMenuButton, this, MainMenu.class);
        //button.isPressed(playGameButton, this, MultiplayerGame.class);

        // We are using multiplayer, there are no companions.
        title.setText("Lobby");
        playerSelectionButton.setVisibility(View.GONE);
        companionSelectionButton.setVisibility(View.GONE);
        playGameButton.setOnClickListener(this);

//        DebugInformation.displayShortToastMessage(this, "Image index: " + playerImage);
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
        //savedInstanceState.putInt("mplayerDistance", level.player.distance);
        //savedInstanceState.putInt("mlevelNumber", level.levelNumber);
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

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = gameSettings.edit();

        if(playerMatchStatus == NetworkConstants.HOST_ID)
        {
//            if(connectionApplication.getServerPeerIndexImage() != null)
//            {
                // Saving the player option status.
                editor.putInt(peerImageIndexKey, playerImages[connectionApplication.getServerPeerIndexImage()]);
//            }
        }
        else if(playerMatchStatus == NetworkConstants.JOIN_ID)
        {
            editor.putInt(peerImageIndexKey, playerImages[connectionApplication.getClientPeerIndexImage()]);
        }

        editor.apply();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onClick(View view)
    {
        if(view == playGameButton)
        {
            final Intent gameActivity = new Intent(this, MultiplayerGame.class);

            // If we are hosting a match.
            if(playerMatchStatus == NetworkConstants.HOST_ID)
            {
                // Send the current player image index to the other player via the server.
                connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getServerTask().setServerPeerImage(playerImage);
                connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getServerTask().setState(NetworkConstants.STATE_SEND_IMAGE_MESSAGE);
                gameActivity.putExtra(NetworkConstants.EXTRA_PLAYER_MATCH_STATUS, NetworkConstants.HOST_ID);
            }
            else if(playerMatchStatus ==  NetworkConstants.JOIN_ID)
            {
                connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getClientTask().setClientPeerImage(playerImage);
                connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getClientTask().setState(NetworkConstants.STATE_SEND_IMAGE_MESSAGE);
                gameActivity.putExtra(NetworkConstants.EXTRA_PLAYER_MATCH_STATUS, NetworkConstants.JOIN_ID);
            }

            final Activity activityReference = this;

            // Create a delay.
            final Handler debugHandler = new Handler();
            debugHandler.postDelayed(new Runnable()
            {
                // After 6 seconds.
                @Override
                public void run()
                {
                    if(playerMatchStatus == NetworkConstants.HOST_ID)
                    {
                        DebugInformation.displayShortToastMessage(activityReference, "Client Image: " + connectionApplication.getServerPeerIndexImage());

                        connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getServerTask().setGameIsRunning(true);
                        connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getServerTask().setState(NetworkConstants.STATE_SEND_GAME_MESSAGES);
                    }
                    else if(playerMatchStatus == NetworkConstants.JOIN_ID)
                    {
                        DebugInformation.displayShortToastMessage(activityReference, "Server Image: " + connectionApplication.getClientPeerIndexImage());

                        connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getClientTask().setGameIsRunning(true);
                        connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getClientTask().setState(NetworkConstants.STATE_SEND_GAME_MESSAGES);
                    }
                }
            }, 6000);

            // Create a delay.
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable()
            {
                // After 6 seconds.
                @Override
                public void run()
                {
//                    if(playerMatchStatus == NetworkConstants.HOST_ID)
//                    {
//                        connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getServerTask().setGameIsRunning(true);
//                        connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getServerTask().setState(NetworkConstants.STATE_SEND_GAME_MESSAGES);
//                    }
//                    else if(playerMatchStatus == NetworkConstants.JOIN_ID)
//                    {
//                        connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getClientTask().setGameIsRunning(true);
//                        connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getClientTask().setState(NetworkConstants.STATE_SEND_GAME_MESSAGES);
//                    }

                    startActivity(gameActivity);
                }
            }, 12000);


        }
    }

//    // This class will handle sending over the image index to the other peer.
//    public class SendImageMessage extends Thread
//    {
//        @Override
//        public void run()
//        {
//            try
//            {
//                // Get access to the singlePlayerGame options.
//                // Accessing the player image index.
//                SharedPreferences gameSettings = getSharedPreferences(SavedFileConstants.PREFS_NAME, MODE_PRIVATE);
//                int playerImage = gameSettings.getInt(SavedFileConstants.PLAYER_IMAGE_INDEX_KEY, 0);
//
//                String imageIndexMessage = String.valueOf(playerImage);
//
//                DataOutputStream dataOutputStream = new DataOutputStream(client.getOutputStream());
//                dataOutputStream.writeUTF(imageIndexMessage);
//            }
//            catch(IOException e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }

}
