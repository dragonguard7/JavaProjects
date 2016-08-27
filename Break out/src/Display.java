import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class Display{

	private JFrame frame;
	private int height = 500, width = 400;
	
	//Menu
	JButton start;
	private JLabel ballSpeedL, numRowsL, playerSizeL;
	JTextField ballSpeed = new JTextField(5), numRows = new JTextField(5);
	ButtonGroup playerSize;
	JRadioButton small, medium, large;
	private JPanel layout;
	
	public Display(){
		frame = new JFrame("Break out");
		frame.setTitle("Break out");
		frame.setSize( width, height ); // set window size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Need to end the game if closed by X 
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
	}

	public void startMenu(GameDriver gameDriver){
				
		layout = new JPanel(new GridLayout(7,1));
		small = new JRadioButton("Small");
		medium = new JRadioButton("Medium");
		large = new JRadioButton("Large");
		playerSize = new ButtonGroup();
		JPanel radioPanel = new JPanel(new GridLayout(1,3));
		
		playerSize.add(small);
		playerSize.add(medium);
		playerSize.add(large);
		radioPanel.add(small);
		radioPanel.add(medium);
		radioPanel.add(large);
		medium.setSelected(true);
		
		start = new JButton("Start");
		
		frame.addKeyListener(gameDriver);
		start.addActionListener(gameDriver);

		
		
		ballSpeedL = new JLabel("Select ball speed: 0-slow, 5-fast");
		numRowsL = new JLabel("Select number of rows between 1 and 5");
		playerSizeL = new JLabel("Select player size: small, medium, large");
		
		numRows.setText("3");
		ballSpeed.setText("2");

		layout.add(numRowsL);
		layout.add(numRows);
		layout.add(ballSpeedL);
		layout.add(ballSpeed);
		layout.add(playerSizeL);
		layout.add(radioPanel);
		layout.add(start);
		
		frame.add(layout);
		
		frame.setVisible( true ); // display window 
	}
	
	
	public JFrame getFrame() {
		return frame;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
	
	
}
