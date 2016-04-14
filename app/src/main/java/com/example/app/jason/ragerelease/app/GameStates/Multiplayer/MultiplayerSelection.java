// The package location of this class.
package com.example.app.jason.ragerelease.app.GameStates.Multiplayer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.Debug.DebugInformation;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkConstants;
import com.example.app.jason.ragerelease.app.GameStates.MainMenu;
import com.example.app.jason.ragerelease.app.GameStates.PlayerSelection;
import com.example.app.jason.ragerelease.app.GameStates.SinglePlayer.SinglePlayerGame;

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
    private static final String PREFS_NAME = "MyPrefsFile";                     // Where the options will be saved to, whether they are true or false.

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
        companionSelectionButton.setVisibility(View.GONE);
        playGameButton.setOnClickListener(this);

        DebugInformation.displayShortToastMessage(this, "Image index: " + playerImage);
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
            // gameActivity = new Intent(this, SinglePlayerGame.class);

            // Send the current player image index to the other player.
            // Start another async task here?3
            if(playerMatchStatus == NetworkConstants.HOST_ID)
            {
                //(connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getServerAsyncTask().sendImageThread).start();
                (connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getServerAsyncTask().sendImageThread).setImageIndex(playerImage);
                connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getServerAsyncTask().setServerState(NetworkConstants.STATE_SEND_IMAGE_MESSAGE);
                //connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getServerAsyncTask().execute();
                //connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getServerAsyncTask().start();
            }
            else if(playerMatchStatus ==  NetworkConstants.JOIN_ID)
            {
                //(connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getClientAsyncTask().sendImageThread).start();
                (connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getClientAsyncTask().sendImageThread).setImageIndex(playerImage);
                connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getClientAsyncTask().setClientState(NetworkConstants.STATE_SEND_IMAGE_MESSAGE);
                //connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getClientAsyncTask().execute();
                //connectionApplication.getConnectionManagement().getWifiHandler().getWifiP2PBroadcastReceiver().getClientAsyncTask().start();
            }
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
