import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameDriver extends JPanel {

	private int numRows = 5, numCols = 5, numWalls = 8;
	private int rowSize = 100, colSize = 100, width, height;
	private Display display;
	private Block blockArray[][];
	private Graphics graphics;
	private static JFrame frame;
	private ArrayList<Block> blocks;
	private boolean intruderFound;
	private BufferStrategy bs;
	private Thread graphicsThread;
	private Queue<Block> breathSearch;
	
	public GameDriver(){
		
		width = numRows * rowSize + 7;
		height = numCols * colSize + 30;
		
		display = new Display(width,height);
		
		frame = display.getFrame();
		frame.add(this);


		breathSearch = new LinkedList<Block>();
		blocks = new ArrayList<Block>();
		
		intruderFound = false;
		//updateDisplay();
		createBlocks();

		render();
		

	}
	
	private void render(){
	System.out.println("In render");
		
	boolean running = true;
	double timePerTick = 1000000000 / 2;
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
				
				traverseMaze();
				repaint();
				
				ticks++;
				delta--;
			}
			
			if(timer >= 1000000000){
				System.out.println("Ticks and Frames: " + ticks);
				ticks = 0;
				timer = 0;
			}
			
		}
		System.out.println("Traverse Ended");
		if(intruderFound){
			JOptionPane.showMessageDialog(frame, "There IS an intruder!!!");
		}else{
			JOptionPane.showMessageDialog(frame, "No intruder detected.");
		}
	
	}
	
	private void createBlocks(){
		blockArray = new Block[numRows][numCols];
		
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){	
				blockArray[i][j] = new Block(Block.Type.OPEN, i, j);
			}
		}
		Random rand = new Random();
		int randX, randY;
		//Create walls
		for(int i = 0; i < numWalls; i++){
			randX = rand.nextInt(numRows);
			randY = rand.nextInt(numCols);

			while(blockArray[randX][randY].getBlockType() == Block.Type.WALL){
				randX = rand.nextInt(numRows);
				randY = rand.nextInt(numCols);
			}
			blockArray[randX][randY].setBlockType(Block.Type.WALL);
			
		}
		//Create player
		randX = rand.nextInt(numRows);
		randY = rand.nextInt(numCols);
		while(blockArray[randX][randY].getBlockType() != Block.Type.OPEN){
			randX = rand.nextInt(numRows);
			randY = rand.nextInt(numCols);
		}
		
		blockArray[randX][randY].setBlockType(Block.Type.PLAYER);
		blocks.add(blockArray[randX][randY]);
		blockArray[randX][randY].setVisisted(true);
		
		breathSearch.add(blockArray[randX][randY]);
		blockArray[randX][randY].setVisisted(true);
		
		//Create enemy
		randX = rand.nextInt(numRows);
		randY = rand.nextInt(numCols);
		while(blockArray[randX][randY].getBlockType() != Block.Type.OPEN){
			randX = rand.nextInt(numRows);
			randY = rand.nextInt(numCols);
		}
		blockArray[randX][randY].setBlockType(Block.Type.ENEMY);
		
		
	}
	
	public void traverseMaze(){
		
		
					
			Block queueHead = breathSearch.poll();
			
			if(queueHead.getBlockType() == Block.Type.ENEMY){
				intruderFound = true;
			}
			
			int xPos = queueHead.getxPos();
			int yPos = queueHead.getyPos();
			System.out.println("Checking " + xPos + " and " + yPos);
			blocks.add(blockArray[xPos][yPos]);
			//There are 4 possible moves: up, down, left, right
			//Check above
			if(yPos - 1 >= 0 && blockArray[xPos][yPos-1].getBlockType() != Block.Type.WALL && !blockArray[xPos][yPos-1].isVisisted()){
				breathSearch.add(blockArray[xPos][yPos-1]);
				blockArray[xPos][yPos-1].setVisisted(true);
			}
			//Check below
			if(yPos + 1 < numRows && blockArray[xPos][yPos+1].getBlockType() != Block.Type.WALL && !blockArray[xPos][yPos+1].isVisisted()){
				breathSearch.add(blockArray[xPos][yPos+1]);
				blockArray[xPos][yPos+1].setVisisted(true);
				
			}
			//Check left
			if(xPos - 1 >= 0 && blockArray[xPos-1][yPos].getBlockType() != Block.Type.WALL && !blockArray[xPos-1][yPos].isVisisted()){
				breathSearch.add(blockArray[xPos-1][yPos]);
				blockArray[xPos-1][yPos].setVisisted(true);
			}
			//Check right
			if(xPos + 1 < numCols && blockArray[xPos+1][yPos].getBlockType() != Block.Type.WALL && !blockArray[xPos+1][yPos].isVisisted()){
				breathSearch.add(blockArray[xPos+1][yPos]);
				blockArray[xPos+1][yPos].setVisisted(true);
			}
		
	}
	
	
	public static void main(String[] args) {

		new GameDriver();

	}

	
	public void paint(Graphics g) {
		System.out.println("Painting...");
		g.clearRect(0, 0, width, height);

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
				//System.out.println("I/J " + i* rowSize + "/" + j * colSize);
	
				g.fillRect(i * rowSize, j * colSize, rowSize, colSize);
				g.setColor(Color.BLACK);
				g.drawRect(i * rowSize, j * colSize, rowSize, colSize);
			}	
		}
		
		Iterator<Block> it = blocks.iterator();
		
		while(it.hasNext()){
			Block b = it.next();

			g.setColor(Color.cyan);
			g.fillOval(b.getxPos()*colSize+colSize/2-15, b.getyPos()*rowSize+rowSize/2-15, 30, 30);
			
		}
		
		
	}
	
	
}
