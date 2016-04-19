// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi;

// All of the extra includes here.
import android.app.ProgressDialog;

import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkConstants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Jason Mottershead on 10/04/2016.
 */

// WiFi client tasks ARE wifi tasks, therefore inherits from it.
public class WiFiClientTasks extends WiFiTasks
{
    // Attributes.
    // Private.
    private Socket serverSocket = new Socket();     // The socket that will be used to connect to the server peer.

    // Methods.
    //////////////////////////////////////////////////
    //                  Constructor                 //
    //==============================================//
    // This will initialise our attributes, and set //
    // up the shared preference files.              //
    //////////////////////////////////////////////////
    public WiFiClientTasks(NetworkActivity networkActivity, String connectedDeviceAddress)
    {
        activity = networkActivity;
        serverAddress = connectedDeviceAddress;
        progressLoading = new ProgressDialog(networkActivity);
        setSharedPreferences();
    }

    //////////////////////////////////////////////////
    //                    Start                     //
    //==============================================//
    // This start function will show progress       //
    // dialog and send the current network state    //
    // over to our handler for processing.          //
    //////////////////////////////////////////////////
    @Override
    public void start()
    {
        super.start();

        // Setting the message of the progress dialog.
        progressLoading.setMessage("Loading...");

        // If the progress dialog is not showing.
        if(!progressLoading.isShowing())
        {
            // Display our loading progress.
            progressLoading.show();
        }

        // Send over our current network state.
        handler.sendEmptyMessage(currentState);
    }

    //////////////////////////////////////////////////
    //                    Run                       //
    //==============================================//
    // This function will set up the main client    //
    // task to run, to begin with we send a ready   //
    // message to the peer.                         //
    //////////////////////////////////////////////////
    @Override
    public void run()
    {
        // Our string message used send over to the server peer.
        String message = "";

        // Our string message to retrieve the response from the server peer.
        String response = "";

        try
        {
            // Switching between the current network state.
            switch (currentState)
            {
                // If we are going to send a ready message.
                case NetworkConstants.STATE_SEND_READY_MESSAGE:
                {
                    // Create a client socket with the host, port number and use the default time out information.
                    serverSocket = new Socket(serverAddress, NetworkConstants.SOCKET_SERVER_PORT);
                    setSocket(serverSocket);

                    // Setting our message to the value of our current state.
                    message = String.valueOf(currentState);

                    // Writing our current network state to the server socket.
                    DataOutputStream dataOutputStream = new DataOutputStream(serverSocket.getOutputStream());
                    dataOutputStream.writeUTF(message);

                    // If we have some data in our input stream.
                    if(serverSocket.getInputStream() != null)
                    {
                        // Read the data from the connected socket.
                        DataInputStream dataInputStream = new DataInputStream(serverSocket.getInputStream());
                        response = dataInputStream.readUTF();

                        // Send the response from the server to our handler.
                        // This should obtain the current server network state.
                        handler.sendEmptyMessage(Integer.parseInt(response));
                    }

                    break;
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // Getters.
    // Get the current peer image.
    public int getPeerImageIndexInt()   { return peerImageIndexInt; }

    // Setters.
    // Setting the peer image index.
    public void setClientPeerImage(int image)
    {
        sendImageThread.setImageIndex(image);
    }
}
