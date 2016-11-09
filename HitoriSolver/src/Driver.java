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
	private Numbers numbers[];
	private JFrame frame;
	private ArrayList<Numbers> shaded;
	private boolean intruderFound;
	private Queue<Numbers> search;
	private String[] tokens;
	
	public Driver(){
		String file = Utils.loadFileAsString("res/game2-8x8.txt");
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
		
		intruderFound = false;
		setNumbers();
		findStarters();
		solveHitori();
		
	}
	
	private void solveHitori(){
		
	boolean running = true;
	//I used 2 to make the iteration go faster, 1 per second seemed too slow
	double timePerTick = 1000000000 /2;
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
				
				if(search.peek() == null){
					System.out.println("Done searching");
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

	

		JOptionPane.showMessageDialog(frame, "The search is over.", "Search Complete", JOptionPane.PLAIN_MESSAGE);

	
	}
	
	/*
	 * Here I randomly initialize the grid. Start with all Open,
	 * then randomize the specific number of walls,
	 * then enemy, then player.
	 */
	private void setNumbers(){
		blockArray = new Numbers[numRows][numCols];
		numbers = new Numbers[numRows * numCols];
		System.out.println("Third " + Utils.parseInt(tokens[2]));
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){					
				blockArray[i][j] = new Numbers(Numbers.Type.UNKNOWN, Utils.parseInt(tokens[j+i*numCols+2]), i, j);
								
				//System.out.println(blockArray[i][j].getValue()+ " RowPos: " + blockArray[i][j].getrowPos() + " ColPos: " + blockArray[i][j].getcolPos());
				System.out.print(blockArray[i][j].getValue() + " ");
			}
			System.out.println(" ");
		}
		
	}

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
				System.out.println((k+1) + " , " + (column+1) + " THIS should be shaded");
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
				System.out.println((row+1) + " , " + (k+1) + " This should be shaded");
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
			

			System.out.println("\n\nLooking at row: " +(xPos+1) + " Column: " + (yPos+1));	

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
