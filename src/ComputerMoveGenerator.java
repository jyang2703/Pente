
public class ComputerMoveGenerator {
	
	PenteGameBoard myGame; 
	int myStone;
	
	
	
	public ComputerMoveGenerator(PenteGameBoard gb, int stoneColor) {
		
		myStone = stoneColor;
		myGame = gb;
		
		System.out.println("Computer playing as " + myStone);
		
	}
	
	public int[] getComputerMove () {
		int[] newMove = generateRandomMove();
		
		
		
		try {
			sleepForAMove();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return newMove;
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
