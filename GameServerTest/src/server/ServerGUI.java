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

public class ServerGUI extends JFrame implements ActionListener, WindowListener
{
	
	private static final long serialVersionUID = 1L;
	JButton serverSS = new JButton("Server On/Off"); // inputs message from user
	JLabel serverStatus = new JLabel("On");
	JTextArea displayArea; // display information to user
	ServerSetUp server;
   

   // set up GUI
   public ServerGUI(ServerSetUp server)
   {
      super( "Game Server" );
      this.server = server;
      serverSS.addActionListener(this);
      JPanel p1 = new JPanel();
      serverStatus.setAlignmentY(CENTER_ALIGNMENT);
      p1.setLayout(new GridLayout(1,2));
      p1.add( serverSS, BorderLayout.NORTH );
      p1.add(serverStatus, BorderLayout.CENTER);
      
      add(p1, BorderLayout.NORTH);
      
      displayArea = new JTextArea(); // create displayArea
      add( new JScrollPane( displayArea ), BorderLayout.CENTER );
      displayArea.setEditable(false);
      setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

      setSize( 325, 400 ); // set size of window
      setVisible( true ); // show window
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
public void actionPerformed(ActionEvent arg0) {
	server.serverUp = !server.serverUp;
	if(server.serverUp){
		serverStatus.setText( "On" );
	}else{
		serverStatus.setText( "Off" );
	}
}
} 
   
