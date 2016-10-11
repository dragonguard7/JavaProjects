import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GameDriver extends JPanel {

	/*
	 * Below you can change the size of the grid or number of walls
	 */
	private int numRows = 7, numCols = 5, numWalls = (int)(numRows * numCols * .2);
	private int rowSize = 100, colSize = 100, width, height;
	private Display display;
	private Block blockArray[][];
	private JFrame frame;
	private ArrayList<Block> blocks;
	private boolean intruderFound;
	private Queue<Block> breathSearch;
	
	public GameDriver(){
		
		width = numRows * rowSize + 7;
		height = numCols * colSize + 30;
		display = new Display(width,height);
		frame = display.getFrame();
		frame.add(this);
		//Used for breath first search
		breathSearch = new LinkedList<Block>();
		/*Used to keep track of traversed blocks
		(I could have used the visited property of blocks but 
		I wanted to show the use of ArrayList, and iterator)
		*/
		blocks = new ArrayList<Block>();
		
		intruderFound = false;
		createBlocks();
		traverseMaze();
		
	}
	
	private void traverseMaze(){
		
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
				
				if(breathSearch.peek() == null || intruderFound){
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

		if(intruderFound){
			JOptionPane.showMessageDialog(frame, "There is an intruder!!!", "Intruder Alert!", JOptionPane.WARNING_MESSAGE);
		}else{
			JOptionPane.showMessageDialog(frame, "No intruder detected.", "No intruder", JOptionPane.PLAIN_MESSAGE);
		}
	
	}
	
	/*
	 * Here I randomly initialize the grid. Start with all Open,
	 * then randomize the specific number of walls,
	 * then enemy, then player.
	 */
	private void createBlocks(){
		blockArray = new Block[numRows][numCols];
		
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){	
				blockArray[i][j] = new Block(Block.Type.OPEN, i, j);
			}
		}
		Random rand = new Random();
		int randRow, randCol;
		
		//Create walls
		for(int i = 0; i < numWalls; i++){
			randRow = rand.nextInt(numRows);
			randCol = rand.nextInt(numCols);
			while(blockArray[randRow][randCol].getBlockType() == Block.Type.WALL){
				randRow = rand.nextInt(numRows);
				randCol = rand.nextInt(numCols);
			}
			blockArray[randRow][randCol].setBlockType(Block.Type.WALL);
			
		}
		
		//Create player
		randRow = rand.nextInt(numRows);
		randCol = rand.nextInt(numCols);
		while(blockArray[randRow][randCol].getBlockType() != Block.Type.OPEN){
			randRow = rand.nextInt(numRows);
			randCol = rand.nextInt(numCols);
		}	
		blockArray[randRow][randCol].setBlockType(Block.Type.PLAYER);
		blocks.add(blockArray[randRow][randCol]);
		blockArray[randRow][randCol].setVisisted(true);
		
		breathSearch.add(blockArray[randRow][randCol]);
		blockArray[randRow][randCol].setVisisted(true);
		
		//Create enemy
		randRow = rand.nextInt(numRows);
		randCol = rand.nextInt(numCols);
		while(blockArray[randRow][randCol].getBlockType() != Block.Type.OPEN){
			randRow = rand.nextInt(numRows);
			randCol = rand.nextInt(numCols);
		}
		blockArray[randRow][randCol].setBlockType(Block.Type.ENEMY);	
	}
	
	public void findNext(){
						
			Block queueHead = breathSearch.poll();
			
			if(queueHead.getBlockType() == Block.Type.ENEMY){
				intruderFound = true;
			}
			
			int xPos = queueHead.getrowPos();
			int yPos = queueHead.getcolPos();

			blocks.add(blockArray[xPos][yPos]);
			/*There are 4 possible moves: up, down, left, right
			 * Since this is not a typical graph, I could brute force check
			 * the surrounding areas. Otherwise, just adding the connecting 
			 * edges is typical.
			*/
			
			//Check above			
			if(yPos - 1 >= 0 && blockArray[xPos][yPos-1].getBlockType() != Block.Type.WALL && !blockArray[xPos][yPos-1].isVisisted()){
				breathSearch.add(blockArray[xPos][yPos-1]);
				blockArray[xPos][yPos-1].setVisisted(true);
			}
			
			//Check right
			if(xPos + 1 < numRows && blockArray[xPos+1][yPos].getBlockType() != Block.Type.WALL && !blockArray[xPos+1][yPos].isVisisted()){
				breathSearch.add(blockArray[xPos+1][yPos]);
				blockArray[xPos+1][yPos].setVisisted(true);
			}
			
			//Check below
			if(yPos + 1 < numCols && blockArray[xPos][yPos+1].getBlockType() != Block.Type.WALL && !blockArray[xPos][yPos+1].isVisisted()){
				breathSearch.add(blockArray[xPos][yPos+1]);
				blockArray[xPos][yPos+1].setVisisted(true);
			}
			
			//Check left
			if(xPos - 1 >= 0 && blockArray[xPos-1][yPos].getBlockType() != Block.Type.WALL && !blockArray[xPos-1][yPos].isVisisted()){
				breathSearch.add(blockArray[xPos-1][yPos]);
				blockArray[xPos-1][yPos].setVisisted(true);
			}
			


			


		
	}
	
	public static void main(String[] args) {

		new GameDriver();

	}
	
	public void paint(Graphics g) {
		g.clearRect(0, 0, width, height);

		//This paints the wall (black) open (white), Enemy (red) and player (blue).
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){					
				if(blockArray[i][j].getBlockType() == Block.Type.PLAYER){
					g.setColor(Color.BLUE);
				}
				else if(blockArray[i][j].getBlockType() == Block.Type.WALL){
					g.setColor(Color.BLACK);
				}
				else if(blockArray[i][j].getBlockType() == Block.Type.ENEMY){
					g.setColor(Color.RED);
				}else{
					g.setColor(Color.WHITE);
				}
				
				g.fillRect(i * rowSize, j * colSize, rowSize, colSize);
				g.setColor(Color.BLACK);
				g.drawRect(i * rowSize, j * colSize, rowSize, colSize);
			}	
		}
		
		//This creates the Cyan dots showing the progression of BFS
		Iterator<Block> it = blocks.iterator();
		while(it.hasNext()){
			Block b = it.next();
			g.setColor(Color.cyan);
			g.fillOval(b.getrowPos()*colSize+colSize/2-15, b.getcolPos()*rowSize+rowSize/2-15, 30, 30);
		}
	}
	
	
}
