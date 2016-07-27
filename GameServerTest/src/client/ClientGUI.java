package client;

/* Name: Ryan Shoaf
 * Course: CNT 4714 Summer 2012 
 * Assignment title: Program 2 � Socket-based File Server
*/

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ClientGUI extends JFrame 
{
	
   private JTextField userName = new JTextField(10);
   private JTextField password = new JTextField(15);
   private JLabel userNameLabel = new JLabel("UserName:");
   private JLabel passwordLabel = new JLabel("Password:");
   private JButton login = new JButton("Login");
   
   private JTextArea displayArea; // display information to user

   
   private ObjectOutputStream output; // output stream to server
   private ObjectInputStream input; // input stream from server
   private String message = ""; // message from server
   private String chatServer; // host server for this application
   private Socket client; // socket to communicate with server

   // initialize chatServer and set up GUI
   public ClientGUI( String host )
   {
	   	   
      super( "TCP Client" );

      chatServer = host; // set server to which this client connects
      this.setLayout(new BorderLayout());
      JPanel p1 = new JPanel();
//      JPanel p2 = new JPanel();
      
      p1.setLayout(new GridLayout(1,5,5,5));
      userName.setEditable( false );
      userName.setAlignmentY(10);
      
      
      login.addActionListener(
         new ActionListener() 
         {
            // send message to server
            public void actionPerformed( ActionEvent event )
            {
               //sendData(event.getActionCommand());
               sendData(userName.getText() + password.getText());
               
               //enterField.setText("");
            } // end method actionPerformed
         } // end anonymous inner class
      ); // end call to addActionListener
           
      password.setEditable( false );
      password.setAlignmentY(10);
      /*
      password.addActionListener(
         new ActionListener() 
         {
            // send message to server
            public void actionPerformed( ActionEvent event )
            {
               sendData(event.getActionCommand());
               //enterField.setText("");
            } // end method actionPerformed
         } // end anonymous inner class
      ); // end call to addActionListener
      */
      p1.add(userNameLabel);
      p1.add(userName);
      p1.add(passwordLabel);
      p1.add(password);
      p1.add(login);
      add(p1, BorderLayout.NORTH );

      displayArea = new JTextArea(); // create displayArea
      displayArea.setEditable(false);
      add( new JScrollPane( displayArea ), BorderLayout.CENTER );

      setSize( 450, 450 ); // set size of window
      setVisible( true ); // show window
   } // end Client constructor

 //************************************************************************************************* 
   // connect to server and process messages from server
   public void runClient() 
   {
      try // connect to server, get streams, process connection
      {
         connectToServer(); // create a Socket to make connection
         getStreams(); // get the input and output streams
         processConnection(); // process connection
      } // end try
      catch ( EOFException eofException ) 
      {
         //displayMessage( "\nClient terminated connection" );
      } // end catch
      catch ( IOException ioException ) 
      {
         ioException.printStackTrace();
      } // end catch
      finally 
      {
         closeConnection(); // close connection
      } // end finally
   } // end method runClient

//************************************************************************************************
   // connect to server
   private void connectToServer() throws IOException
   {      
      //displayMessage( "Attempting connection\n" );

      // create Socket to make connection to server
      client = new Socket( InetAddress.getByName( chatServer ), 12345 );

      // display connection information
      //displayMessage( "Connected to: " + 
         //client.getInetAddress().getHostName() );
      
   } // end method connectToServer

 //*************************************************************************************************   
   // get streams to send and receive data
   private void getStreams() throws IOException
   {
      // set up output stream for objects
      output = new ObjectOutputStream( client.getOutputStream() );      
      output.flush(); // flush output buffer to send header information

      // set up input stream for objects
      input = new ObjectInputStream( client.getInputStream() );

      //displayMessage( "\nGot I/O streams\n" );
   } // end method getStreams

 //*************************************************************************************************       
   // process connection with server
   private void processConnection() throws IOException
   {
      // enable enterField so client user can send messages
      setTextFieldEditable( true );

      do // process messages sent from server
      { 
         try // read message and display it
         {
            message = ( String ) input.readObject(); // read new message
            displayMessage( message + "\n" ); // display message
         } // end try
         catch ( ClassNotFoundException classNotFoundException ) 
         {
            displayMessage( "\nUnknown object type received" );
         } // end catch

      } while ( !message.equals( "SERVER>>> TERMINATE" ) );
   } // end method processConnection

 //*************************************************************************************************    
   // close streams and socket
   private void closeConnection() 
   {
      //displayMessage( "\nClosing connection" );
      setTextFieldEditable( false ); // disable enterField

      try 
      {
         output.close(); // close output stream
         input.close(); // close input stream
         client.close(); // close socket
      } // end try
      catch ( IOException ioException ) 
      {
         ioException.printStackTrace();
      } // end catch
   } // end method closeConnection

 //*************************************************************************************************    
   // send message to server
   private void sendData( String message )
   {
      try // send object to server
      {
    	 
         output.writeObject(message);
         output.flush(); // flush data to output
         //displayMessage(message);
      } // end try
      catch ( IOException ioException )
      {
         displayArea.append( "\nError writing object" );
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
               displayArea.append( messageToDisplay );
            } // end method run
         }  // end anonymous inner class
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
               userName.setEditable( editable );
               password.setEditable(editable);
            } // end method run
         } // end anonymous inner class
      ); // end call to SwingUtilities.invokeLater
   } // end method setTextFieldEditable
} // end class TCPClientGUI

