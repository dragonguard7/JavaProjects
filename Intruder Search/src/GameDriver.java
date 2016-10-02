import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameDriver extends JPanel {

	private int numRows = 5, numCols = 5, numWalls = 7;
	private int rowSize = 100, colSize = 100, width, height;
	private Display display;
	private Block blockArray[][];
	private Graphics g;
	private static JFrame frame;
	private ArrayList<Block> blocks;
	private boolean intruderFound;
	
	public GameDriver(){
		
		width = numRows * rowSize + 7;
		height = numCols * colSize + 30;
		
		
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);


		blocks = new ArrayList<Block>();
		intruderFound = false;
		//updateDisplay();
		createBlocks();
		traverseMaze();
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

		Iterator<Block> it = blocks.iterator();
		Block b = it.next();
		
		Queue<Block> breathSearch = new LinkedList<Block>();
		breathSearch.add(b);
		
		
		while(breathSearch.peek() != null){
			
			
			Block queueHead = breathSearch.poll();
			
			if(queueHead.getBlockType() == Block.Type.ENEMY){
				intruderFound = true;
				break;
			}
			
			int xPos = queueHead.getxPos();
			int yPos = queueHead.getyPos();
			System.out.println("Checking " + xPos + " and " + yPos);
			//There are 4 possible moves: up, down, left, right
			//Check above
			if(yPos - 1 >= 0 && blockArray[xPos][yPos-1].getBlockType() != Block.Type.WALL && !blockArray[xPos][yPos-1].isVisisted()){
				blocks.add(blockArray[xPos][yPos-1]);
				breathSearch.add(blockArray[xPos][yPos-1]);
			}
			//Check below
			if(yPos + 1 < numRows && blockArray[xPos][yPos+1].getBlockType() != Block.Type.WALL && !blockArray[xPos][yPos+1].isVisisted()){
				blocks.add(blockArray[xPos][yPos+1]);
				breathSearch.add(blockArray[xPos][yPos+1]);
			}
			//Check left
			if(xPos - 1 >= 0 && blockArray[xPos-1][yPos].getBlockType() != Block.Type.WALL && !blockArray[xPos-1][yPos].isVisisted()){
				blocks.add(blockArray[xPos-1][yPos]);
				breathSearch.add(blockArray[xPos-1][yPos]);
			}
			//Check right
			if(xPos + 1 < numCols && blockArray[xPos+1][yPos].getBlockType() != Block.Type.WALL && !blockArray[xPos+1][yPos].isVisisted()){
				blocks.add(blockArray[xPos+1][yPos]);
				breathSearch.add(blockArray[xPos+1][yPos]);
			}
			queueHead.setVisisted(true);
		}

		repaint();
		
	}
	
	
	public static void main(String[] args) {
		frame = new JFrame("Maze Runner");
		frame.add(new GameDriver());
	}

	
	public void paint(Graphics g) {
		
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
		
		if(intruderFound){
			g.setColor(Color.orange);
			
			g.drawString("There is an intruder!!", width/2, height/2);
		}else{
			g.setColor(Color.pink);
			
			g.drawString("There is NO intruder!!", width/2, height/2);
		}
		
		
	}
	
	
}
