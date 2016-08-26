import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Ball {

	private float x, y, radius, xSpeed, ySpeed;
	private Display gameDisplay;
	
	public Ball(Display gameDisplay, float xSpeed, float ySpeed){
		this.gameDisplay = gameDisplay;
		radius = 20f;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		x = gameDisplay.getWidth()/2;
		y = gameDisplay.getHeight()/2;
		
	}
	
	public void tick(){
		x += xSpeed;
		y += ySpeed;
		if(x < 0){
			xSpeed = -xSpeed;
			x = 0;
		}else if(x + radius > gameDisplay.getWidth()){
			xSpeed = -xSpeed;
			x = gameDisplay.getWidth() - radius;
		}
		if(y < 0){
			ySpeed = -ySpeed;
			y = 0;
		}else if(y + radius > gameDisplay.getHeight()){
			ySpeed = -ySpeed;
			y = gameDisplay.getHeight() - radius;
		}
	}
		
	
	public void render(Graphics g){
		g.setColor(Color.blue);
		//g.fillArc((int)x, (int)y, (int)radius, (int)radius, 0, 360);
		g.fillOval((int)x, (int)y, (int)radius, (int)radius);
		g.drawRect((int)(x), (int)(y), (int)(radius), (int)(radius));
	}
	
	public Rectangle getBounds(){
		return new Rectangle((int)(x), (int)(y), (int)(radius), (int)(radius));
	}
	public void bounce(){
		ySpeed = -ySpeed;
		//xSpeed = -xSpeed;
	}
}
