// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework.Network;

/**
 * Created by Jason Mottershead on 22/03/2016.
 */

// This class will handle all of the connection states.
public class ConnectionState
{
    public int currentState;
    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;
}
