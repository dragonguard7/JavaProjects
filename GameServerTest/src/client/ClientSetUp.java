package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.SwingUtilities;

import ClientServer.ClientServerMessage;

public class ClientSetUp 
{

   private ClientGUI clientGUI;
   private ObjectOutputStream sOutput; // output stream to server
   private ObjectInputStream sInput; // input stream from server
   private Socket client; // socket to communicate with server
   private int port = 12345;
   private String userName, password;
   
   
   OutputStream output; // output stream to client
   InputStream input; // input stream from client
   BufferedReader buffRead;
   private String message = "";
   private String chatServer; // host server for this application

   protected boolean endConnection = false;

   // initialize chatServer and set up GUI
   public ClientSetUp( String host )
   {	     
      chatServer = host; // set server to which this client connects
      
      clientGUI = new ClientGUI(this); 


   } // end Client constructor

   public void runClient()
   {
	   
		while(!endConnection){
			
		}
		
		System.out.println("Exit client loop");	
	
   }

private String recieveData() {
	String inputMessage = null;
	try{
		inputMessage = buffRead.readLine();
	}catch ( IOException ioException ) 
	   {
	      ioException.printStackTrace();
	   } // end catch 
	return inputMessage;
}

//************************************************************************************************
   // connect to server
   protected void connectToServer()
   {      
      
	   try {
		   
		    System.out.println("Connecting to Server...");
		    client = new Socket(chatServer , port );		
		    // display connection information

			setTextFieldEditable( true );
	    }catch(Exception ec) {
			displayMessage("Error connectiong to server:" + ec);
		}
	   	clientGUI.displayArea.append( "Connected to: " + client.getInetAddress().getHostName() + "\n");
	   	clientGUI.displayArea.append("Setting up Client I/O Streams...\n");			
	   
		try{
			sInput  = new ObjectInputStream(client.getInputStream());
			sOutput = new ObjectOutputStream(client.getOutputStream());
		}
		catch (IOException eIO) {
			displayMessage("Exception creating new Input/output Streams: " + eIO);
		}
	   
		new ListenFromServer().start();

		//send initial message to server if desired
		//sendMessage(new ClientServerMessage(ClientServerMessage.MESSAGE, "Test1"));

   } // end method connectToServer


 //*************************************************************************************************    
   // close streams and socket
   protected void closeConnection() 
   {
      clientGUI.displayArea.append( "Closing connection" );
      setTextFieldEditable( false ); // disable enterField

      try 
      {
    	 if(sOutput != null)
    		 sOutput.close(); // close output stream
         if(sInput != null)
        	 sInput.close(); // close input stream
         if(client != null)
        	 client.close(); // close socket
      } // end try
      catch (Exception exception ) 
      {
    	 displayMessage("Exception in closeConnection");
         exception.printStackTrace();
      } // end catch
   } // end method closeConnection

 //*************************************************************************************************    
   // send message to server
   protected void sendMessage( ClientServerMessage message )
   {
      try // send object to server
      {
    	 displayMessage("Trying to send data... " + message.getMessage() + "\n") ;
    	 sOutput.writeObject(message);
      } // end try
      catch ( IOException ioException )
      {
			displayMessage("Exception writing to server: " + ioException);
      } // end catch
   } // end method sendData

 //*************************************************************************************************    
   // manipulates displayArea in the event-dispatch thread
   private void displayMessage( String messageToDisplay )
   {
	   clientGUI.displayArea.append( messageToDisplay + "\n");
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
               System.out.println("Setting fields to: " + editable);
               clientGUI.userName.setEditable( editable );
               clientGUI.password.setEditable(editable);
            } // end method run
         } // end anonymous inner class
      ); // end call to SwingUtilities.invokeLater
   } // end method setTextFieldEditable
   
   public static void main( String args[] )
   {
      ClientSetUp application; // declare client application

      // if no command line args
      if ( args.length == 0 )
         application = new ClientSetUp( "127.0.0.1" ); // connect to localhost
      else
         application = new ClientSetUp( args[ 0 ] ); // use args to connect

     application.runClient(); // run client application
   }
   
   class ListenFromServer extends Thread {

		public void run() {
			while(true) {
				try {
					String msg = (String) sInput.readObject();
					// if console mode print the message and add back the prompt
						displayMessage(msg);
					
				}
				catch(IOException e) {
					displayMessage("Server has close the connection: " + e);
					break;
				}
				// can't happen with a String object but need the catch anyhow
				catch(ClassNotFoundException e2) {
				}
			}
		}
	}
   
} // end class TCPClientGUI

