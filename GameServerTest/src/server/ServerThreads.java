package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ClientServer.ClientServerMessage;
import database.DatabaseManager;
import database.dbResultSet;


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
					validateLogin(csm.getMessage());
					break;
				case ClientServerMessage.MESSAGE:
					displayMessage("Client message " + id  + " : " + message);
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
	
	private boolean writeMessage(String msg) {
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

	private void validateLogin(String UNPW){
		String[] tokens = csm.getMessage().split("[/]+");
		if(tokens.length != 2){
			writeMessage("Invalid login");
			return;
		}
		System.out.println(tokens[0] +" " + tokens[1]);

		String query = "Select * from users where userName = \"" + tokens[0] + "\"";
		System.out.println(query);
		DatabaseManager dbQuery = new DatabaseManager(query);
		dbResultSet queryResult = dbQuery.query(query);
		System.out.println("Num rows: " + queryResult.getRowCount());
		
		if(queryResult.getRowCount() == 0){
			displayMessage(tokens[0] + " is NOT a valid userName");
			writeMessage("Invalid Username");
			return;
		}else{
			displayMessage(tokens[0] + " is a valid userName!");
			query = "Select * from users where password = \"" + tokens[1] + "\"";
			queryResult = dbQuery.query(query);
			if(queryResult.getRowCount() == 0){
				writeMessage("Invalid password");
				return;
			}else{
				userName = tokens[0];
				writeMessage("You have been successfully logged in, " + userName);
			}
			return;
		}
	}

}
