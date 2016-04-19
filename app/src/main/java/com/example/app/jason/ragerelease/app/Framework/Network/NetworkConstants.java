// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework.Network;

/**
 * Created by Jason Mottershead on 05/04/2016.
 */

// This class will be responsible for holding all of the network constant values within the app.
public class NetworkConstants
{
    // Attributes.
    // Public.
    // Ints.
    public static final int SOCKET_SERVER_PORT = 8888;                                  // The server port we will use to connect the peers together and to set up the server/client sockets.
    public static final int HOST_ID = 11;                                               // The host ID number, used to determine if the player is hosting a game.
    public static final int JOIN_ID = HOST_ID + 1;                                      // The join ID number, used to determine if the player is joining a game.
    public static final int STATE_SEND_READY_MESSAGE = JOIN_ID + 1;                     // The state of the connection, we should send a ready message to the peer.
    public static final int STATE_SEND_IMAGE_MESSAGE = STATE_SEND_READY_MESSAGE + 1;    // The state of the connection, we should send an image message to the peer.
    public static final int STATE_SEND_GAME_MESSAGES = STATE_SEND_IMAGE_MESSAGE + 1;    // The state of the connection, we should send our game messages to the peer.

    // Strings.
    public static final String EXTRA_DEVICE_ADDRESS = "deviceAddress";                  // The key for storing our connected device MAC address.
    public static final String EXTRA_PLAYER_MATCH_STATUS = "playerMatchStatus";         // The key for storing our player match status - if they are hosting or joining.
    public static final String EXTRA_PEER_INDEX = "peerImageIndex";                     // The key for storing our peer image.
}
