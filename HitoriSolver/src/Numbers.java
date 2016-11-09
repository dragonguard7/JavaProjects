
public class Numbers {

	private Type blockType;
	private boolean visisted;
	private int value, rowPos, colPos;
	
	public enum Type{
		UNKNOWN, CIRCLE, SHADE;
	}
	

	public Numbers(Type blockType, int value, int row, int col){
		this.blockType = blockType;
		this.value = value;
		rowPos = row;
		colPos = col;
		this.visisted = false;
	}
	
	
//Getters and Setters
	public boolean isVisisted() {
		return visisted;
	}


	public int getValue() {
		return value;
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
