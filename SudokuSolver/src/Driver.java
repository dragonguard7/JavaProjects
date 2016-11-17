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
	protected static Boxes blockArray[][];
	private JFrame frame;
	private ArrayList<Boxes> shaded;
	private Queue<Boxes> search;
	protected static String[] tokens;
	private boolean searchDuplicates = false;
	
	/*
	 * Change the path of the file to put a new game in
	 * or solve a different puzzle
	 */
	public Driver(){
		String file = Utils.loadFileAsString("res/easy-game1.txt");
		tokens = file.split("\\s+"); //Splits up every number into their own string separated by any white space
		numRows = 9;
		numCols = 9;
		width = numRows * rowSize + 7;
		height = numCols * colSize + 30;
		display = new Display(width,height);
		frame = display.getFrame();
		frame.add(this);
		//Used for going through the shaded list
		search = new LinkedList<Boxes>();
		shaded = new ArrayList<Boxes>();
		
		SearchingAlgorithm.setNumbers();
		SearchingAlgorithm.searchPossibleValues();
		//SearchingAlgorithm.searchNumber(1);
		//solveSudoku();
		
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
				
				/*
				 * I want to search until we run out of shaded
				 * boxes, however there could still be duplicates
				 * and we check it. I just do 1 pass, it can be
				 * adjusted if desired but it will tell you if
				 * there are more duplicates after doing it once.
				 */
				if(search.peek() == null){
					/*
					if(searchDuplicates){
						
						if(checkRemaining() == 0){
							
							break;
						}else{
							JOptionPane.showMessageDialog(frame, "More duplicates remaining", "Search Complete", JOptionPane.WARNING_MESSAGE);
							break;
						}
					}else{
						checkRemaining();
					}
					*/
					JOptionPane.showMessageDialog(frame, "The search is completed.", "Search Complete", JOptionPane.PLAIN_MESSAGE);
				}
				
				findNext();
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
/*
 * This reads numbers from a file, the first two
 *  are the cols and rows respectively
 * 
 */

/*
 * As of right now I just do a basic look for numbers between two
 * of the same numbers in both rows and columns.
 * Ill expand to triplets and two of the same numbers together with another in column/row
 */

	
	//Search a given column for a specified value excluding the position
	private void searchColumn(int column, int value, int position){
		
		for(int k = 0; k < numCols; k++){
			if(k != position && blockArray[k][column].getValue() == value){
				shaded.add(blockArray[k][column]);
				search.add(blockArray[k][column]);
			}									
		}
	}
	
	//Search a given row for a specified value excluding the position
	private void searchRow(int row, int value, int position){

		for(int k = 0; k < numRows; k++){
			if(k != position && blockArray[row][k].getValue() == value){
				shaded.add(blockArray[row][k]);

				search.add(blockArray[row][k]);
			}	
		}
	}
	
/*
 * This is exected from the main loop if there are values in the search
 * arraylist. The shaded or bad value is given and it will look at each
 * surrounding value and find other shaded values by search the rows 
 * and columns for the number
 */
	public void findNext(){
						
			Boxes queueHead = search.poll();
			
			int xPos = queueHead.getrowPos();
			int yPos = queueHead.getcolPos();
			
			//Check Left			
			if(yPos - 1 >= 0){

				searchColumn(yPos-1,blockArray[xPos][yPos-1].getValue(),xPos);
				searchRow(xPos,blockArray[xPos][yPos-1].getValue(),yPos-1);
			}
			
			//Check Below
			if(xPos + 1 < numRows){
				searchColumn(yPos,blockArray[xPos+1][yPos].getValue(),xPos+1);
				searchRow(xPos+1,blockArray[xPos+1][yPos].getValue(),yPos);

			}
		
			//Check Right
			if(yPos + 1 < numCols){
				//System.out.println("Right: " + blockArray[xPos][yPos+1].getValue());
				searchColumn(yPos+1,blockArray[xPos][yPos+1].getValue(),xPos);
				searchRow(xPos,blockArray[xPos][yPos+1].getValue(),yPos+1);
				
			}
				
			//Check Above
			if(xPos - 1 >= 0){
				//System.out.println("Above: " + blockArray[xPos-1][yPos].getValue());
				searchColumn(yPos,blockArray[xPos-1][yPos].getValue(),xPos-1);
				searchRow(xPos-1,blockArray[xPos-1][yPos].getValue(),yPos);
			}	
			
			
	}
	
	/*
	 * This was added to complete puzzles that have isolated "white/open"
	 * chucks since going by just shaded values won't find them. After
	 * doing the basic search through the found starting shaded values,
	 * We go through every row and column and find open duplicate numbers.
	 * These have to be accounted for but are most likely "cut-off" other
	 * numbers which is bad. So we do a basic search at each point to see if
	 * a surrounding value will get cut off.
	 */
	/*
	public int checkRemaining(){
		searchDuplicates = true;
		int counter = 0;
		//Check for any duplicates in rows
		for(int i = 0; i < numRows; i++){
			for (int j = 0; j < numCols; j++){
				
				/*
				 * If there is a duplicate then we want to see
				 * if it causes a value to be "trapped" or cut-off
				 * we do the same thing for columns
				 */
	/*
				if(blockArray[i][j].getBlockType() == Numbers.Type.UNKNOWN){
					for(int k = j+1; k < numCols; k++){
						if(blockArray[i][j].getValue() == blockArray[i][k].getValue() && blockArray[i][k].getBlockType() == Numbers.Type.UNKNOWN){
							System.out.println("There is still two numbers in the same row... at row: " + (i+1) + " Col: " + (j+1) + " and " + (k+1));
							if(createsTrapped(blockArray[i][j])){
								System.out.println("Row: " + (i+1) + " Col: " + (k+1) + " should be shaded.");
								blockArray[i][k].setBlockType(Numbers.Type.SHADE);
								search.add(blockArray[i][k]);
								shaded.add(blockArray[i][k]);
							}
							if(createsTrapped(blockArray[i][k])){
								System.out.println("Row: " + (i+1) + " Col: " + (j+1)+ " should be shaded.");
								blockArray[i][j].setBlockType(Numbers.Type.SHADE);
								search.add(blockArray[i][j]);
								shaded.add(blockArray[i][j]);
							}
							counter++;
						}
					}
				}

			}
		}

		//Check for any duplicates in columns
		for(int i = 0; i < numCols; i++){
			for (int j = 0; j < numRows; j++){
				
				if(blockArray[j][i].getBlockType() == Numbers.Type.UNKNOWN){
					for(int k = j+1; k < numRows; k++){
						if(blockArray[j][i].getValue() == blockArray[k][i].getValue() && blockArray[k][i].getBlockType() == Numbers.Type.UNKNOWN){
							System.out.println("There is still two numbers in the same col... at col: " + (i+1) + " Row: " + (j+1) + " and " + (k+1));
							if(createsTrapped(blockArray[j][i])){
								System.out.println("Row: " + (k+1) + " Col: " + (i+1) + " should be shaded.");
								blockArray[k][i].setBlockType(Numbers.Type.SHADE);
								search.add(blockArray[k][i]);
								shaded.add(blockArray[k][i]);
							}
							if(createsTrapped(blockArray[k][i])){
								System.out.println("Row: " + (j+1) + " Col: " + (i+1) + " should be shaded.");
								blockArray[j][i].setBlockType(Numbers.Type.SHADE);
								search.add(blockArray[j][i]);
								shaded.add(blockArray[j][i]);
							}
							counter++;
						}
					}
				}

			}
		}
		
		return counter;
	}	
	*/
	/*
	 * This function goes into the logic in finding a trapped value
	 * A number or box is passed over and we want to check if it will
	 * cause a trapped value by looking at the 4 possible positions,
	 * above, below, left and right. We check each possibly and see if
	 * there is no way from there. If so, it will be a trapped value.
	 */
	public boolean createsTrapped(Boxes position){
		int xPos = position.getcolPos();
		int yPos = position.getrowPos();
		System.out.println("Testing row: " + (yPos+1) + " col: "+ (xPos+1));
		
		//Check above the given position
		if(yPos-1 >= 0){ //Is there one above?
			//check above 
			if(yPos-2 < 0 ){
				//check left
				if(xPos-1 < 0 ){
					//check right
					if(xPos+1 > numCols){
						System.out.println(" Row: " + (yPos+1) + " Col: " + (xPos+1) + " is blocked above...");
						return true;
					}
				}
			}			
		}		
		
		//Check below
		if(yPos+1 < numRows){ //Is there one below?
			//check below
			if(yPos+2 >= numRows ){
				//check left
				if(xPos-1 < 0 ){
					//check right
					if(xPos+1 >= numCols ){
						System.out.println(" Row: " + (yPos+1) + " Col: " + (xPos+1) + " is blocked below...");
						return true;
					}
				}
			}			
		}
		//Check left
		if(xPos - 1 >= 0){
			//left
			if(xPos - 2 < 0 ){
				//above
				if(yPos - 1 < 0 ){
					//below
					if(yPos + 1 >= numRows){
						System.out.println(" Row: " + (yPos+1) + " Col: " + (xPos+1) + " is blocked left...");
						return true;
					}
				}

			}
		}
		
		//Check right
		if(xPos + 1 < numCols){//Is there one to right?
			//right
			if(xPos + 2 >= numCols ){
				//above
				if(yPos - 1 < 0){
					//below
					if(yPos + 1 >= numRows){
						System.out.println(" Row: " + (yPos+1) + " Col: " + (xPos+1) + " is blocked right...");
						return true;
					}
				}

			}
		}
		

		
		return false;
	}
	
	public static void main(String[] args) {

		new Driver();

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
				if(blockArray[i][j].getValue() != 0){
					g.drawString( Integer.toString(blockArray[i][j].getValue()), blockArray[i][j].getcolPos()*colSize+colSize/2-6, blockArray[i][j].getrowPos()*rowSize+rowSize/2+4);
				}else{
					g.drawString( " ", blockArray[i][j].getcolPos()*colSize+colSize/2-6, blockArray[i][j].getrowPos()*rowSize+rowSize/2+4);

				}
				
				//If box is a starter, shade it a bit to distinguish it
				if(blockArray[i][j].isStarter()){
					g.fillRect(blockArray[i][j].getcolPos()*colSize, blockArray[i][j].getrowPos()*rowSize, colSize, barSize/2);
					g.fillRect(blockArray[i][j].getcolPos()*colSize, blockArray[i][j].getrowPos()*rowSize+rowSize-barSize/2+2, colSize, barSize/2);
					g.fillRect(blockArray[i][j].getcolPos()*colSize, blockArray[i][j].getrowPos()*rowSize, barSize/2, rowSize);
					g.fillRect(blockArray[i][j].getcolPos()*colSize+colSize-barSize/2+2, blockArray[i][j].getrowPos()*rowSize, barSize/2, rowSize);
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
