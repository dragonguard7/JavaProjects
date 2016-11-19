
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
					if(box.getPossibleValues()[0] == 1){
						box.completeBox();
					}
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
				if(box.getPossibleValues()[0] == 1){
					box.completeBox();
				}
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
				if(box.getPossibleValues()[0] == 1){
					box.completeBox();
				}
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
			//System.out.println("Group: " + i + " Row OS: " + ROS + " Col OS " + COS);
			
			for(int j = 0; j < 9; j++){
				block[j] = Driver.boxesArray[(j/3)+(ROS*3)][(j%3)+(COS*3)];
			}
			Driver.blockArray[i] = new Groups(block);
			//Driver.blockArray[i].printGroup();
			//Driver.blockArray[i].printPossibleValues();
		}
		
		//This does row groups	
		for(int i = 0; i < 9; i++){

			Boxes[] block = new Boxes[9];
		
			for(int j = 0; j < 9; j++){
				block[j] = Driver.boxesArray[i][j];
			}
			Driver.rowArray[i] = new Groups(block);
			//Driver.rowArray[i].printGroup();
			//Driver.rowArray[i].printPossibleValues();
		}
		
		//This does column groups
		for(int i = 0; i < 9; i++){

			Boxes[] block = new Boxes[9];
		
			for(int j = 0; j < 9; j++){
				block[j] = Driver.boxesArray[j][i];
			}
			Driver.columnArray[i] = new Groups(block);
			//Driver.columnArray[i].printGroup();
			//Driver.columnArray[i].printPossibleValues();
		}	
		
		
	}
	
	protected static void searchGroups(){
		//searchBlock(Driver.blockArray[1]);
		for(int i = 0; i < Driver.blockArray.length; i++){
			//System.out.println("Checking block: " + i );
			searchBlock(Driver.blockArray[i]);
			searchBlock(Driver.rowArray[i]);
			searchBlock(Driver.columnArray[i]);
		}
		
		
		
	}
	
	//Search block
	private static void searchBlock(Groups block){
		//Lets find a number we can look at.
		for(int i = 1; i < 10; i++){

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
					//If something can go there and the value isnt in row or column
					//boxes[j].printPosition();
					//boxes[j].printPossibleValues();
					//System.out.print(boxes[j].getPossibleValues()[i]);
					if(boxes[j].getValue() == i){
						counter = 0;
						break;
					}
					if(boxes[j].getPossibleValues()[i] == 0 && boxes[j].getPossibleValues()[0] != 0){
						//System.out.print(i + " can go ");
						//boxes[j].printPosition();
						row = boxes[j].getrowPos();
						column = boxes[j].getcolPos();
						counter++;
						//System.out.println("");
					}
					
				}
				
				if(counter == 1){
					//System.out.print("There is only 1 possibility for " + i+ " at: " );
					//Driver.boxesArray[row][column].printPosition();
					//Driver.boxesArray[row][column].printPossibleValues();
					//System.out.println("");
					Driver.boxesArray[row][column].setValue(i);
					Driver.foundValue = true;
				}
						
			}

	}
	
	protected static boolean validiateSolution(){
		//Check blocks
		
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
	
}
