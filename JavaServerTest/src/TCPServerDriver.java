/* Name: Ryan Shoaf
 * Course: CNT 4714 Summer 2012 
 * Assignment title: Program 2 – Socket-based File Server
*/

import javax.swing.JFrame;

public class TCPServerDriver {
	   public static void main( String args[] )
	   {
	      TCPServerGUI application = new TCPServerGUI(); // create server
	      application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	      application.runServer(); // run server application
	   } // end main
}
