package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import ClientServer.ClientServerMessage;


public class ServerThreads implements Runnable{
 
	protected Socket socket;
	protected ServerGUI serverGUI;
	boolean disconnected = false;
	ObjectInputStream sInput;
	ObjectOutputStream sOutput;
	BufferedReader buffRead;
	int id;
	String userName;
	ClientServerMessage csm;
	
	
	
	public ServerThreads(Socket clientSocket, ServerGUI serverGUI){
		this.socket = clientSocket;
		this.serverGUI = serverGUI;
		id = ++serverGUI.server.uniqueId;
	}
	
	public void run(){

			displayMessage("Client: " + id + " " + socket.getInetAddress().toString() + " connected.\n");
		   displayMessage( "Waiting for connection..." );
		   //System.out.println("Setting up I/O Streams");
		   //while(!disconnected){
		   try{
			
			   sOutput = new ObjectOutputStream(socket.getOutputStream());
			   sInput  = new ObjectInputStream(socket.getInputStream());
					   
		   }catch(IOException e){
			   System.out.println("IO Exception during socket initialization");
			   e.printStackTrace();;
		   }
		   boolean isConnected = true;
		   while(isConnected){
			   
			   try{
				   csm = (ClientServerMessage)sInput.readObject();
			   }catch (IOException e) {
					//display(userName + " Exception reading Streams: " + e);
				   System.out.println("Exception in reading client stream" + e);
					break;				
				}
				catch(ClassNotFoundException e2) {
					break;
				}
			   //The message from client
			   
			   String message = csm.getMessage();
			   
				switch(csm.getType()) {
				
				case ClientServerMessage.LOGIN:
					displayMessage("Client trying to login in with: " + csm.getMessage());

				case ClientServerMessage.MESSAGE:
					displayMessage("Client" + id  + " : " + message);
					break;
				case ClientServerMessage.LOGOUT:
					displayMessage("Client" + id  + " logged out.");
					isConnected = false;
					break;
				}//End case statement
		   }
		   //Thread is done...
		   close();
		   
	}// End run
	
	private void displayMessage(String message){
		   serverGUI.server.displayMessage(message);
			
	}
	private void close() {
		// try to close the connection
		try {
			if(sOutput != null) sOutput.close();
		}
		catch(Exception e) {}
		try {
			if(sInput != null) sInput.close();
		}
		catch(Exception e) {};
		try {
			if(socket != null) socket.close();
		}
		catch (Exception e) {}
	}
	
	private boolean writeMsg(String msg) {
		// if Client is still connected send the message to it
		if(!socket.isConnected()) {
			close();
			return false;
		}
		// write the message to the stream
		try {
			sOutput.writeObject(msg);
		}
		// if an error occurs, do not abort just inform the user
		catch(IOException e) {
			displayMessage("Error sending message to " + id);
			displayMessage(e.toString());
		}
		return true;
	}


}
