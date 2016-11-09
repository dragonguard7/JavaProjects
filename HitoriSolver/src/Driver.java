import java.awt.Color;
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

	private int numRows, numCols;
	private int rowSize = 50, colSize = 50, width, height;
	private Display display;
	private Numbers blockArray[][];
	private JFrame frame;
	private ArrayList<Numbers> shaded;
	private Queue<Numbers> search;
	private String[] tokens;
	private boolean searchDuplicates = false;
	
	/*
	 * Change the path of the file to put a new game in
	 * or solve a different puzzle
	 */
	public Driver(){
		String file = Utils.loadFileAsString("res/game4-12x12.txt");
		tokens = file.split("\\s+"); //Splits up every number into their own string separated by any white space
		numCols = Utils.parseInt(tokens[0]);
		numRows = Utils.parseInt(tokens[1]);
		width = numRows * rowSize + 7;
		height = numCols * colSize + 30;
		display = new Display(width,height);
		frame = display.getFrame();
		frame.add(this);
		//Used for going through the shaded list
		search = new LinkedList<Numbers>();
		shaded = new ArrayList<Numbers>();
		
		setNumbers();
		findStarters();
		solveHitori();
		
	}
	
	private void solveHitori(){
		
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
					if(searchDuplicates){
						if(checkRemaining() == 0){
							JOptionPane.showMessageDialog(frame, "The search is completed.", "Search Complete", JOptionPane.PLAIN_MESSAGE);
							break;
						}else{
							JOptionPane.showMessageDialog(frame, "More duplicates remaining", "Search Complete", JOptionPane.WARNING_MESSAGE);
							break;
						}
					}else{
						checkRemaining();
					}
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
	private void setNumbers(){
		blockArray = new Numbers[numRows][numCols];
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){					
				blockArray[i][j] = new Numbers(Numbers.Type.UNKNOWN, Utils.parseInt(tokens[j+i*numCols+2]), i, j);
								
			}

		}
		
	}

/*
 * As of right now I just do a basic look for numbers between two
 * of the same numbers in both rows and columns.
 * Ill expand to triplets and two of the same numbers together with another in column/row
 */
	public void findStarters(){
			
		//Look at each row...
		for(int i = 0; i < numRows;i++){
			for(int j = 0; j < numCols-2;j++){
				//System.out.println(blockArray[i][j].getValue() + " and " + blockArray[i][j+2].getValue());			
				if(blockArray[i][j].getValue() == blockArray[i][j+2].getValue()){
					//System.out.println("Match at Row: " + (i+1) + " Col: " + (j+2) + " value: " + blockArray[i][j+1].getValue());			
					blockArray[i][j+1].setBlockType(Numbers.Type.CIRCLE);
					int row = i;
					int col = j+1;
					int value = blockArray[i][j+1].getValue();
					
					searchColumn(col,value,row);
					searchRow(row,value,col);
					
				}
				
			}
		}
		
		//Look at each column...
		for(int i = 0; i < numCols;i++){
			for(int j = 0; j < numRows-2;j++){
				if(blockArray[j][i].getValue() == blockArray[j+2][i].getValue()){
					//System.out.println("Match at Row: " + (j+2) + " Col: " + (i+1) + " value: " + blockArray[j+1][i].getValue());			
					blockArray[j+1][i].setBlockType(Numbers.Type.CIRCLE);
					
					int row = j+1;
					int col = i;
					int value = blockArray[j+1][i].getValue();
					
					searchColumn(col,value,row);
					searchRow(row,value,col);
	
				}
			}
		}	
	}
	
	//Search a given column for a specified value excluding the position
	private void searchColumn(int column, int value, int position){
		
		for(int k = 0; k < numCols; k++){
			if(k != position && blockArray[k][column].getValue() == value && blockArray[k][column].getBlockType() == Numbers.Type.UNKNOWN){
				shaded.add(blockArray[k][column]);
				blockArray[k][column].setBlockType(Numbers.Type.SHADE);
				search.add(blockArray[k][column]);
			}									
		}
	}
	
	//Search a given row for a specified value excluding the position
	private void searchRow(int row, int value, int position){

		for(int k = 0; k < numRows; k++){
			if(k != position && blockArray[row][k].getValue() == value && blockArray[row][k].getBlockType() == Numbers.Type.UNKNOWN){
				shaded.add(blockArray[row][k]);
				blockArray[row][k].setBlockType(Numbers.Type.SHADE);
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
						
			Numbers queueHead = search.poll();
			
			int xPos = queueHead.getrowPos();
			int yPos = queueHead.getcolPos();
			
			//Check Left			
			if(yPos - 1 >= 0){
				blockArray[xPos][yPos-1].setBlockType(Numbers.Type.CIRCLE);
				searchColumn(yPos-1,blockArray[xPos][yPos-1].getValue(),xPos);
				searchRow(xPos,blockArray[xPos][yPos-1].getValue(),yPos-1);
			}
			
			//Check Below
			if(xPos + 1 < numRows){
				blockArray[xPos+1][yPos].setBlockType(Numbers.Type.CIRCLE);
				searchColumn(yPos,blockArray[xPos+1][yPos].getValue(),xPos+1);
				searchRow(xPos+1,blockArray[xPos+1][yPos].getValue(),yPos);

			}
		
			//Check Right
			if(yPos + 1 < numCols){
				//System.out.println("Right: " + blockArray[xPos][yPos+1].getValue());
				blockArray[xPos][yPos+1].setBlockType(Numbers.Type.CIRCLE);
				searchColumn(yPos+1,blockArray[xPos][yPos+1].getValue(),xPos);
				searchRow(xPos,blockArray[xPos][yPos+1].getValue(),yPos+1);
				
			}
				
			//Check Above
			if(xPos - 1 >= 0){
				//System.out.println("Above: " + blockArray[xPos-1][yPos].getValue());
				blockArray[xPos-1][yPos].setBlockType(Numbers.Type.CIRCLE);	
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
	/*
	 * This function goes into the logic in finding a trapped value
	 * A number or box is passed over and we want to check if it will
	 * cause a trapped value by looking at the 4 possible positions,
	 * above, below, left and right. We check each possibly and see if
	 * there is no way from there. If so, it will be a trapped value.
	 */
	public boolean createsTrapped(Numbers position){
		int xPos = position.getcolPos();
		int yPos = position.getrowPos();
		System.out.println("Testing row: " + (yPos+1) + " col: "+ (xPos+1));
		
		//Check above the given position
		if(yPos-1 >= 0){ //Is there one above?
			//check above 
			if(yPos-2 < 0 || blockArray[yPos-2][xPos].getBlockType() == Numbers.Type.SHADE){
				//check left
				if(xPos-1 < 0 || blockArray[yPos-1][xPos-1].getBlockType() == Numbers.Type.SHADE){
					//check right
					if(xPos+1 > numCols || blockArray[yPos-1][xPos+1].getBlockType() == Numbers.Type.SHADE){
						System.out.println(" Row: " + (yPos+1) + " Col: " + (xPos+1) + " is blocked above...");
						return true;
					}
				}
			}			
		}		
		
		//Check below
		if(yPos+1 < numRows){ //Is there one below?
			//check below
			if(yPos+2 >= numRows || blockArray[yPos+2][xPos].getBlockType() == Numbers.Type.SHADE){
				//check left
				if(xPos-1 < 0 || blockArray[yPos+1][xPos-1].getBlockType() == Numbers.Type.SHADE){
					//check right
					if(xPos+1 >= numCols || blockArray[yPos+1][xPos+1].getBlockType() == Numbers.Type.SHADE){
						System.out.println(" Row: " + (yPos+1) + " Col: " + (xPos+1) + " is blocked below...");
						return true;
					}
				}
			}			
		}
		//Check left
		if(xPos - 1 >= 0){
			//left
			if(xPos - 2 < 0 || blockArray[yPos][xPos-2].getBlockType() == Numbers.Type.SHADE){
				//above
				if(yPos - 1 < 0 || blockArray[yPos-1][xPos-1].getBlockType() == Numbers.Type.SHADE){
					//below
					if(yPos + 1 >= numRows || blockArray[yPos+1][xPos-1].getBlockType() == Numbers.Type.SHADE){
						System.out.println(" Row: " + (yPos+1) + " Col: " + (xPos+1) + " is blocked left...");
						return true;
					}
				}

			}
		}
		
		//Check right
		if(xPos + 1 < numCols){//Is there one to right?
			//right
			if(xPos + 2 >= numCols || blockArray[yPos][xPos+2].getBlockType() == Numbers.Type.SHADE){
				//above
				if(yPos - 1 < 0 || blockArray[yPos-1][xPos+1].getBlockType() == Numbers.Type.SHADE){
					//below
					if(yPos + 1 >= numRows || blockArray[yPos+1][xPos+1].getBlockType() == Numbers.Type.SHADE){
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

		//This paints the circle 
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){
				g.setColor(Color.BLACK);
				
				if(blockArray[i][j].getBlockType() == Numbers.Type.CIRCLE){
					g.drawOval(blockArray[i][j].getcolPos()*colSize+colSize/4, blockArray[i][j].getrowPos()*rowSize+rowSize/4, colSize/2, rowSize/2);
				}
				
				String num = Integer.toString(blockArray[i][j].getValue());

				g.drawRect(i * rowSize, j * colSize, rowSize, colSize);
				g.drawString( num, blockArray[i][j].getcolPos()*colSize+colSize/2-6, blockArray[i][j].getrowPos()*rowSize+rowSize/2+4);

			}	
		}
		
		//This draws the shaded parts, I could put it above
		//but I wanted to use an iterator
		Iterator<Numbers> it = shaded.iterator();
		while(it.hasNext()){
			
			Numbers b = it.next();
			g.setColor(Color.black);
			g.fillRect(b.getcolPos()*colSize, b.getrowPos()*rowSize, colSize, rowSize/3);
			g.fillRect(b.getcolPos()*colSize, b.getrowPos()*rowSize+2*rowSize/3+1, colSize, rowSize/3);
			g.fillRect(b.getcolPos()*colSize, b.getrowPos()*rowSize, colSize/3, rowSize);
			g.fillRect(b.getcolPos()*colSize+2*colSize/3+1, b.getrowPos()*rowSize, colSize/3, rowSize);
		}
	}
	
	
}
