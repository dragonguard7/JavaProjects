import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

public class BrickManager {

	private Display gameDisplay;
	ArrayList<Brick> bricks;
	
	public BrickManager(Display gameDisplay){
		this.gameDisplay = gameDisplay;
		bricks = new ArrayList<Brick>();
		generateBricks();
	}
	
	private void generateBricks(){
		int brickSpace = 6;
		int numBrickRow = 2;
		int numBrickCol = gameDisplay.getWidth() / (Brick.width + brickSpace);
		int brickY = 30, brickX = (gameDisplay.getWidth() - numBrickCol * (Brick.width + brickSpace))/2;
		
		for(int j = 0; j < numBrickRow; j++){	
			for(int i = 0; i < numBrickCol; i++){
				bricks.add(new Brick(brickX,brickY));
				brickX += Brick.width + brickSpace;
			}
			brickY += 20;
			brickX = (gameDisplay.getWidth() - numBrickCol * (Brick.width + brickSpace))/2;

		}
		
		System.out.println(numBrickCol + " " + (gameDisplay.getWidth() - numBrickCol * (Brick.width +2)));
		/*
		bricks.add(new Brick(30,gameDisplay.getHeight()/2));
		bricks.add(new Brick(gameDisplay.getWidth()/2,30));
		bricks.add(new Brick(gameDisplay.getWidth()-50,gameDisplay.getHeight()/2));
		bricks.add(new Brick(gameDisplay.getWidth()/2,gameDisplay.getHeight()-20));
		*/
		System.out.println("Generated bricks");
	}
	
	public void tick(){
		Iterator<Brick> it = bricks.iterator();
		while(it.hasNext()){
		Brick b = it.next();
		b.tick();
		if(!b.stillAlive){
			it.remove();
		}
	}
	}
	public void render(Graphics g){
		for(Brick b : bricks){
			b.render(g);

		}
	}
	
}
