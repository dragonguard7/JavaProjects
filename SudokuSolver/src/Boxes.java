public class Boxes {

	private boolean starter;
	private int value, rowPos, colPos;
	/*This is to keep track of possible values, the first
	 * value will be the number possible
	*/
	private int possibleValues[];

	public Boxes(int value, int row, int col){
		this.value = value;
		rowPos = row;
		colPos = col;
		possibleValues = new int[10];
		if(value == 0){
			possibleValues[0] = 9;
		}else{
			possibleValues[0] = 0;
			possibleValues[value] = 1;
		}
	}
	
	
//Getters and Setters

	public int getValue() {
		return value;
	}
	
	public void completeBox(int value){
		this.possibleValues[0] = 0;
		this.value = value;
	}

	public void setStarter(boolean starter) {
		this.starter = starter;
	}


	public boolean isStarter() {
		return starter;
	}


	public int getrowPos() {
		return rowPos;
	}


	public int getcolPos() {
		return colPos;
	}


	public int[] getPossibleValues() {
		return possibleValues;
	}


	public void removePossibleValue(int removeValue) {
		this.possibleValues[removeValue] = 1;
		this.possibleValues[0] -= 1;
	}
	
	public void printPosition(){
		System.out.print("( " + colPos + " , " + rowPos + " ) ");
	}
	
	public void printPossibleValues(){
		for(int k = 0; k < possibleValues.length; k++){
			
			System.out.print(possibleValues[k] + " ");
		}
		System.out.println("");
	}
}
