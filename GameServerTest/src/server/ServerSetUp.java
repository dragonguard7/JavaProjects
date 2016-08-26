package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

import javax.swing.SwingUtilities;

public class ServerSetUp implements Runnable
{

   private ServerGUI serverGUI;
   protected static int uniqueId;
   protected ServerSocket server; // server socket
   protected boolean serverUp, running = false;
   protected int serverPort;
   protected Thread runningThread = null;

   public ServerSetUp(){
	   this.serverPort = 36;
   }
   
   public ServerSetUp(int serverPort){
	   this.serverPort = serverPort;
   }
   
   
   public void serverInit(){
	   serverUp = true;
	   serverGUI = new ServerGUI(this);		      

	      
	      
	   while(true){
		  if(serverUp && !running){
			  System.out.println("Starting server");
			  new Thread(this).start();

			  running = true;
		  }

	      if(!serverUp && running){
	    	  this.stop();
	      }
	   }   
	      
   }

   public void run(){
	   synchronized(this){
		   this.runningThread = Thread.currentThread();
	   }
	  
	   try // set up server to receive connections; process connections
	      {
		   
	         server = new ServerSocket( serverPort, 100, InetAddress.getLocalHost()); // create ServerSocket
	         URL ip = new URL("http://checkip.amazonaws.com");
	         BufferedReader in = new BufferedReader(new InputStreamReader(ip.openStream()));
	         String ipS = in.readLine();
	         System.out.println("IP from amazonaws: " + ipS);
	         displayMessage("Starting server at " + InetAddress.getLocalHost());
	      } catch (IOException e){
	          throw new RuntimeException("Cannot open port " + serverPort,e);
	      } // end catch
	   	 displayMessage( "Waiting for connection..." );
	   while(isServerUp()){
		   
		   Socket clientSocket = null;		   
		   try{
			   clientSocket = this.server.accept();
		   }catch(IOException e){
			   if(!isServerUp()){
				   serverGUI.displayArea.append("Server was stopped\n");
				   return;
			   }   
			   throw new RuntimeException("Error acception client connection",e);
		   }//End catch
		   
	//End server's job. Create a new Thread to handle client connection.
		   new Thread(new ServerThreads(clientSocket,serverGUI)).start();

	   }
	   serverGUI.displayArea.append("Server Stopped\n");
	   System.out.println("Server Stopped.");
   }
   
   private synchronized boolean isServerUp(){
	   return this.serverUp;
   }
   public synchronized void stop(){
	   this.running = false;
	   try{
		   this.server.close();
	   }catch(IOException e){
		   throw new RuntimeException("Error closing server", e);
	   }
   }

//************************************************************************************************* 
   // manipulates displayArea in the event-dispatch thread
   protected void displayMessage(final String messageToDisplay )
   {
      SwingUtilities.invokeLater(
         new Runnable() 
         {
            public void run() // updates displayArea
            {
               serverGUI.displayArea.append( messageToDisplay + "\n"); // append message
            } // end method run
         } // end anonymous inner class
      ); // end call to SwingUtilities.invokeLater
   } // end method displayMessage
   
   public static void main( String args[] )
   {
	   ServerSetUp server = new ServerSetUp(); // create server
    	server.serverInit();
   } // end main


} // end class TCPServerGUI


