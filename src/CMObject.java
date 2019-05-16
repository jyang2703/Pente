
public class CMObject implements Comparable<Object>{

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
	public Integer getPriorityInt() {
		return new Integer(priority);
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
	
	public String toString() {
		
		return "Move at: " + row + ", " + col + ", priority: " + priority;
	}

	@Override
	public int compareTo(Object o) {
		int comparePriority=((CMObject)o).getPriority();
		return comparePriority-this.priority;
		
	}
	
	
	
}
