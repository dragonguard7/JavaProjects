
public class SearchingAlgorithm {
	
	
	protected static void setNumbers(){
		Driver.boxesArray = new Boxes[Driver.numRows][Driver.numCols];
		for(int i = 0; i < Driver.numRows; i++){
			for(int j = 0; j < Driver.numCols; j++){					
				Driver.boxesArray[i][j] = new Boxes(Utils.parseInt(Driver.tokens[j+i*Driver.numCols]), i, j);
				if(Driver.boxesArray[i][j].getValue() != 0){
					Driver.boxesArray[i][j].setStarter(true);
				}else{
					Driver.boxesArray[i][j].setStarter(false);
				}		
			}

		}
	}
	
	
	protected static void searchPossibleValues(){
		System.out.println("Searching 1-9 for each box");
			
		for(int i = 0; i < Driver.numRows; i++){

			for(int j = 0; j < Driver.numCols;j++){

					Boxes tempBox = Driver.boxesArray[i][j];
					int temp[] = tempBox.getPossibleValues();
						
					if(tempBox.getPossibleValues()[0] > 1){
						searchColumn(tempBox);
						searchRow(tempBox);
						searchBlock(tempBox);
	
					}
					//tempBox.printPosition();
					//tempBox.printPossibleValues();
			}
		}
		
	}//End search values
	
	//Search block
	private static void searchBlock(Boxes box){
		//Upper left column, upper left row, this will be the start position
		int ULC = (box.getcolPos()/3)*3;
		int ULR = (box.getrowPos()/3)*3;
		//box.printPosition();
		//System.out.println("Upper left should be " + ULC + " , " + ULR);
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				//Driver.blockArray[ULR+i][ULC+j].printPosition();
				if(Driver.boxesArray[ULR+i][ULC+j].getValue() != 0 && box.getPossibleValues()[Driver.boxesArray[ULR+i][ULC+j].getValue()] == 0){
					box.removePossibleValue(Driver.boxesArray[ULR+i][ULC+j].getValue());
					Driver.foundValue = true;				
				}
			}
		}
	}
	
	
	//Search a given column for a specified value excluding the position
	private static void searchColumn(Boxes box){
		int column = box.getcolPos();
		
		for(int k = 0; k < Driver.numCols; k++){
			if(Driver.boxesArray[k][column].getValue() != 0 && box.getPossibleValues()[Driver.boxesArray[k][column].getValue()] == 0){
				box.removePossibleValue(Driver.boxesArray[k][column].getValue());
				Driver.foundValue = true;
				
			}									
		}
	}
	
	//Search a given row for a specified value excluding the position
	private static void searchRow(Boxes box){

		for(int k = 0; k < Driver.numRows; k++){
			int row = box.getrowPos();
			if(box.getPossibleValues()[Driver.boxesArray[row][k].getValue()] == 0 && Driver.boxesArray[row][k].getValue() != 0){
				box.removePossibleValue(Driver.boxesArray[row][k].getValue());
				Driver.foundValue = true;
			}	
		}
	}

//************************************ THIS SECTION DOES GROUP ALGORITHMS ************************************
	
	
	protected static void setGroupNumbers(){
		
		//This does block groups
		for(int i = 0; i < 9; i++){
			int ROS = (i/3); //Row offset
			int COS = (i%3); //Column offset
			//These 2 for loop will create the block
			Boxes[] block = new Boxes[9];
			
			for(int j = 0; j < 9; j++){
				block[j] = Driver.boxesArray[(j/3)+(ROS*3)][(j%3)+(COS*3)];
			}
			Driver.blockArray[i] = new Groups(block);

		}
		
		//This does row groups	
		for(int i = 0; i < 9; i++){

			Boxes[] block = new Boxes[9];
		
			for(int j = 0; j < 9; j++){
				block[j] = Driver.boxesArray[i][j];
			}
			Driver.rowArray[i] = new Groups(block);

		}
		
		//This does column groups
		for(int i = 0; i < 9; i++){

			Boxes[] block = new Boxes[9];
		
			for(int j = 0; j < 9; j++){
				block[j] = Driver.boxesArray[j][i];
			}
			Driver.columnArray[i] = new Groups(block);
			
		}	
		
		
	}
	
	protected static void searchGroups(){	
		System.out.println("Searching groups (row,columns,blocks)");
		//Update the group positions then search
		for(int i = 0; i < Driver.blockArray.length; i++){
			Driver.blockArray[i].setPossibleValues();
			Driver.rowArray[i].setPossibleValues();
			Driver.columnArray[i].setPossibleValues();
			searchBlock(Driver.blockArray[i]);

		}
	
		
		
	}
	
	//Search block
	private static void searchBlock(Groups block){
		//Lets find a number we can look at.
		for(int i = 1; i < 10; i++){
			//System.out.println("Looking for " + i);
			
			if(block.getPossibleValues()[i] == 2){
				//System.out.println("The value already exists in the block");
				continue;
			}

				/* We are going to use these to keep track of the spots
				 * we will go through all the boxes in the block and if
				 * the value can go there we will increment counter and
				 * update the row and column. At the end, if there is only
				 * one spot, that is where it will go.
				 */
				
				int counter = 0;
				int row = -1;
				int column = -1;
				//To go through all the boxes
				Boxes[] boxes = block.getGroup();
				for(int j = 0; j < 9; j++){	

					if(Driver.rowArray[boxes[j].getrowPos()].getPossibleValues()[i] == 0 && Driver.columnArray[boxes[j].getcolPos()].getPossibleValues()[i] == 0 && boxes[j].getPossibleValues()[0] != 0){
						row = boxes[j].getrowPos();
						column = boxes[j].getcolPos();
						counter++;
					}
					
				}
				
				if(counter == 1){
					Driver.boxesArray[row][column].setValue(i);
					Driver.foundValue = true;
				}
						
			}

	}
	
	protected static boolean validiateSolution(){

		//Check rows
		for(int i = 0; i < 9;i++){ //row
			for(int j = 0; j < 9; j++){ // column
				
				for(int k = j+1; k < 9; k++){
					//System.out.println("Is: " + Driver.boxesArray[i][j].getValue() +  " equal to " + Driver.boxesArray[i][k].getValue());
					if(Driver.boxesArray[i][j].getValue() == Driver.boxesArray[i][k].getValue()){
						System.out.println("There is a duplicate at: ");
						Driver.boxesArray[i][j].printPosition();
						Driver.boxesArray[i][k].printPosition();
						return false;
					}
				}
			}
		}
		
		//Check columns
		for(int i = 0; i < 9;i++){ //column
			for(int j = 0; j < 9; j++){ //row
				
				for(int k = j+1; k < 9; k++){
					//System.out.println("Is: " + Driver.boxesArray[j][i].getValue() +  " equal to " + Driver.boxesArray[k][i].getValue());
					if(Driver.boxesArray[j][i].getValue() == Driver.boxesArray[k][i].getValue()){
						System.out.println("There is a duplicate at: ");
						Driver.boxesArray[j][i].printPosition();
						Driver.boxesArray[k][i].printPosition();
						return false;
					}
				}
			}
		}
		
		//Check blocks
		for(int i = 0; i < Driver.blockArray.length; i++){
			for(int j = 0; j < Driver.blockArray[i].getGroup().length; j++){
				for(int k = j+1 ; k < 9; k++){
					int value1 = Driver.blockArray[i].getGroup()[j].getValue();
					int value2 = Driver.blockArray[i].getGroup()[k].getValue();
					//System.out.println("Is: " + value1 +  " equal to " + value2);
					if(value1 == value2){
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
//********************************************Checking group numbers **********************************************************

	protected static void checkHiddenNumbers(){
		System.out.println("Checking hidden numbers");
		for(int i = 0; i < Driver.blockArray.length; i++){

			checkGroupNumber(Driver.blockArray[i],i);

		}
	}
	
	private static void checkGroupNumber(Groups group, int blockNumber){
		//Update the group possible values 
		group.setPossibleValues();
		
		//Go through 1-9
		for(int i = 1; i < group.getPossibleValues().length;i++){
			//If its open, lets look at the group for it
			if(group.getPossibleValues()[i] == 0){
				//Go through the group
				for(int j = 0; j < group.getGroup().length;j++){
				
					int boxValue = group.getGroup()[j].getValue(); //0 if boxValue is open
					int possibleValue = group.getGroup()[j].getPossibleValues()[i]; //0 if possible value is open
					
					//Find first open box and open value
					if(boxValue == 0 && possibleValue == 0){
						boolean sameRow = true, sameColumn = true, rowIsValid = false, columnIsValid = false;
						int row = group.getGroup()[j].getrowPos(), column = group.getGroup()[j].getcolPos();
						
						//Lets go through the rest
						for(int k = j+1; k < group.getGroup().length;k++){
							int nextBoxValue = group.getGroup()[k].getValue();
							int nextPossibleValue = group.getGroup()[k].getPossibleValues()[i];
							
							//if the box is open and the value is open
							if(nextBoxValue == 0 && nextPossibleValue == 0){
								if(group.getGroup()[k].getrowPos() != row){ sameRow = false;}else{rowIsValid = true;}
								if(group.getGroup()[k].getcolPos() != column){ sameColumn = false;}else{columnIsValid = true;}
							}
							
							j=k; //Update the position 
						}
						
						if(sameRow && rowIsValid){
							updateRow(row, blockNumber, i);
						}
						if(sameColumn && columnIsValid){
							updateColumn(column, blockNumber, i);
						}
			
					}//End boxValue if
					
				}
				
				
			}
		}

	}
	
	private static void updateRow(int row, int block, int value){
		//Calculate the values we want to omit
		int low = (block%3) * 3;
		int high = (block%3) * 3 + 3;
		for(int i = 0; i < Driver.rowArray.length;i++){ 
			if( i < low || i >= high){
				if(Driver.rowArray[row].getGroup()[i].getPossibleValues()[value] == 0 && Driver.rowArray[row].getGroup()[i].getPossibleValues()[0] != 0){
					Driver.rowArray[row].getGroup()[i].removePossibleValue(value);
					Driver.foundValue = true;
				}
			}
		}
	}
	
	private static void updateColumn(int column, int block, int value){
		int low = (block/3) * 3;
		int high = (block/3) * 3 + 3;
		for(int i = 0; i < Driver.columnArray.length;i++){
			if( !(i >=low && i< high)){
				if(Driver.columnArray[column].getGroup()[i].getPossibleValues()[value] == 0 & Driver.columnArray[column].getGroup()[i].getPossibleValues()[0] != 0){
					Driver.columnArray[column].getGroup()[i].removePossibleValue(value);
					Driver.foundValue = true;
				}
			}
		}
		
	}
	
	
	
}
