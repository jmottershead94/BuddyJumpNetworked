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
//public class WiFiServerTasks extends AsyncTask<String, Void, String>
public class WiFiServerTasks extends WiFiTasks
{
    // Attributes.
    private Socket client = null;

    // Methods.
    public WiFiServerTasks(NetworkActivity networkActivity)
    {
        activity = networkActivity;
        progressLoading = new ProgressDialog(networkActivity);
        //newImageThread();
    }

    @Override
    public void start()
    {
        super.start();

        progressLoading.setMessage("Loading...");
        progressLoading.show();

        handler.sendEmptyMessage(currentState);
    }

    @Override
    public void run()
    {
        String message = "";

        try
        {
            switch (currentState)
            {
                // If we need to send the ready message.
                case NetworkConstants.STATE_SEND_READY_MESSAGE:
                {
                    // Create a server socket and wait for client connections.
                    ServerSocket serverSocket = new ServerSocket(NetworkConstants.SOCKET_SERVER_PORT);
                    client = serverSocket.accept();
                    setSocket(client);

                    DataInputStream dataInputStream = new DataInputStream(client.getInputStream());
                    message = dataInputStream.readUTF();

                    // Make a response.
                    new SendReadyMessage().run();
                    handler.sendEmptyMessage(Integer.parseInt(message));

                    // If we are here, we have accepted a connection from the client.
                    break;
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
        }
        catch (IOException e)
        {
            message = "Not done";
        }
    }

    // Getters.
    // Get the current server state.
    public int getServerState()         { return currentState; }

    // Get the current peer image.
    public int getPeerImageIndexInt()   { return peerImageIndexInt; }

//    // Setters.
//    public void setServerState(int value)
//    {
//        currentState = value;
//        handler.sendEmptyMessage(value);
//    }

    public void setServerPeerImage(int image)
    {
        sendImageThread.setImageIndex(image);
    }
}
