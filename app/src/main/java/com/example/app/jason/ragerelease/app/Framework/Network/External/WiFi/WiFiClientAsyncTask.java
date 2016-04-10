// The package location of this class.
package com.example.app.jason.ragerelease.app.Framework.Network.External.WiFi;

// All of the extra includes here.
import android.os.AsyncTask;

import com.example.app.jason.ragerelease.app.Framework.Debug.DebugInformation;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkActivity;
import com.example.app.jason.ragerelease.app.Framework.Network.NetworkConstants;

import java.io.IOException;
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
    private String serverAddress;
    private NetworkActivity activity = null;
    private Socket socket = new Socket();

    // Methods.
    public WiFiClientAsyncTask(NetworkActivity networkActivity, String connectedDeviceAddress)
    {
        activity = networkActivity;
        serverAddress = connectedDeviceAddress;
    }

    @Override
    protected String doInBackground(String... params)
    {
        try
        {
            // Create a client socket with the host, port number and timeout information.
            socket.bind(null);
            socket.connect(new InetSocketAddress(serverAddress, NetworkConstants.SOCKET_SERVER_PORT), 500);

            // If we arrive here, we have connected to the server.
            return "Done";
        }
        catch (IOException e)
        {
            return "Not done";
        }
        finally
        {
            if(socket != null)
            {
                if(socket.isConnected())
                {
                    try
                    {
                        socket.close();
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onPostExecute(String result)
    {
        DebugInformation.displayShortToastMessage(activity, "Client: " + result);
    }
}
