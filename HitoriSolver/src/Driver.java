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

	/*
	 * Below you can change the size of the grid or number of walls
	 */
	private int numRows, numCols;
	private int rowSize = 50, colSize = 50, width, height;
	private Display display;
	private Numbers blockArray[][];
	private JFrame frame;
	private ArrayList<Numbers> shaded;
	private Queue<Numbers> search;
	private String[] tokens;
	
	public Driver(){
		String file = Utils.loadFileAsString("res/game1-12x12.txt");
		tokens = file.split("\\s+"); //Splits up every number into their own string separated by any white space
		numCols = Utils.parseInt(tokens[0]);
		numRows = Utils.parseInt(tokens[1]);
		width = numRows * rowSize + 7;
		height = numCols * colSize + 30;
		display = new Display(width,height);
		frame = display.getFrame();
		frame.add(this);
		//Used for breath first search
		search = new LinkedList<Numbers>();
		/*Used to keep track of traversed blocks
		(I could have used the visited property of blocks but 
		I wanted to show the use of ArrayList, and iterator)
		*/
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
				
				if(search.peek() == null && checkRemaining() == 0){
					System.out.println("Done searching");
					/*
					if (checkRemaining() != 0){
						JOptionPane.showMessageDialog(frame, "There are duplicates remaining...", "Search Complete", JOptionPane.WARNING_MESSAGE);

					}else
					*/
						JOptionPane.showMessageDialog(frame, "The search is completed.", "Search Complete", JOptionPane.PLAIN_MESSAGE);
						break;
					
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
	 * Here I randomly initialize the grid. Start with all Open,
	 * then randomize the specific number of walls,
	 * then enemy, then player.
	 */
	private void setNumbers(){
		blockArray = new Numbers[numRows][numCols];
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){					
				blockArray[i][j] = new Numbers(Numbers.Type.UNKNOWN, Utils.parseInt(tokens[j+i*numCols+2]), i, j);
								
				//System.out.println(blockArray[i][j].getValue()+ " RowPos: " + blockArray[i][j].getrowPos() + " ColPos: " + blockArray[i][j].getcolPos());
				System.out.print(blockArray[i][j].getValue() + " ");
			}
			System.out.println(" ");
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
		//System.out.println("Searching column (" + (column+1) + ") for value " + value + " excluding position "+ (position+1));
		for(int k = 0; k < numCols; k++){
			//System.out.print(blockArray[k][column].getValue() + " ");
			if(k != position && blockArray[k][column].getValue() == value && blockArray[k][column].getBlockType() == Numbers.Type.UNKNOWN){
				//System.out.println((k+1) + " , " + (column+1) + " THIS should be shaded");
				shaded.add(blockArray[k][column]);
				blockArray[k][column].setBlockType(Numbers.Type.SHADE);
				search.add(blockArray[k][column]);
			}
			
						
		}
		//System.out.println("");
	}
	//Search a given row for a specified value excluding the position
	private void searchRow(int row, int value, int position){
		//System.out.println("Searching row (" + (row+1) + ") for value " + value + " excluding position "+ (position+1));

		for(int k = 0; k < numRows; k++){
			//System.out.print(blockArray[row][k].getValue() + " ");
			if(k != position && blockArray[row][k].getValue() == value && blockArray[row][k].getBlockType() == Numbers.Type.UNKNOWN){
				//System.out.println((row+1) + " , " + (k+1) + " This should be shaded");
				shaded.add(blockArray[row][k]);
				blockArray[row][k].setBlockType(Numbers.Type.SHADE);
				search.add(blockArray[row][k]);
			}
				
			
		}
		//System.out.println("");
	}
	
	
	public void findNext(){
						
			Numbers queueHead = search.poll();
			
			
			int xPos = queueHead.getrowPos();
			int yPos = queueHead.getcolPos();
			

			//System.out.println("\n\nLooking at row: " +(xPos+1) + " Column: " + (yPos+1));	

			//Check Left			
			if(yPos - 1 >= 0){
				//System.out.println("Left: " + blockArray[xPos][yPos-1].getValue());
				//System.out.println("Row: " + blockArray[xPos][yPos-1].getrowPos() + " Col: " + blockArray[xPos][yPos-1].getcolPos());
				blockArray[xPos][yPos-1].setBlockType(Numbers.Type.CIRCLE);
				searchColumn(yPos-1,blockArray[xPos][yPos-1].getValue(),xPos);
				searchRow(xPos,blockArray[xPos][yPos-1].getValue(),yPos-1);
			}
			
			//Check Below
			if(xPos + 1 < numRows){
				//System.out.println("Below: " + blockArray[xPos+1][yPos].getValue());
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
	public int checkRemaining(){
		int counter = 0;
		//Check for any duplicates in rows
		for(int i = 0; i < numRows; i++){
			for (int j = 0; j < numCols; j++){
				
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
	
	public boolean createsTrapped(Numbers position){
		int xPos = position.getcolPos();
		int yPos = position.getrowPos();
		System.out.println("Testing row: " + (yPos+1) + " col: "+ (xPos+1));
		
		//Check above
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

		//This paints the wall (black) open (white), Enemy (red) and player (blue).
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){
				g.setColor(Color.BLACK);
				
				if(blockArray[i][j].getBlockType() == Numbers.Type.CIRCLE){
					g.drawOval(blockArray[i][j].getcolPos()*colSize+colSize/4, blockArray[i][j].getrowPos()*rowSize+rowSize/4, colSize/2, rowSize/2);
				}
				else if(blockArray[i][j].getBlockType() == Numbers.Type.SHADE){
					g.setColor(Color.BLACK);
				}
				
				String num = Integer.toString(blockArray[i][j].getValue());

				g.drawRect(i * rowSize, j * colSize, rowSize, colSize);
				g.drawString( num, blockArray[i][j].getcolPos()*colSize+colSize/2-3, blockArray[i][j].getrowPos()*rowSize+rowSize/2+3);

			}	
		}
		
		//This creates the Cyan dots showing the progression of BFS
		Iterator<Numbers> it = shaded.iterator();
		while(it.hasNext()){
			
			Numbers b = it.next();
			g.setColor(Color.black);
			//g.drawRect(b.getrowPos()*colSize+colSize/4, b.getcolPos()*rowSize+rowSize/4, colSize/2, rowSize/2);
			g.fillRect(b.getcolPos()*colSize, b.getrowPos()*rowSize, colSize, rowSize/3);
			g.fillRect(b.getcolPos()*colSize, b.getrowPos()*rowSize+2*rowSize/3+1, colSize, rowSize/3);
			g.fillRect(b.getcolPos()*colSize, b.getrowPos()*rowSize, colSize/3, rowSize);
			g.fillRect(b.getcolPos()*colSize+2*colSize/3+1, b.getrowPos()*rowSize, colSize/3, rowSize);
		}
	}
	
	
}
