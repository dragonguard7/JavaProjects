

public class Block {

	private Type blockType;
	private boolean visisted;
	private int xPos, yPos;
	
	public enum Type{
		OPEN, WALL, PLAYER, ENEMY;
	}
	

	public Block(Type blockType, int x, int y){
		this.blockType = blockType;
		xPos = x;
		yPos = y;
		this.visisted = false;
	}
	
	
//Getters and Setters
	public boolean isVisisted() {
		return visisted;
	}


	public void setVisisted(boolean visisted) {
		this.visisted = visisted;
	}


	public Type getBlockType() {
		return blockType;
	}

	public void setBlockType(Type blockType) {
		this.blockType = blockType;
	}


	public int getxPos() {
		return xPos;
	}


	public int getyPos() {
		return yPos;
	}
	
	
}
