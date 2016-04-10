// The package location of this class.
package com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi;

// All of the extra includes here.
import android.os.AsyncTask;

import com.example.app.jason.ragerelease.app.Framework.Debug.DebugInformation;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkConstants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Jason Mottershead on 10/04/2016.
 */

// WiFi server async task IS AN async task, therefore inherits from it.
public class WiFiServerAsyncTask extends AsyncTask<String, Void, String>
{
    // Attributes.
    private NetworkActivity activity = null;

    // Methods.
    public WiFiServerAsyncTask(NetworkActivity networkActivity)
    {
        activity = networkActivity;
    }

    @Override
    protected String doInBackground(String... params)
    {
        try
        {
            // Create a server socket and wait for client connections.
            ServerSocket serverSocket = new ServerSocket(NetworkConstants.SOCKET_SERVER_PORT);
            Socket client = serverSocket.accept();

            // If we are here, we have accepted a connection from the client.
            return "Done";
        }
        catch (IOException e)
        {
            return "Not done";
        }
    }

    @Override
    protected void onPostExecute(String result)
    {
        DebugInformation.displayShortToastMessage(activity, "Server: " + result);
    }
}
