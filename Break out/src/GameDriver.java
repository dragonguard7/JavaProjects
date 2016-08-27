import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;


public class GameDriver implements Runnable, KeyListener, ActionListener{
	private static GameDriver game;
	private Display gameDisplay;
	private Ball gameBall;
	private BrickManager brickManager;
	private Thread thread;
	private boolean running = false;
	private int FPS = 60;
	private Graphics g;
	private float gameSpeed = 1;
	private Brick player;
	//0 - Menu, 1 - Game, 2 - Ending
	private int stateID = 0;


	
	public GameDriver(){
		gameDisplay = new Display();
		brickManager = new BrickManager(gameDisplay);
		//startGame();
		gameDisplay.startMenu(this);
	}
	
	public static void main(String[] args) {
		game = new GameDriver();
	}
	
	public synchronized void start(){
		if(running){
			return;
		}
		running = true;
		thread = new Thread(this);	//Put the game class on the thread then calls run
		thread.start();
	}

	@Override
	public void run() {
		double timePerTick = 1000000000 / FPS;
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
				tick();
				render();
				ticks++;
				delta--;
			}
			
			if(timer >= 1000000000){
				//System.out.println("Ticks and Frames: " + ticks);
				FPS = ticks;
				ticks = 0;
				timer = 0;
			}
		}
		
	}
	
	
	private void startGame(){
		start();
		Random rand = new Random();
		gameDisplay.getFrame().requestFocus();
		gameBall = new Ball(gameDisplay, (float)(rand.nextInt(3 - 1 + 1) + 1)* gameSpeed, -(float)(rand.nextInt(6-3+1)+ 3)* gameSpeed);
		player = new Brick(gameDisplay.getWidth()/2, gameDisplay.getHeight()-50, 60);
		brickManager.generateBricks();
		
		g = gameDisplay.getFrame().getGraphics();
	}
	
	private void render(){
		g.clearRect(0, 0, gameDisplay.getWidth(), gameDisplay.getHeight());
		gameBall.render(g);
		brickManager.render(g);
		player.render(g);
		
		if(brickManager.bricks.isEmpty()){
			running = false;
			g.clearRect(0, 0, gameDisplay.getWidth(), gameDisplay.getHeight());
			g.drawString("Congratulations you win!", gameDisplay.getWidth()/3, gameDisplay.getHeight()/2);

		}
	}
	
	private void tick(){
		gameBall.tick();	
		brickManager.tick();
		checkCollisions();
	}
	
	private void checkCollisions(){
		for(Brick b : brickManager.bricks){
			if(collides(gameBall.getBounds(),b.getBounds())){
				//System.out.println("Hit a brick.");
				b.stillAlive = false;
				gameBall.bounce();
				break;
			}
		}
		if(collides(gameBall.getBounds(),player.getBounds())){
			gameBall.bounce();
		}
	}
	
	private boolean collides(Rectangle ball, Rectangle brick){
		if(ball.intersects(brick)){
			return true;
		}
		return false;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			player.xPos += 35;
			if(player.xPos + player.playerWidth > gameDisplay.getWidth()){
				player.xPos = gameDisplay.getWidth() - player.playerWidth;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			player.xPos -= 35;
			if(player.xPos < 0){
				player.xPos = 0;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public Display getGameDisplay() {
		return gameDisplay;
	}

	public void setGameDisplay(Display gameDisplay) {
		this.gameDisplay = gameDisplay;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object o = e.getSource();
		if(o == gameDisplay.start){
			if(!gameDisplay.numRows.getText().equals("")){
				int numRows = Integer.parseInt(gameDisplay.numRows.getText());
				if(numRows < 6 && numRows > 0){
					System.out.println("Correct input. Value: " + numRows);
					brickManager.setNumBrickRow(numRows);
				}else{
					System.out.println("Not null but not correct input. Default 3");
					brickManager.setNumBrickRow(3);
				}
			}else{
				System.out.println("It's null. Default 3");
				brickManager.setNumBrickRow(3);
			}

			gameDisplay.getFrame().removeAll();
			gameDisplay.getFrame().revalidate();
			startGame();
		}
		
		
	}
	
	
}
