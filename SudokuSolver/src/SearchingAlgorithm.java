
public class SearchingAlgorithm {
	
	
	protected static void setNumbers(){
		Driver.blockArray = new Boxes[Driver.numRows][Driver.numCols];
		for(int i = 0; i < Driver.numRows; i++){
			for(int j = 0; j < Driver.numCols; j++){					
				Driver.blockArray[i][j] = new Boxes(Utils.parseInt(Driver.tokens[j+i*Driver.numCols]), i, j);
				if(Driver.blockArray[i][j].getValue() != 0){
					Driver.blockArray[i][j].setStarter(true);
				}else{
					Driver.blockArray[i][j].setStarter(false);
				}
								
			}

		}
	}
	
	
	protected static void searchPossibleValues(){
			
		for(int i = 0; i < Driver.numRows; i++){

			for(int j = 0; j < Driver.numCols;j++){

					Boxes tempBox = Driver.blockArray[i][j];
					int temp[] = tempBox.getPossibleValues();
						
					if(tempBox.getPossibleValues()[0] > 1){
						searchColumn(tempBox);
						searchRow(tempBox);
						searchBlock(tempBox);
	
					}
					tempBox.printPosition();
					tempBox.printPossibleValues();
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
				if(Driver.blockArray[ULR+i][ULC+j].getValue() != 0 && box.getPossibleValues()[Driver.blockArray[ULR+i][ULC+j].getValue()] == 0){
					box.removePossibleValue(Driver.blockArray[ULR+i][ULC+j].getValue());
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
			if(Driver.blockArray[k][column].getValue() != 0 && box.getPossibleValues()[Driver.blockArray[k][column].getValue()] == 0){
				box.removePossibleValue(Driver.blockArray[k][column].getValue());
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
			if(box.getPossibleValues()[Driver.blockArray[row][k].getValue()] == 0 && Driver.blockArray[row][k].getValue() != 0){
				box.removePossibleValue(Driver.blockArray[row][k].getValue());
				Driver.foundValue = true;
				if(box.getPossibleValues()[0] == 1){
					box.completeBox();
				}
			}	
		}
	}
	
}
