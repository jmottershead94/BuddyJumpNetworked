// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi;

// All of the extra includes here.
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.example.app.jason.ragerelease.app.Framework.Debug.DebugInformation;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkConstants;
import com.example.app.jason.ragerelease.app.Framework.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Jason Mottershead on 17/04/2016.
 */

// This class will be responsible for handling the generic tasks for wifi communication.
public class WiFiTasks extends Thread
{
    // Attributes.
    // Protected.
    protected ProgressDialog progressLoading = null;                        // This will be used to display loading progress for the wifi tasks.
    protected String serverAddress = "";                                    // The string value to store the server address.
    protected NetworkActivity activity = null;                              // Our reference to the current network activity.
    protected SendImageMessage sendImageThread = new SendImageMessage();    // This will be used in order to send our image index over to the other thread.
    protected SendStartGameMessage sendStartGameMessage = new SendStartGameMessage();
    protected SendGameMessages sendGameMessages = new SendGameMessages();   // This will be used in order to send game messages over to the other peer.
    protected int peerImageIndexInt = 0;                                    // The current int value for our peer image index.
    protected static String PREFS_NAME = "MyPrefsFile";                     // Where the options will be saved to, whether they are true or false.
    protected String PLAYER_TAPPED_KEY = "mplayerTapped";                   // The string key for accessing whether or not the peer player has tapped the screen.
    protected String PLAYER_READY_KEY = "mplayerReady";
    protected int currentState = NetworkConstants.STATE_SEND_READY_MESSAGE; // Initialising our current network state to send a ready message.
    protected Socket socket = null;                                         // Will be used for wifi communication and reading input stream / writing to output stream.
    protected boolean isReady = false;                                      // Whether or not the player is ready to start the game.
    protected static boolean peerTapped = false;                            // Whether or not the peer has tapped.
    protected boolean isRunning = false;                                    // Whether or not the game message sending thread should be running or not.
    protected Player player = null;                                         // Gaining access to the player.
    protected SharedPreferences gameSettings = null;                        // Gaining access to the shared preferences file.
    protected SharedPreferences.Editor editor = null;                       // Gaining access to the editor of the shared preference file.

    // This private class will handle sending the ready message over to the other peer.
    protected class SendReadyMessage extends Thread
    {
        // Methods.
        //////////////////////////////////////////////////
        //                      Run                     //
        //==============================================//
        // This function will run the ready message     //
        // thread, which will send over the current     //
        // state to the peer.                           //
        //////////////////////////////////////////////////
        @Override
        public void run()
        {
            try
            {
                // Setting up the response to the peer, this will store our current network state.
                String response = String.valueOf(currentState);

                // This will write our response to the output stream.
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(response);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    // This class will handle sending over the image index to the other peer.
    protected class SendImageMessage extends Thread
    {
        // Attributes.
        // Private.
        private int playerImageIndex = 0;           // This is our current peer image index.

        // Methods.
        //////////////////////////////////////////////////
        //                      Run                     //
        //==============================================//
        // This function will run the image message     //
        // thread, which will send over our current     //
        // image index.                                 //
        //////////////////////////////////////////////////
        @Override
        public void run()
        {
            try
            {
                // Accessing the player image index.
                final int playerImage = playerImageIndex;
                final String imageIndexMessage = String.valueOf(playerImage);

                // Writing our index to the output stream of the socket.
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(imageIndexMessage);

                // If we have some data in our input stream.
                if(socket.getInputStream() != null)
                {
                    // Read the data from the connected socket.
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    String peerImageIndex = dataInputStream.readUTF();

                    // Access the peer image index.
                    peerImageIndexInt = Integer.parseInt(peerImageIndex);

                    // Display a message to the user, telling them that an image has been received from the peer.
                    activity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            DebugInformation.displayShortToastMessage(activity, "Received peer image: " + peerImageIndexInt);
                        }
                    });
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        public void cancel()
        {

        }

        // Setters.
        // This will set our current image index.
        public void setImageIndex(int imageIndex) { playerImageIndex = imageIndex; }
    }

    protected class SendStartGameMessage extends Thread
    {
        // Methods.
        //////////////////////////////////////////////////
        //                      Run                     //
        //==============================================//
        // This function will run the game message      //
        // thread, this will be responsible for sending //
        // over messages from each player.              //
        //////////////////////////////////////////////////
        @Override
        public void run()
        {
            try
            {
                // Set is ready to true when we press play game!
                final boolean ready = true;

                // Store that tapped value into a string.
                String readyMessage = String.valueOf(ready);

                // Writing the tapped message to the output stream of the socket.
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(readyMessage);

                while(!isReady)
                {
//                    try
//                    {
                        // If we have some data in our input stream.
                        if (socket.getInputStream() != null)
                        {
                            // Reading data from the input stream of the socket.
                            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                            String peerReadyMessage = dataInputStream.readUTF();

                            // Parsing our string value into a boolean value to be used with our game activity.
                            isReady = Boolean.parseBoolean(peerReadyMessage);

                            // Saving the boolean value within our shared preference file in order to use.
                            editor.putBoolean(PLAYER_READY_KEY, isReady);
                            editor.apply();

                            if (isReady)
                            {
                                break;
                            }
                        }
//                    }
//                    catch(IOException e)
//                    {
//                        e.printStackTrace();
//                    }
                }

                if(isReady)
                {
                    // Display a message to the user, telling them that an image has been received from the peer.
                    activity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            DebugInformation.displayShortToastMessage(activity, "Peer is ready!");
                        }
                    });
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    // This class will allow us to send game messages within the game loop.
    protected class SendGameMessages extends Thread
    {
        // Methods.
        //////////////////////////////////////////////////
        //                      Run                     //
        //==============================================//
        // This function will run the game message      //
        // thread, this will be responsible for sending //
        // over messages from each player.              //
        //////////////////////////////////////////////////
        @Override
        public void run()
        {
            try
            {
                // While the game is running.
                while(isRunning)
                {
                    // If we have an instance of the player.
                    if(player != null)
                    {
                        // Access the tapped status from the player.
                        boolean playerTapped = player.tap;

                        // Store that tapped value into a string.
                        String tappedMessage = String.valueOf(playerTapped);

                        // Writing the tapped message to the output stream of the socket.
                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                        dataOutputStream.writeUTF(tappedMessage);

                        // If we have some data in our input stream.
                        if (socket.getInputStream() != null)
                        {
                            // Reading data from the input stream of the socket.
                            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                            String peerTappedMessageTemp = dataInputStream.readUTF();

                            // Parsing our string value into a boolean value to be used with our game activity.
                            peerTapped = Boolean.parseBoolean(peerTappedMessageTemp);

                            // Saving the boolean value within our shared preference file in order to use.
                            editor.putBoolean(PLAYER_TAPPED_KEY, peerTapped);
                            editor.apply();
                        }
                    }
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    // This is our handler which will have our network states.
    protected Handler handler = new Handler()
    {
        // Methods.
        //////////////////////////////////////////////////
        //               Handle Message                 //
        //==============================================//
        // This function will handle the messages that  //
        // are sent to the handler.                     //
        //////////////////////////////////////////////////
        @Override
        public void handleMessage(Message message)
        {
            // Switching between the message int value.
            switch (message.what)
            {
                // If we are going to send a ready message.
                case NetworkConstants.STATE_SEND_READY_MESSAGE:
                {
                    // Setting the message of the progress dialog.
                    progressLoading.setMessage("Connecting...");

                    // If the progress loading dialog is not showing.
                    if(!progressLoading.isShowing())
                    {
                        // Display our loading progress.
                        progressLoading.show();
                    }

                    // Displaying a message to the user, telling them that they have successfully connected to the peer.
                    DebugInformation.displayShortToastMessage(activity, "Peer connected successfully");

                    // If our progress dialog is showing.
                    if(progressLoading.isShowing())
                    {
                        // Remove the progress loading dialog.
                        progressLoading.dismiss();
                    }

                    break;
                }
                // If we are going to send an image message to the peer.
                case NetworkConstants.STATE_SEND_IMAGE_MESSAGE:
                {
                    // We are about to send over the image index.
                    progressLoading.setMessage("Sending image over...");

                    // If the progress loading dialog is not showing.
                    if(!progressLoading.isShowing())
                    {
                        // Display our loading progress.
                        progressLoading.show();
                    }

                    if(!sendImageThread.isAlive())
                    {
                        // Start the thread to send the image index.
                        sendImageThread.start();
                    }

                    // Switch the state for the client to the next state.
                    currentState = NetworkConstants.STATE_TELL_PEER_READY;

                    // If our progress dialog is showing.
                    if(progressLoading.isShowing())
                    {
                        // Remove the progress loading dialog.
                        progressLoading.dismiss();
                    }

                    break;
                }
                case NetworkConstants.STATE_TELL_PEER_READY:
                {
                    // We are about to send over the image index.
                    progressLoading.setMessage("Sending image over...");

                    // If the progress loading dialog is not showing.
                    if(!progressLoading.isShowing())
                    {
                        // Display our loading progress.
                        progressLoading.show();
                    }

                    if(!sendStartGameMessage.isAlive())
                    {
                        sendStartGameMessage.start();
                    }

                    currentState = NetworkConstants.STATE_SEND_GAME_MESSAGES;

                    // If our progress dialog is showing.
                    if(progressLoading.isShowing())
                    {
                        // Remove the progress loading dialog.
                        progressLoading.dismiss();
                    }

                    break;
                }
                // If we are going to send game messages to the peer.
                case NetworkConstants.STATE_SEND_GAME_MESSAGES:
                {
                    // If our progress dialog is showing.
                    if(progressLoading.isShowing())
                    {
                        // Remove the progress loading dialog.
                        progressLoading.dismiss();
                    }

                    // If our send game messages thread is not yet alive.
                    if(!sendGameMessages.isAlive())
                    {
                        // Start sending game messages.
                        sendGameMessages.start();
                    }

                    break;
                }
            }
        }
    };

//    public void stopAllThreads()
//    {
//        if(sendImageThread.isAlive())
//        {
//            sendImageThread.
//        }
//    }

    // Getters.
    // This will tell us whether or not the peer is ready.
    public boolean isPlayerReady() { return isReady; }

    public int getCurrentState() {return currentState;}

    // Setters.
    // This will set our current network state and send that state over to our handler for processing.
    public void setState(final int newState)
    {
        // Setting the new state.
        currentState = newState;

        // Sending that state over to the handler.
        handler.sendEmptyMessage(newState);
    }

    // This will set our current shared preference file and editor.
    protected void setSharedPreferences()
    {
        // Accessing the shared preferences file.
        gameSettings = activity.getSharedPreferences(PREFS_NAME, activity.MODE_PRIVATE);

        // Setting up the edit for the shared preference file.
        editor = gameSettings.edit();
    }

    // This will set our current socket instance.
    protected void setSocket(final Socket peerSocket)     { socket = peerSocket; }

    // This will set whether or not the game message loop should be running.
    public void setGameIsRunning(final boolean running)   { isRunning = running; }

    // This will set up our instance of the player.
    public void setPlayer(final Player gamePlayer)        { player = gamePlayer; }
}
