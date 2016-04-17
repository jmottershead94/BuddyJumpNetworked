// The package location of this class.
package com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi;

// All of the extra includes here.
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;

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
//public class WiFiClientTasks extends AsyncTask<String, Void, String>
public class WiFiClientTasks extends WiFiTasks
{
    // Attributes.
    private Socket serverSocket = new Socket();

    // Methods.
    public WiFiClientTasks(NetworkActivity networkActivity, String connectedDeviceAddress)
    {
        activity = networkActivity;
        serverAddress = connectedDeviceAddress;
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
        String response = "";

        try
        {
            switch (currentState)
            {
                // If we need to send the ready message.
                case NetworkConstants.STATE_SEND_READY_MESSAGE:
                {
                    // Create a client socket with the host, port number and timeout information.
                    serverSocket = new Socket(serverAddress, NetworkConstants.SOCKET_SERVER_PORT);
                    setSocket(serverSocket);

                    message = String.valueOf(currentState);

                    DataOutputStream dataOutputStream = new DataOutputStream(serverSocket.getOutputStream());
                    dataOutputStream.writeUTF(message);

                    DataInputStream dataInputStream = new DataInputStream(serverSocket.getInputStream());
                    response = dataInputStream.readUTF();
                    handler.sendEmptyMessage(Integer.parseInt(response));

                    break;
                }
                case NetworkConstants.STATE_SEND_IMAGE_MESSAGE:
                {
                    sendImageThread.start();
                    handler.sendEmptyMessage(currentState);

                    currentState = NetworkConstants.STATE_SEND_GAME_MESSAGES;
                    message = String.valueOf(currentState);

                    break;
                }
                case NetworkConstants.STATE_SEND_GAME_MESSAGES:
                {
                    handler.sendEmptyMessage(currentState);
                    message = String.valueOf(currentState);

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
    // Get the current client state.
    public int getClientState()         { return currentState; }

    // Get the current peer image.
    public int getPeerImageIndexInt()   { return peerImageIndexInt; }

    // Setters.
    public void setClientState(int value)
    {
        currentState = value;
        handler.sendEmptyMessage(value);
    }

    public void setClientPeerImage(int image)
    {
        sendImageThread.setImageIndex(image);
    }
}
