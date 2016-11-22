
public class Groups {

	private Boxes[] group;
	private int possibleValues[];
	
	Groups(Boxes[] group){
		this.group = group;
		possibleValues = new int[10];
		setPossibleValues();
	}

	public void setPossibleValues(){
		possibleValues[0] = 9;
		for(int i = 0; i < group.length;i++){
			if(group[i].getValue() != 0){
			possibleValues[group[i].getValue()] = 2;
			possibleValues[0] -= 1;
			}		
		}
	}
	
	public Boxes[] getGroup() {
		return group;
	}

	public void setGroup(Boxes[] group) {
		this.group = group;
	}

	public int[] getPossibleValues() {
		return possibleValues;
	}

	
	public void removePossibleValue(int removeValue) {
		this.possibleValues[removeValue] = 2;
		this.possibleValues[0] -= 1;
	}
	
	public void printGroup(){
		for(int i = 0; i < group.length; i++){
			System.out.print("( " + group[i].getcolPos() + " , " + group[i].getrowPos()+ " ) : " + group[i].getValue() + " ");
		}
		System.out.println("");
	}
	
	public void printPossibleValues(){
		System.out.print("Group possible values: ");
		for(int k = 0; k < possibleValues.length; k++){
			
			System.out.print(possibleValues[k] + " ");
		}
		System.out.println("");
	}
	
}
