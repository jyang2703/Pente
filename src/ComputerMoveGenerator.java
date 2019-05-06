import java.util.ArrayList;

public class ComputerMoveGenerator {
	
	public static final int OFFENSE = -1;
	public static final int DEFENSE = 1;
	public static final int ONE_IN_ROW_DEF = 1;
	
	PenteGameBoard myGame; 
	int myStone;
	
	ArrayList<CMObject> oMoves = new ArrayList<CMObject>();
	ArrayList<CMObject> dMoves = new ArrayList<CMObject>();
	
	public ComputerMoveGenerator(PenteGameBoard gb, int stoneColor) {
		
		myStone = stoneColor;
		myGame = gb;
		
		System.out.println("Computer playing as " + myStone);
		
	}
	
	
	public int[] getComputerMove () {
		
		int[] newMove = new int[2];
		newMove[0] = -1;
		newMove[1] = -1;
		dMoves.clear();
		oMoves.clear();
		
		findDefMoves();
		findOffMoves();
		
		if(dMoves.size() > 0) {
			
			int whichOne = (int)(Math.random() * dMoves.size());
			CMObject ourMove = dMoves.get(whichOne);
			newMove[0] = ourMove.getRow();
			newMove[1] = ourMove.getCol();
			
		} else {
			newMove = generateRandomMove();
		}
		
		try {
			sleepForAMove();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return newMove;
	}
	
	public void findDefMoves() {
		for(int row = 0; row < PenteGameBoard.NUM_SQUARES_SIDE; row++) {
			for(int col = 0; col < PenteGameBoard.NUM_SQUARES_SIDE; col++) {
					if(myGame.getBoard()[row][col].getState() == myStone * -1) {
						
					
					findOneDef(row, col);
			}
		}		
	}
}
	
	
	public void findOneDef(int r, int c) {
		
		for(int rL = -1; rL <= 1; rL++) {
			for(int uD = -1; uD <=1; uD++) {
				try {
					
					if(myGame.getBoard()[r+rL][c+uD].getState() == PenteGameBoard.EMPTY) {
						CMObject newMove = new CMObject();
						newMove.setRow(r + rL);
						newMove.setCol(c + uD);
						newMove.setPriority(ONE_IN_ROW_DEF);
						
						newMove.setMoveType(DEFENSE);
						dMoves.add(newMove);
					}
					
				} catch (ArrayIndexOutOfBoundsException e) {
					
				}
			}
		}
		
		
		
	}
			
	public void findOffMoves() {
		
	}
	
	public int[] generateRandomMove() {
		int[] move = new int[2]; //will have row and col
		
		boolean done = false;
		
		int newR, newC;
		do {
			newR = (int)(Math.random() * PenteGameBoard.NUM_SQUARES_SIDE);
			newC = (int)(Math.random() * PenteGameBoard.NUM_SQUARES_SIDE);
			
			if(myGame.getBoard()[newR][newC].getState() == PenteGameBoard.EMPTY) {
				done = true;
				move[0] = newR;
				move[1] = newC;
				
			}
		} while(!done);
		
		return move;
		
	}
	
	public void sleepForAMove() throws InterruptedException {
		
		
		Thread currThread = Thread.currentThread();
		Thread.sleep(PenteGameBoard.SLEEP_TIME);
	}
	
}
