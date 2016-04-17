// The package location of this class.
package com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

import com.example.app.jason.ragerelease.app.Framework.Debug.DebugInformation;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkConstants;

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
    protected ProgressDialog progressLoading = null;
    protected String serverAddress;
    protected NetworkActivity activity = null;
    protected SendReadyMessage sendReadyMessage = new SendReadyMessage();
    protected SendImageMessage sendImageThread = new SendImageMessage();
    protected SendGameMessages sendGameMessages = new SendGameMessages();
    protected int peerImageIndexInt = 0;
    protected static String PREFS_NAME = "MyPrefsFile";                      // Where the options will be saved to, whether they are true or false.
    protected static String PLAYER_IMAGE_INDEX_KEY = "mplayerImage";         // The key used to access the player image index.
    protected int currentState = NetworkConstants.STATE_SEND_READY_MESSAGE;
    protected Socket socket = null;
    protected boolean tapped = false;
    protected boolean peerTapped = false;
    protected boolean isRunning = false;

    // Methods.
//    protected void newReadyThread()
//    {
//        sendReadyMessage = new SendReadyMessage();
//    }
//
//    protected void newImageThread()
//    {
//        sendImageThread = new SendImageMessage();
//    }
//
//    protected void newGameMessageThread()
//    {
//
//    }

    protected void setSocket(Socket peerSocket)
    {
        socket = peerSocket;
    }

    public void setTapped(boolean tap)
    {
        tapped = tap;
    }

    public void setGameIsRunning(boolean running)
    {
        isRunning = running;
    }

    // This class will handle sending the ready message over to the other peer.
    protected class SendReadyMessage extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                String response = String.valueOf(currentState);
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
        private int playerImageIndex = 0;

        public void setImageIndex(int imageIndex)
        {
            playerImageIndex = imageIndex;
        }

        @Override
        public void run()
        {
            try
            {
                // Get access to the singlePlayerGame options.
                // Accessing the player image index.
                final int playerImage = playerImageIndex;
                final String imageIndexMessage = String.valueOf(playerImage);

                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(imageIndexMessage);

                if(socket.getInputStream() != null)
                {
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    String peerImageIndex = dataInputStream.readUTF();
                    peerImageIndexInt = Integer.parseInt(peerImageIndex);

                    activity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            DebugInformation.displayShortToastMessage(activity, "Peer Image index: " + peerImageIndexInt);
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
        @Override
        public void run()
        {
            try
            {
                // while(either player has not died)
                // {

                while(isRunning)
                {
                    // Accessing the player tapped status.
                    final boolean playerTapped = tapped;
                    final String tappedMessage = String.valueOf(playerTapped);

                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF(tappedMessage);

                    if (socket.getInputStream() != null)
                    {
                        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                        String peerTappedMessage = dataInputStream.readUTF();
                        peerTapped = Boolean.valueOf(peerTappedMessage);
                    }
                }

                // }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    protected Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message message)
        {
            switch (message.what)
            {
                case NetworkConstants.STATE_SEND_READY_MESSAGE:
                {
                    DebugInformation.displayShortToastMessage(activity, "Peer connected successfully");
                    progressLoading.dismiss();
                    break;
                }
                case NetworkConstants.STATE_SEND_IMAGE_MESSAGE:
                {
                    // We are about to send over the image index.
                    progressLoading.setMessage("Sending image over...");
                    progressLoading.show();
                    //DebugInformation.displayShortToastMessage(activity, "Sending image.");

                    // Start the thread to send the image index.
                    sendImageThread.start();

                    // Switch the state for the client to the next state.
                    currentState = NetworkConstants.STATE_SEND_GAME_MESSAGES;
                    String clientMessage = String.valueOf(currentState);

                    // We have received our server image here.
                    //DebugInformation.displayShortToastMessage(activity, "Image received from client");
                    progressLoading.dismiss();
                    break;
                }
                case NetworkConstants.STATE_SEND_GAME_MESSAGES:
                {
                    DebugInformation.displayShortToastMessage(activity, "Game messages");
                    progressLoading.dismiss();

                    // Start sending game messages.
                    sendGameMessages.start();

                    //DebugInformation.displayShortToastMessage(activity, "Shouldn't get here?" + areGameMessagesRunning());

                    break;
                }
            }
        }
    };

    // Getters.
    // Return whether or not the peer has tapped on their character.
    public boolean hasPeerTapped() { return peerTapped; }

    public boolean areGameMessagesRunning() { return isRunning; }

    // Setters.
    public void setState(int newState)
    {
        currentState = newState;
        handler.sendEmptyMessage(newState);
    }
}
