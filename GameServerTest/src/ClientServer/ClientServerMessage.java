package ClientServer;

import java.io.Serializable;

/*
 * This class is the message definition for communication between
 * client and server. It's much easier to use an Object instead
 * of bytes and newlines
 */
public class ClientServerMessage implements Serializable{

	// The different types of message sent by the Client
	// WHOISIN to receive the list of the users connected
	// MESSAGE an ordinary message
	// LOGOUT to disconnect from the Server
	public static final int MESSAGE = 1, LOGOUT = 2;
	private int type;
	private String message;
	
	// constructor
	public ClientServerMessage(int type, String message) {
		this.type = type;
		this.message = message;
	}
	
	// getters
	public int getType() {
		return type;
	}
	public String getMessage() {
		return message;
	}
}
