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
            /**
             * Create a server socket and wait for client connections. This
             * call blocks until a connection is accepted from a client
             */
            ServerSocket serverSocket = new ServerSocket(NetworkConstants.SOCKET_SERVER_PORT);
            Socket client = serverSocket.accept();

            /**
             * If this code is reached, a client has connected and transferred data
             * Save the input stream from the client as a JPEG file
             */
            DebugInformation.displayShortToastMessage(activity, "Client has connected");
            return null;
        }
        catch (IOException e)
        {
            //Log.e(WiFiDirectActivity.TAG, e.getMessage());
            DebugInformation.displayShortToastMessage(activity, "Client has not connected");
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result)
    {
        DebugInformation.displayShortToastMessage(activity, "Executing");
    }
}
