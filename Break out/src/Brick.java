import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Brick {
	static int width = 30;
	private static int height = 10;
	protected boolean stillAlive;
	int xPos , yPos;
	int playerWidth = 0;
	
	public Brick(int x, int y){
		stillAlive = true;
		xPos = x;
		yPos = y;
		width = 30;
		height = 10;
	}
	
	public Brick(int x, int y, int pWidth){
		stillAlive = true;
		xPos = x;
		yPos = y;
		playerWidth = pWidth;
		height = 10;
	}
	
	public void tick(){
		
	}
	
	public void render(Graphics g){
		if(playerWidth == 0){
			g.setColor(Color.red);	
			g.fillRect(xPos, yPos, width, height);
		}else{
			g.setColor(Color.BLACK);
			g.fillRect(xPos, yPos, playerWidth, height);
		}
	}
	
	public Rectangle getBounds(){
		if(playerWidth == 0){
		return new Rectangle(xPos-10, yPos, width+20, height);
		}
		return new Rectangle(xPos, yPos, playerWidth, height); 
	}
	
}
