
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
		
		int row1 = 0;
		for(int h = 0; h < 9; h++){
			
			int counter = 0;
			if(h % 3 == 0 && h != 0){
				row1++;
			}
			int col = h % 3;
			for(int i = 0; i < 3;i++){
				for(int j = 0; j < 3; j++){
					Boxes tempBox = Driver.blockArray[i+(row1*3)][j+(col*3)];
					int temp[] = tempBox.getPossibleValues();
					tempBox.printPosition();
					tempBox.printPossibleValues();
					/*
					for(int k = 0; k < temp.length; k++){
						
						System.out.print(temp[k] + " ");
					}
					System.out.println("");
					/*
					System.out.print("Checking... "+ Driver.blockArray[i+(row1*3)][j+(col*3)].getValue());
					if(Driver.blockArray[i+(row1 *3)][j+(col * 3)].getValue() == number){
						System.out.println("\nThe number is at pos " + i + " , " + j);
						counter++;
					}
					*/
				}
				
			}
			System.out.println("");
			/*
			
			if(counter == 0){
				System.out.println("There is no " + number + " in the box " + h);
			}else{
				System.out.println("There is a " + number + " in box " + h);
			}
			counter = 0;
			*/
		}
		
		
	}
	
	//Search a given column for a specified value excluding the position
	private void searchColumn(int column, int value, int position){
		
		for(int k = 0; k < Driver.numCols; k++){
			if(k != position && Driver.blockArray[k][column].getValue() == value){
				//shaded.add(blockArray[k][column]);
				//search.add(blockArray[k][column]);
			}									
		}
	}
	
	//Search a given row for a specified value excluding the position
	private void searchRow(int row, int value, int position){

		for(int k = 0; k < Driver.numRows; k++){
			if(k != position && Driver.blockArray[row][k].getValue() == value){
				//shaded.add(blockArray[row][k]);

				//search.add(blockArray[row][k]);
			}	
		}
	}

	protected static void searchNumber(int number){
		
		//Look at each block...
		int row1 = 0;
		for(int h = 0; h < 9; h++){
			
			int counter = 0;
			if(h % 3 == 0 && h != 0){
				row1++;
			}
			int col = h % 3;
			for(int i = 0; i < 3;i++){
				for(int j = 0; j < 3; j++){
					System.out.print("Checking... "+ Driver.blockArray[i+(row1*3)][j+(col*3)].getValue());
					if(Driver.blockArray[i+(row1 *3)][j+(col * 3)].getValue() == number){
						System.out.println("\nThe number is at pos " + i + " , " + j);
						counter++;
					}
				}
				System.out.println("");
			}
			if(counter == 0){
				System.out.println("There is no " + number + " in the box " + h);
			}else{
				System.out.println("There is a " + number + " in box " + h);
			}
			counter = 0;
		}
		
		
		//Look at each row...
		for(int i = 0; i < Driver.numRows;i++){
			for(int j = 0; j < Driver.numCols-2;j++){
				//System.out.println(blockArray[i][j].getValue() + " and " + blockArray[i][j+2].getValue());			
				if(Driver.blockArray[i][j].getValue() == Driver.blockArray[i][j+2].getValue()){

					int row = i;
					int col = j+1;
					int value = Driver.blockArray[i][j+1].getValue();
					
					//searchColumn(col,value,row);
					//searchRow(row,value,col);
					
				}
				
			}
		}
		
		//Look at each column...
		for(int i = 0; i < Driver.numCols;i++){
			for(int j = 0; j < Driver.numRows-2;j++){
				if(Driver.blockArray[j][i].getValue() == Driver.blockArray[j+2][i].getValue()){
					//System.out.println("Match at Row: " + (j+2) + " Col: " + (i+1) + " value: " + blockArray[j+1][i].getValue());								
					int row = j+1;
					int col = i;
					int value = Driver.blockArray[j+1][i].getValue();
					
					//searchColumn(col,value,row);
					//searchRow(row,value,col);
	
				}
			}
		}	
	}
	
}
