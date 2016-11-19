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
	private int rowSize = 80, colSize = 80, width, height;
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
		String file = Utils.loadFileAsString("res/medium-game1.txt");
		//String file = Utils.loadFileAsString("res/hard-game1.txt");
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
			
				}else{
					if(SearchingAlgorithm.validiateSolution()){
						JOptionPane.showMessageDialog(frame, "The search is completed.", "Solution Complete", JOptionPane.PLAIN_MESSAGE);
						break;
					}else{
						printAllPossibleValues();
						JOptionPane.showMessageDialog(frame, "There are missing or incorrect values", "Solution Incomplete", JOptionPane.WARNING_MESSAGE);
						break;
					}
				}
				
				
				//findNext();
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
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){
				if(boxesArray[i][j].getPossibleValues()[0] > 1)
					boxesArray[i][j].printPossibleValues();
			}
			System.out.println("");
		}
	}
	
	public void paint(Graphics g) {
		g.clearRect(0, 0, width, height);
		g.setColor(Color.BLACK);
		
		int xOffset = 0;
		int yOffset = 0;
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
				
				//Draws the number if not 0, if 0 just leaves box empty
				if(boxesArray[i][j].getValue() != 0){
					g.drawString( Integer.toString(boxesArray[i][j].getValue()), boxesArray[i][j].getcolPos()*colSize+colSize/2-6, boxesArray[i][j].getrowPos()*rowSize+rowSize/2+4);
				}else{
					g.drawString( " ", boxesArray[i][j].getcolPos()*colSize+colSize/2-6, boxesArray[i][j].getrowPos()*rowSize+rowSize/2+4);

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
		
		
		//This draws the shaded parts, I could put it above
		//but I wanted to use an iterator
		Iterator<Boxes> it = shaded.iterator();
		while(it.hasNext()){
			
			Boxes b = it.next();
			g.setColor(Color.black);
			g.fillRect(b.getcolPos()*colSize, b.getrowPos()*rowSize, colSize, rowSize/3);
			g.fillRect(b.getcolPos()*colSize, b.getrowPos()*rowSize+2*rowSize/3+1, colSize, rowSize/3);
			g.fillRect(b.getcolPos()*colSize, b.getrowPos()*rowSize, colSize/3, rowSize);
			g.fillRect(b.getcolPos()*colSize+2*colSize/3+1, b.getrowPos()*rowSize, colSize/3, rowSize);
		}
	}
	
	
}
