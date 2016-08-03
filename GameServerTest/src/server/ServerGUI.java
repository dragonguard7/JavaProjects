package server;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import database.DatabaseManager;

public class ServerGUI extends JFrame implements ActionListener, WindowListener
{
	
	private static final long serialVersionUID = 1L;
	JButton serverSS = new JButton("Server On/Off"); // inputs message from user
	JButton showUsers = new JButton("Show database users");
	JLabel serverStatus = new JLabel("On");
	JTextArea displayArea; // display information to user
	ServerSetUp server;
    DatabaseManager DBM = null;
   

   // set up GUI
   public ServerGUI(ServerSetUp server)
   {
      super( "Game Server" );
      this.server = server;
      serverSS.addActionListener(this);
      showUsers.addActionListener(this);
      JPanel p1 = new JPanel();
      serverStatus.setAlignmentY(CENTER_ALIGNMENT);
      p1.setLayout(new GridLayout(1,2));
      p1.add( serverSS, BorderLayout.NORTH );
      p1.add(serverStatus, BorderLayout.CENTER);
      
      this.add(p1, BorderLayout.NORTH);
      
      displayArea = new JTextArea(); // create displayArea
      this.add( new JScrollPane( displayArea ), BorderLayout.CENTER );
      this.add(showUsers, BorderLayout.SOUTH);
      
      displayArea.setEditable(false);     
     
      this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      this.setSize( 325, 400 ); // set size of window
      this.setVisible( true ); // show window
   } // end Server GUI constructor


@Override
public void windowClosing(WindowEvent arg0) {
	server.stop();
	dispose();
	System.exit(0);
}

public void windowActivated(WindowEvent arg0) {}
public void windowClosed(WindowEvent arg0) {}
public void windowDeactivated(WindowEvent arg0) {}
public void windowDeiconified(WindowEvent arg0) {}
public void windowIconified(WindowEvent arg0) {}
public void windowOpened(WindowEvent arg0) {}


@Override
public void actionPerformed(ActionEvent action) {
	Object o = action.getSource();
	
	if(o == serverSS){
		server.serverUp = !server.serverUp;
		if(server.serverUp){
			serverStatus.setText( "On" );
		}else{
			serverStatus.setText( "Off" );
		}
	}
	
	if(o == showUsers){
	      if(DBM == null){
	    	  new DatabaseManager();  
	      }
	}
}
} 
   
