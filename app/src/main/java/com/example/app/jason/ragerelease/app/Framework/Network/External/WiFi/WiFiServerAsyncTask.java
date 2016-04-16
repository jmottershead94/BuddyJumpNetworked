// The package location of this class.
package com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi;

// All of the extra includes here.
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

import com.example.app.jason.ragerelease.app.Framework.Debug.DebugInformation;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkConstants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Jason Mottershead on 10/04/2016.
 */

// WiFi server async task IS AN async task, therefore inherits from it.
//public class WiFiServerAsyncTask extends AsyncTask<String, Void, String>
public class WiFiServerAsyncTask extends Thread
{
    // Attributes.
    private ProgressDialog progressLoading = null;
    private NetworkActivity activity = null;
    private Socket client = null;
    public SendImageMessage sendImageThread = null;
    private int peerImageIndexInt = 0;
    private static String PREFS_NAME = "MyPrefsFile";                           // Where the options will be saved to, whether they are true or false.
    private static String PLAYER_IMAGE_INDEX_KEY = "mplayerImage";              // The key used to access the player image index.
    private static int serverState = NetworkConstants.STATE_SEND_READY_MESSAGE;    // The current wifi server state.

    // Methods.
    public WiFiServerAsyncTask(NetworkActivity networkActivity)
    {
        activity = networkActivity;
        progressLoading = new ProgressDialog(networkActivity);
        sendImageThread = new SendImageMessage();

        // Get access to the singlePlayerGame options.
        // Accessing the player image index.
    }

    @Override
    public void start()
    {
        super.start();

        //DebugInformation.displayShortToastMessage(activity, "");

        progressLoading.setMessage("Loading...");
        progressLoading.show();

        handler.sendEmptyMessage(serverState);
    }

    @Override
    public void run()
    {
        String message = "";

        //while(true)
        //{
            try
            {
                switch (serverState)
                {
                    // If we need to send the ready message.
                    case NetworkConstants.STATE_SEND_READY_MESSAGE:
                    {
                        // Create a server socket and wait for client connections.
                        ServerSocket serverSocket = new ServerSocket(NetworkConstants.SOCKET_SERVER_PORT);
                        client = serverSocket.accept();

                        DataInputStream dataInputStream = new DataInputStream(client.getInputStream());
                        message = dataInputStream.readUTF();

                        // Make a response.
                        new SendReadyMessage().run();
                        handler.sendEmptyMessage(Integer.parseInt(message));

                        // Send the current image index.
                        //new SendImageMessage().run();

                        //serverState = NetworkConstants.STATE_SEND_IMAGE_MESSAGE;
                        //message = String.valueOf(serverState);

                        // If we are here, we have accepted a connection from the client.
                        break;
                        //return (message);
                    }
                    case NetworkConstants.STATE_SEND_IMAGE_MESSAGE:
                    {
                        break;
                    }
                    case NetworkConstants.STATE_SEND_GAME_MESSAGES:
                    {
                        break;
                    }
                }

                //return message;
            }
            catch (IOException e)
            {
                //return "Not done";
                message = "Not done";
                //break;
            }
        //}
    }

//    @Override
//    protected void onPostExecute(String result)
//    {
//        super.onPostExecute(result);
//
//        // If we are ready...
//        if(Boolean.valueOf(result))
//        {
//            // Start a new thread for sending over an image index?
//            DebugInformation.displayShortToastMessage(activity, "Peer successfully connected");
//            progressLoading.dismiss();
//
//            //new SendImageMessage().run();
//            //DebugInformation.displayShortToastMessage(activity, "Image index: " + peerImageIndexInt);
//        }
//    }

    // This class will handle sending the ready message over to the other peer.
    private class SendReadyMessage extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                String response = String.valueOf(serverState);
                DataOutputStream dataOutputStream = new DataOutputStream(client.getOutputStream());
                dataOutputStream.writeUTF(response);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    // This class will handle sending over the image index to the other peer.
    public class SendImageMessage extends Thread
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
                //SharedPreferences gameSettings = activity.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
                final int playerImage = playerImageIndex;
                final String imageIndexMessage = String.valueOf(playerImage);

//                activity.runOnUiThread(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        DebugInformation.displayShortToastMessage(activity, "Int image index: " + playerImage);
//                        DebugInformation.displayShortToastMessage(activity, "String image index: " + imageIndexMessage);
//                    }
//                });

                DataOutputStream dataOutputStream = new DataOutputStream(client.getOutputStream());
                dataOutputStream.writeUTF(imageIndexMessage);

                if(client.getInputStream() != null)
                {
                    DataInputStream dataInputStream = new DataInputStream(client.getInputStream());
                    String peerImageIndex = dataInputStream.readUTF();
                    peerImageIndexInt = Integer.parseInt(peerImageIndex);

                    activity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            DebugInformation.displayShortToastMessage(activity, "Client image index: " + peerImageIndexInt);
                        }
                    });
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
//            catch(NumberFormatException nfe)
//            {
//                nfe.printStackTrace();
//            }
        }
    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message message)
        {
            switch (message.what)
            {
                case NetworkConstants.STATE_SEND_READY_MESSAGE:
                {
                    DebugInformation.displayShortToastMessage(activity, "CLIENT READY MESSAGE");
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
                    serverState = NetworkConstants.STATE_SEND_GAME_MESSAGES;
                    String clientMessage = String.valueOf(serverState);

                    // We have received our server image here.
                    DebugInformation.displayShortToastMessage(activity, "Image received from client");
                    progressLoading.dismiss();
                    break;
                }
                case NetworkConstants.STATE_SEND_GAME_MESSAGES:
                {
                    DebugInformation.displayShortToastMessage(activity, "CLIENT GAME MESSAGES");
                    progressLoading.dismiss();
                    break;
                }
            }
        }
    };

    // Getters.
    public int getServerState() { return serverState; }

    // Setters.
    public void setServerState(int value)
    {
        serverState = value;
        handler.sendEmptyMessage(value);
    }
}
