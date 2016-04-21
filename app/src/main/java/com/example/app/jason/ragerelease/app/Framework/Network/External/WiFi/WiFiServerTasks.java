// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi;

// All of the extra includes here.
import android.app.ProgressDialog;

import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkConstants;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Jason Mottershead on 10/04/2016.
 */

// WiFi server tasks ARE wifi tasks, therefore inherits from it.
public class WiFiServerTasks extends WiFiTasks
{
    // Attributes.
    // Private.
    private Socket client = null;       // The socket that will be used to connect to the client peer.

    // Methods.
    //////////////////////////////////////////////////
    //                  Constructor                 //
    //==============================================//
    // This will initialise our attributes, and set //
    // up the shared preference files.              //
    //////////////////////////////////////////////////
    public WiFiServerTasks(NetworkActivity networkActivity)
    {
        // Initialising our attributes.
        activity = networkActivity;
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

        // If the progress loading dialog is not showing.
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
    // This function will set up the main server    //
    // task to run, to begin with we send a ready   //
    // message to the peer.                         //
    //////////////////////////////////////////////////
    @Override
    public void run()
    {
        // Our string message used to handle the response from the client peer.
        String message = "";

        try
        {
            if(currentState == NetworkConstants.STATE_SEND_READY_MESSAGE)
            {
                // Create a server socket and wait for client connections.
                ServerSocket serverSocket = new ServerSocket(NetworkConstants.SOCKET_SERVER_PORT);
                client = serverSocket.accept();
                setSocket(client);

                // If we have some data in our input stream.
                if(client.getInputStream() != null)
                {
                    // Read the data from the connected socket.
                    DataInputStream dataInputStream = new DataInputStream(client.getInputStream());
                    message = dataInputStream.readUTF();

                    // Make a response.
                    new SendReadyMessage().run();

                    // Send the current message over to the handle.
                    // This will send over the peer network state.
                    handler.sendEmptyMessage(Integer.parseInt(message));
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
    public int getPeerImageIndexInt()           { return peerImageIndexInt; }

    // Setters.
    // Setting the peer image index.
    public void setServerPeerImage(int image)   { sendImageThread.setImageIndex(image); }
}
