// The package location of this class.
package com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi;

// All of the extra includes here.
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.app.jason.ragerelease.app.Framework.Debug.DebugInformation;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkConstants;
import com.example.app.jason.ragerelease.app.Framework.SavedFileConstants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Jason Mottershead on 10/04/2016.
 */

// WiFi server async task IS AN async task, therefore inherits from it.
public class WiFiServerAsyncTask extends AsyncTask<String, Void, String>
{
    // Attributes.
    private ProgressDialog progressLoading = null;
    private NetworkActivity activity = null;
    private Socket client = null;
    public SendImageMessage sendImageThread = null;
    private int peerImageIndexInt = 0;
    private static String PREFS_NAME = "MyPrefsFile";                      // Where the options will be saved to, whether they are true or false.
    private static String PLAYER_IMAGE_INDEX_KEY = "mplayerImage";         // The key used to access the player image index.

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
    protected void onPreExecute()
    {
        super.onPreExecute();

        //DebugInformation.displayShortToastMessage(activity, "");

        progressLoading.setMessage("Loading...");
        progressLoading.show();
    }

    @Override
    protected String doInBackground(String... params)
    {
        try
        {
            // Create a server socket and wait for client connections.
            ServerSocket serverSocket = new ServerSocket(NetworkConstants.SOCKET_SERVER_PORT);
            client = serverSocket.accept();

            DataInputStream dataInputStream = new DataInputStream(client.getInputStream());
            String message = dataInputStream.readUTF();

            // Make a response.
            new SendReadyMessage().run();

            // Send the current image index.
            //new SendImageMessage().run();

            // If we are here, we have accepted a connection from the client.
            return (message);
        }
        catch (IOException e)
        {
            return "Not done";
        }
    }

    @Override
    protected void onPostExecute(String result)
    {
        super.onPostExecute(result);

        // If we are ready...
        if(Boolean.valueOf(result))
        {
            // Start a new thread for sending over an image index?
            DebugInformation.displayShortToastMessage(activity, "Peer successfully connected");
            progressLoading.dismiss();

            //new SendImageMessage().run();
            //DebugInformation.displayShortToastMessage(activity, "Image index: " + peerImageIndexInt);
        }
    }

    // This class will handle sending the ready message over to the other peer.
    private class SendReadyMessage extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                String response = "true";
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

                activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        DebugInformation.displayShortToastMessage(activity, "Int image index: " + playerImage);
                        DebugInformation.displayShortToastMessage(activity, "String image index: " + imageIndexMessage);
                    }
                });

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
                            DebugInformation.displayShortToastMessage(activity, "Image index: " + peerImageIndexInt);
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
}
