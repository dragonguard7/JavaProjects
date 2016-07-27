package server;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ServerGUI extends JFrame 
{
   JButton serverSS = new JButton("Server On/Off"); // inputs message from user
   JLabel serverStatus = new JLabel("On");
   JTextArea displayArea; // display information to user
   

   // set up GUI
   public ServerGUI()
   {
      super( "Game Server" );

      JPanel p1 = new JPanel();
      serverStatus.setAlignmentY(CENTER_ALIGNMENT);
      p1.setLayout(new GridLayout(1,2));
      p1.add( serverSS, BorderLayout.NORTH );
      p1.add(serverStatus, BorderLayout.CENTER);
      
      add(p1, BorderLayout.NORTH);
      
      displayArea = new JTextArea(); // create displayArea
      add( new JScrollPane( displayArea ), BorderLayout.CENTER );
      displayArea.setEditable(false);

      setSize( 325, 400 ); // set size of window
      setVisible( true ); // show window
   } // end Server GUI constructor
} 
   
