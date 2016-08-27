import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Display{

	private JFrame frame;
	private int height = 500, width = 400;
	
	//Menu
	JButton start;
	private JLabel difficultyL, numRowsL;
	JTextField difficulty = new JTextField(5), numRows = new JTextField(5);
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
		
		start = new JButton("Start");
		
		frame.addKeyListener(gameDriver);
		start.addActionListener(gameDriver);
		
		layout = new JPanel(new GridLayout(5,1));
		
		
		difficultyL = new JLabel("Select difficulty: 0-easy, 5-hard");
		
		numRowsL = new JLabel("Select number of rows between 1 and 5");
		
		numRows.setText("3");
		difficulty.setText("1");

		layout.add(numRowsL);
		layout.add(numRows);
		layout.add(difficultyL);
		layout.add(difficulty);
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
