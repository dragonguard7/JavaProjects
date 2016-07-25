/* Name: Ryan Shoaf
 * Course: CNT 4714 Summer 2012 
 * Assignment title: Program 2 – Socket-based File Server
*/
import javax.swing.JFrame;

public class TCPClientDriver {
	   public static void main( String args[] )
	   {
	      TCPClientGUI application; // declare client application

	      // if no command line args
	      if ( args.length == 0 )
	         application = new TCPClientGUI( "127.0.0.1" ); // connect to localhost
	      else
	         application = new TCPClientGUI( args[ 0 ] ); // use args to connect

	      application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	      application.runClient(); // run client application
	   } // end main
}//end TCPClientDriver