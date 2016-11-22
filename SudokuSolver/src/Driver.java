import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Driver extends JPanel {

	protected static int numRows, numCols;
	private int rowSize = 90, colSize = 90, width, height;
	private Display display;
	protected static Boxes boxesArray[][];
	protected static Groups blockArray[];
	protected static Groups rowArray[];
	protected static Groups columnArray[];
	private JFrame frame;
	private ArrayList<Boxes> shaded;
	protected static String[] tokens;
	protected static boolean foundValue = true;
	
	/*
	 * Change the path of the file to put a new game in
	 * or solve a different puzzle
	 */
	public Driver(){
		//String file = Utils.loadFileAsString("res/easy-game1.txt");
		//String file = Utils.loadFileAsString("res/medium-game1.txt");
		//String file = Utils.loadFileAsString("res/hard-game2.txt");
		String file = Utils.loadFileAsString("res/extreme-game1.txt");
		tokens = file.split("\\s+"); //Splits up every number into their own string separated by any white space
		numRows = 9;
		numCols = 9;
		width = numRows * rowSize + 7;
		height = numCols * colSize + 30;
		display = new Display(width,height);
		blockArray = new Groups[9];
		rowArray = new Groups[9];
		columnArray = new Groups[9];
		frame = display.getFrame();
		frame.add(this);
		//Used for going through the shaded list
		shaded = new ArrayList<Boxes>();
		
		SearchingAlgorithm.setNumbers();
		SearchingAlgorithm.setGroupNumbers();
		solveSudoku();
		
	}
	
	private void solveSudoku(){
		
	boolean running = true;
	//I used 2 to make the iteration go faster, 1 per second seemed too slow
	double timePerTick = 1000000000/2;
	double delta = 0;
	long now;
	long lastTime = System.nanoTime();
	long timer = 0;
	int ticks = 0;
	

	while(running){
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;

			if(delta >= 1){
				

				if(foundValue){
					foundValue = false;
					SearchingAlgorithm.searchPossibleValues();
					SearchingAlgorithm.searchGroups();
					SearchingAlgorithm.checkHiddenNumbers();
					System.out.println("");
					
			
				}else{
					if(SearchingAlgorithm.validiateSolution()){
						JOptionPane.showMessageDialog(frame, "The search is completed and valid!.", "Solution Complete", JOptionPane.PLAIN_MESSAGE);
						break;
					}else{
						printAllPossibleValues();
						JOptionPane.showMessageDialog(frame, "There are missing or incorrect values", "Solution Incomplete", JOptionPane.WARNING_MESSAGE);
						break;
					}
				}
				
				repaint();
				
				ticks++;
				delta--;
			}
			
			if(timer >= 1000000000){
				System.out.println("Iteration per second: " + ticks);
				ticks = 0;
				timer = 0;
			}		
		}
	}

	
	public static void main(String[] args) {

		new Driver();

	}
	
	
	public void printAllPossibleValues(){
		System.out.println("");
		for(int i = 0; i < blockArray.length; i++){
			for(int j = 0; j < blockArray[i].getGroup().length; j++){							
					blockArray[i].getGroup()[j].printPossibleValues();								
			}
			System.out.println("");
		}
	}
	
	

	public void paint(Graphics g) {
		g.clearRect(0, 0, width, height);
		g.setColor(Color.BLACK);
		

		int barSize = 6;
		g.fillRect(colSize*3, 0, barSize ,height);
		g.fillRect(colSize*6, 0, barSize ,height);
		g.fillRect(0, rowSize*3, width ,barSize);
		g.fillRect(0, rowSize*6, width ,barSize);
		
		//This paints the circle 
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){

				//Draws the Grid			
				g.drawRect(i * rowSize, j * colSize, rowSize, colSize);
				
				//Draws the number if not 0, if 0 I'll print out all the possibilites
				if(boxesArray[i][j].getValue() != 0){
					g.setFont(new Font("TimesRoman", Font.BOLD, 20));
					g.drawString( Integer.toString(boxesArray[i][j].getValue()), boxesArray[i][j].getcolPos()*colSize+colSize/2-6, boxesArray[i][j].getrowPos()*rowSize+rowSize/2+4);
				}else{
					
					int xOffset = 10;
					int yOffset = 0;
					for(int k = 1; k < 10;k++){

							if((k-1)%3 == 0){
								xOffset = 10;
								yOffset += 25;
							}
							if(boxesArray[i][j].getPossibleValues()[k] == 0){
								g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
								g.setColor(Color.GRAY);
								g.drawString( Integer.toString(k), boxesArray[i][j].getcolPos()*colSize+xOffset, boxesArray[i][j].getrowPos()*rowSize+yOffset);
								g.setColor(Color.BLACK);
							}
							xOffset += colSize/3;

						
					}
					

				}
				
				//If box is a starter, shade it a bit to distinguish it
				if(boxesArray[i][j].isStarter()){
					g.fillRect(boxesArray[i][j].getcolPos()*colSize, boxesArray[i][j].getrowPos()*rowSize, colSize, barSize/2);
					g.fillRect(boxesArray[i][j].getcolPos()*colSize, boxesArray[i][j].getrowPos()*rowSize+rowSize-barSize/2+2, colSize, barSize/2);
					g.fillRect(boxesArray[i][j].getcolPos()*colSize, boxesArray[i][j].getrowPos()*rowSize, barSize/2, rowSize);
					g.fillRect(boxesArray[i][j].getcolPos()*colSize+colSize-barSize/2+2, boxesArray[i][j].getrowPos()*rowSize, barSize/2, rowSize);
				}
			}	
		}
		
	}
	
	
}
