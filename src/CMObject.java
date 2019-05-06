
public class CMObject {

	//Data
	private int priority;
	private int row, col;
	private int moveType;
	
	
	//methods
	public void setPriority(int newP) {
		priority = newP;
		
	}
	
	public void setRow(int newR) {
		row = newR;
	}
	
	public void setCol(int newC) {
		col = newC;
	}
	
	public void setMoveType(int newT) {
		moveType = newT;
	}
	
	
	//getter methods
	
	public int getPriority() {
		return priority;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public int getType() {
		return moveType;
	}
	
	
}
