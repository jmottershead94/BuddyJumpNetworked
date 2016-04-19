// The package location of this class.
package com.example.app.jason.ragerelease.app.Framework.Network;

/**
 * Created by Jason Mottershead on 05/04/2016.
 */

// This class will be responsible for holding all of the network constant values within the app.
public class NetworkConstants
{
    // Attributes.
    // Ints.
    public static final int SOCKET_SERVER_PORT = 8888;
    public static final int HOST_ID = 11;
    public static final int JOIN_ID = HOST_ID + 1;
    public static final int STATE_SEND_READY_MESSAGE = JOIN_ID + 1;
    public static final int STATE_SEND_IMAGE_MESSAGE = STATE_SEND_READY_MESSAGE + 1;
    public static final int STATE_SEND_GAME_MESSAGES = STATE_SEND_IMAGE_MESSAGE + 1;

    // Strings.
    public static final String EXTRA_DEVICE_ADDRESS = "deviceAddress";
    public static final String EXTRA_PLAYER_MATCH_STATUS = "playerMatchStatus";
    public static final String EXTRA_PEER_INDEX = "peerImageIndex";

}
