package client;

import javax.swing.JFrame;

public class ClientDriver {
	   public static void main( String args[] )
	   {
	      ClientGUI application; // declare client application

	      // if no command line args
	      if ( args.length == 0 )
	         application = new ClientGUI( "127.0.0.1" ); // connect to localhost
	      else
	         application = new ClientGUI( args[ 0 ] ); // use args to connect

	      application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	      application.runClient(); // run client application
	   }
}
