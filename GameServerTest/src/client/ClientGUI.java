package client;

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
import javax.swing.JTextField;

import ClientServer.ClientServerMessage;

public class ClientGUI extends JFrame implements ActionListener, WindowListener
{
	   /**
	 * 
	 */
		
	private static final long serialVersionUID = 1L;
	protected JTextField userName = new JTextField(10);   
	protected JTextField password = new JTextField(15);
	private JLabel userNameLabel = new JLabel("UserName:");
	private JLabel passwordLabel = new JLabel("Password:");
	private JLabel isConnected = new JLabel("Disconnected");
	private JButton connect = new JButton("Connect");
	private JButton login = new JButton("Login");
	private JButton logout = new JButton("Logout");
	protected JTextArea displayArea; // display information to user
	private ClientSetUp client;
	   
	public ClientGUI(ClientSetUp client)
	   { 
		   this.client = client;
		   this.setLayout(new BorderLayout());
		      JPanel p1 = new JPanel();
		      JPanel p2 = new JPanel();
		      
		      p1.setLayout(new GridLayout(2,3,5,5));
		      p2.setLayout(new GridLayout(1,2,5,5));
		      userName.setEditable( false );
		      userName.setAlignmentY(10);
		   
		      password.setEditable( false );
		      password.setAlignmentY(10);
		      
		      connect.addActionListener(this);
		      login.addActionListener(this);
		      login.setEnabled(false);
		      logout.addActionListener(this);
		      logout.setEnabled(false);
		      
		      p1.add(userNameLabel);
		      p1.add(userName);
		      p1.add(connect);
		      p1.add(passwordLabel);
		      p1.add(password);
		      p1.add(login);
		      add(p1, BorderLayout.NORTH );

		      displayArea = new JTextArea(); // create displayArea
		      displayArea.setEditable(false);
		      add( new JScrollPane( displayArea ), BorderLayout.CENTER );
		      p2.add(isConnected);
		      p2.add(logout);
		      add(p2, BorderLayout.SOUTH);
		      
		      setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		      setSize( 450, 450 ); // set size of window
		      setVisible( true ); // show window
	   }

	   
	public void windowClosing(WindowEvent e) {		
		dispose();
		System.exit(0);
	}
	   
	public void windowActivated(WindowEvent e) {	}
	public void windowClosed(WindowEvent e) {	}

	public void windowDeactivated(WindowEvent e) {	}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {	}


	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		
		if(o == connect){
			client.connectToServer();
			isConnected.setText("Connected");
			connect.setEnabled(false);
			login.setEnabled(true);
			logout.setEnabled(true);
		}
		if(o == login){
			String UN = userName.getText().trim();
			String PW = password.getText().trim();
			client.sendMessage(new ClientServerMessage(ClientServerMessage.MESSAGE, UN ));
			client.sendMessage(new ClientServerMessage(ClientServerMessage.MESSAGE, PW));

		}
		
		if(o == logout){
        	System.out.println("Logging out");
        	client.endConnection = true;
        	client.closeConnection();
        	isConnected.setText("Disconnected");
        	connect.setEnabled(true);
        	login.setEnabled(false);
        	logout.setEnabled(false);
		}
		
	}
}
