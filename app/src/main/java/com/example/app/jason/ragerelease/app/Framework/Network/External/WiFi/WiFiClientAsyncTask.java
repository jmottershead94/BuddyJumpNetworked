// The package location of this class.
package com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi;

// All of the extra includes here.
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Debug;

import com.example.app.jason.ragerelease.app.Framework.Debug.DebugInformation;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkConstants;
import com.example.app.jason.ragerelease.app.Framework.SavedFileConstants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Jason Mottershead on 10/04/2016.
 */

// WiFi client async task IS AN async task, therefore inherits from it.
public class WiFiClientAsyncTask extends AsyncTask<String, Void, String>
{
    // Attributes.
    private ProgressDialog progressLoading = null;
    private String serverAddress;
    private NetworkActivity activity = null;
    private Socket socket = new Socket();
    public SendImageMessage sendImageThread = null;
    private int peerImageIndexInt = 0;
    private static String PREFS_NAME = "MyPrefsFile";                      // Where the options will be saved to, whether they are true or false.
    private static String PLAYER_IMAGE_INDEX_KEY = "mplayerImage";         // The key used to access the player image index.

    // Methods.
    public WiFiClientAsyncTask(NetworkActivity networkActivity, String connectedDeviceAddress)
    {
        activity = networkActivity;
        serverAddress = connectedDeviceAddress;
        progressLoading = new ProgressDialog(networkActivity);
        sendImageThread = new SendImageMessage();
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        progressLoading.setMessage("Loading...");
        progressLoading.show();
    }

    @Override
    protected String doInBackground(String... params)
    {
        try
        {
            // Create a client socket with the host, port number and timeout information.
            socket = new Socket(serverAddress, NetworkConstants.SOCKET_SERVER_PORT);

            String message = "true";

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(message);

            String response = "";
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            response = dataInputStream.readUTF();

            //new SendImageMessage().run();

            // If we arrive here, we have connected to the server.
            return (response);
        }
        catch (IOException e)
        {
            return "Not done";
        }
//        finally
//        {
//            if(socket != null)
//            {
//                if(socket.isConnected())
//                {
//                    try
//                    {
//                        socket.close();
//                    }
//                    catch(IOException e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
    }

    @Override
    protected void onPostExecute(String result)
    {
        // If the peer is ready.
        if(Boolean.valueOf(result))
        {
            // Send over image index message.
            DebugInformation.displayShortToastMessage(activity, "Peer successfully connected");
            progressLoading.dismiss();

            //new SendImageMessage().run();
            //DebugInformation.displayShortToastMessage(activity, "Image index: " + peerImageIndexInt);
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
                            DebugInformation.displayShortToastMessage(activity, "Image index: " + peerImageIndexInt);
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
}
