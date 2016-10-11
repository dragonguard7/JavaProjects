

public class Block {

	private Type blockType;
	private boolean visisted;
	private int rowPos, colPos;
	
	public enum Type{
		OPEN, WALL, PLAYER, ENEMY;
	}
	

	public Block(Type blockType, int x, int y){
		this.blockType = blockType;
		rowPos = x;
		colPos = y;
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


	public int getrowPos() {
		return rowPos;
	}


	public int getcolPos() {
		return colPos;
	}
	
	
}
