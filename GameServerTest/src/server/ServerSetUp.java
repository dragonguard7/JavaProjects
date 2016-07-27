package server;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ServerSetUp implements Runnable
{

   private ObjectOutputStream output; // output stream to client
   private ObjectInputStream input; // input stream from client  
   private Socket connection; // connection to client
   private int counter = 1; // counter of number of connections
   
   ServerGUI serverGUI;
   
   protected ServerSocket server; // server socket
   protected boolean serverUp = true, running = false;
   protected int serverPort = 12345;
   protected Thread runningThread = null;

   public void ServerInit(){
	   serverGUI = new ServerGUI();
	   serverGUI.serverSS.addActionListener(
		         new ActionListener() 
		         {
		            // send message to client
		            public void actionPerformed( ActionEvent event )
		            {
		               //sendData( event.getActionCommand() );
		            	serverUp = !serverUp;
		            	if(serverUp){
		            		serverGUI.serverStatus.setText( "On" );
		            	}else{
		            		serverGUI.serverStatus.setText( "Off" );
		            	}
		            } // end method actionPerformed
		         } // end anonymous inner class
		      ); // end call to addActionListener
	      serverGUI.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	      
	      
	   while(true){
		  if(serverUp && !running){
			  System.out.println("Starting server");
			  serverGUI.displayArea.append("Starting server\n");
			  new Thread(this).start();
			  running = true;
		  }
	      try{
	    	  while(serverUp){
	    		  Thread.sleep(2 * 1000);
	    	  }
	      }catch(InterruptedException e){
	    	  e.printStackTrace();
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
	         server = new ServerSocket( serverPort, 100 ); // create ServerSocket
	      } catch (IOException e){
	          throw new RuntimeException("Cannot open port " + serverPort,e);
	      } // end catch
	   while(isServerUp()){
		   Socket clientSocket = null;
		   try{
			   clientSocket = this.server.accept();
		   }catch(IOException e){
			   if(!isServerUp()){
				   serverGUI.displayArea.append("Server IOException: Server Stopped\n");
				   System.out.println("Server stopped.");
				   return;
			   }   
			   throw new RuntimeException("Error acception client connection",e);
		   }
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
   // set up and run server 
   public void runServer()
   {
	 //while(serverUp){
      try // set up server to receive connections; process connections
      {
         server = new ServerSocket( serverPort, 100 ); // create ServerSocket
/*   
         while ( true ) 
         {
        	 
        	 
     	 
            try 
            {
               waitForConnection(); // wait for a connection
               getStreams(); // get input & output streams
               processConnection(); // process connection
            } // end try
            catch ( EOFException eofException ) 
            {
               displayMessage( "\nServer terminated connection" );
            } // end catch
            finally 
            {
               closeConnection(); //  close connection
               counter++;
            } // end finally
            System.out.println(serverUp);                      
         } // end while
*/      
      } // end try 
      catch ( IOException ioException ) 
      {
         ioException.printStackTrace();
      } // end catch
      while(true){
    	  try{
    		  waitForConnection();
    	  }catch(IOException e){
    		  System.out.println("I/O error: " + e);
    	  }
    	  new Thread(new ServerThreads(connection, serverGUI)).start();
      }

   } // end method runServer
   
 //************************************************************************************************* 
   // wait for connection to arrive, then display connection info
   private void waitForConnection() throws IOException
   {
      displayMessage( "Waiting for connection\n" );
      connection = server.accept(); // allow server to accept connection            
      displayMessage( "Connection " + counter + " received from: " +
         connection.getInetAddress().getHostName() + "\n" );
        
      
   } // end method waitForConnection
   
 //************************************************************************************************* 
   // get streams to send and receive data
   private void getStreams() throws IOException
   {
      // set up output stream for objects
      output = new ObjectOutputStream( connection.getOutputStream() );
      output.flush(); // flush output buffer to send header information

      // set up input stream for objects
      input = new ObjectInputStream( connection.getInputStream() );

      displayMessage( "\nGot I/O streams\n" );
      
      displayMessage( "\nSERVER>>> Connection successful");
   } // end method getStreams
   
 //************************************************************************************************* 
   // process connection with client
   private void processConnection() throws IOException
   {
     //String message = "Connection sucess!";
     String fileLine;
     //sendData( message ); // send connection successful message

      // enable enterField so server user can send messages
      setTextFieldEditable( true );
    	  
         try // read message and display it
         {
        	 String fileName = (String) input.readObject(); // read new message
        	 
        	 output.writeObject("The contents of the file you requested:\n");
        	 /*
        	 BufferedReader file = new BufferedReader(new FileReader(fileName));       	
        	  
        	 while(file.ready()){
        		 fileLine = file.readLine();
        		 sendData(fileLine);
        	 }        	        	
        	 file.close();
        	 */
        	 displayMessage("\n" + fileName);
         } // end try
         catch ( ClassNotFoundException classNotFoundException ) 
         {
            displayMessage( "\nUnknown object type received" );
         } // end catch
         catch (FileNotFoundException fnfe)
         {
        	 sendData("File not found. Be sure to enter full file path.");
         }

   } // end method processConnection
   
 //************************************************************************************************* 
   // close streams and socket
   private void closeConnection() 
   {
      displayMessage( "\nTerminating connection\n" );
      setTextFieldEditable( false ); // disable enterField

      try 
      {
         output.close(); // close output stream
         input.close(); // close input stream
         connection.close(); // close socket
      } // end try
      catch ( IOException ioException ) 
      {
         ioException.printStackTrace();
      } // end catch
   } // end method closeConnection
   
 //************************************************************************************************* 
   // send message to client
   private void sendData( String message )
   {
      try // send object to client
      {
         //output.writeObject( "The contents of the file you requested:\n\n");
    	  output.writeObject( message );
         output.flush(); // flush output to client
         displayMessage( "\nSERVER>>> " + message );
      } // end try
      catch ( IOException ioException ) 
      {
         serverGUI.displayArea.append( "\nError writing object" );
      } // end catch
   } // end method sendData
   
 //************************************************************************************************* 
   // manipulates displayArea in the event-dispatch thread
   private void displayMessage( final String messageToDisplay )
   {
      SwingUtilities.invokeLater(
         new Runnable() 
         {
            public void run() // updates displayArea
            {
               serverGUI.displayArea.append( messageToDisplay ); // append message
            } // end method run
         } // end anonymous inner class
      ); // end call to SwingUtilities.invokeLater
   } // end method displayMessage
   
 //************************************************************************************************* 
   // manipulates enterField in the event-dispatch thread
   private void setTextFieldEditable( final boolean editable )
   {
	  
      SwingUtilities.invokeLater(
         new Runnable()
         {
            public void run() // sets enterField's editability
            {
               //serverGUI.enterField.setEditable( editable );
            } // end method run
         }  // end inner class
      ); // end call to SwingUtilities.invokeLater
   } // end method setTextFieldEditable



} // end class TCPServerGUI


