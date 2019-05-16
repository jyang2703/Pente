import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PenteGameBoard extends JPanel implements MouseListener {
	
	public static final int EMPTY = 0;
	public static final int BLACKSTONE = 1;
	public static final int WHITESTONE = -1;
	public static final int NUM_SQUARES_SIDE = 19;
	public static final int INNER_START = 7;
	public static final int INNER_END = 11;
	public static final int PLAYER1_TURN = 1;
	public static final int PLAYER2_TURN = -1;
	public static final int MAX_CAPTURES = 15;
	public static final int SLEEP_TIME = 500;
	
	private int bWidth, bHeight;
	
	private PenteBoardSquare testSquare;
	private int squareW, squareH;

	
	private PenteBoardSquare [] [] gameBoard;
	private PenteScore myScoreBoard;
	
	//variables for game
	
	private int playerTurn; 
	private boolean player1IsComputer = false;
	private boolean player2IsComputer = false;
	private String p1Name, p2Name;
	private int p1Captures, p2Captures;
	private boolean darkStoneMove2Taken = false;
	private boolean gameOver = false;
	
	//Computer Game Players
	
	private ComputerMoveGenerator p1ComputerPlayer = null;
	private ComputerMoveGenerator p2ComputerPlayer = null;
	
	
	public PenteGameBoard (int w, int h, PenteScore sb) {
		
		bWidth = w;
		bHeight = h;
		myScoreBoard = sb;
		
		p1Captures = 0;
		p2Captures = 0;
		
		this.setSize(w,h);;
		this.setBackground(Color.GREEN);
		
		squareW = bWidth/this.NUM_SQUARES_SIDE;
		squareH = bHeight/this.NUM_SQUARES_SIDE;
		
		gameBoard = new PenteBoardSquare[NUM_SQUARES_SIDE][NUM_SQUARES_SIDE];
		
		//testSquare = new PenteBoardSquare(0, 0, squareW, squareH);
		
		for(int row = 0; row < NUM_SQUARES_SIDE; row++) {
			
			for(int col = 0; col < NUM_SQUARES_SIDE; col++) {
				
				gameBoard[row][col] = new PenteBoardSquare(col*squareW , row*squareH, squareW, squareH);
				if(col >= INNER_START && col <= INNER_END) {
					if(row >= INNER_START && row <= INNER_END) {
					gameBoard[row][col].setInner();
					}
				}
				
			}
			
		}
		initialPente();
		repaint();
		this.addMouseListener(this);
		this.setFocusable(true);
		
	}

	
	//method to do drawing...
	
	//override
	
	public void paintComponent(Graphics g) {
		
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, bWidth, bHeight);
		
		//do this 19 x 19 times
		//testSquare.drawMe(g);
		for(int row = 0; row < NUM_SQUARES_SIDE; row++) {
			for(int col = 0; col < NUM_SQUARES_SIDE; col++) {
				gameBoard[row][col] .drawMe(g);
		}
		
		}
	}
	
	
	public void resetBoard() {
		for(int row = 0; row < NUM_SQUARES_SIDE; row++) {
			for(int col = 0; col < NUM_SQUARES_SIDE; col++) {
				gameBoard[row][col].setState(EMPTY);
				gameBoard[row][col].setWinningSquare(false);
			}
		}
		repaint();
	}
	
	public void startNewGame(boolean firstGame) {
		
		//reset captures
		
		p1Captures = 0;
		p2Captures = 0;
		gameOver = false;
		
		
			
	        
	        if(firstGame) {
	        		p1Name = JOptionPane.showInputDialog("Name of player 1 (or type 'c' for computer)");
	        		if(p1Name != null && (p1Name.toLowerCase().equals("c") || p1Name.toLowerCase().equals("computer") ||p1Name.toLowerCase().equals("comp"))) {
	        			player1IsComputer = true;
	        		p1ComputerPlayer = new ComputerMoveGenerator(this, BLACKSTONE);
	        		}
	        	
	        	}
			
	        myScoreBoard.setName(p1Name, BLACKSTONE);
	        myScoreBoard.setCaptures(p1Captures, BLACKSTONE);
			
	        if(firstGame) {
			
			p2Name = JOptionPane.showInputDialog("Enter Player Two Name or type 'c' for Computer");
			if(p2Name != null && (p2Name.toLowerCase().equals("c") || p2Name.toLowerCase().equals("computer") || p2Name.toLowerCase().equals("comp"))){
				player2IsComputer = true;
				p2ComputerPlayer = new ComputerMoveGenerator(this, WHITESTONE);
				}
	        }
			
			myScoreBoard.setName(p2Name, WHITESTONE);
			myScoreBoard.setCaptures(p2Captures, WHITESTONE);
			
			resetBoard();  
		
		
		
		playerTurn = PLAYER1_TURN;
		
		//set center as dark
		this.gameBoard[NUM_SQUARES_SIDE/2][NUM_SQUARES_SIDE/2].setState(BLACKSTONE);
		
		darkStoneMove2Taken = false;
		changePlayerTurn();
		
		checkForComputerMove(playerTurn);
		
		
		this.repaint();

}
	
	

	public void changePlayerTurn() {
		playerTurn *= -1;
		myScoreBoard.setPlayerTurn(playerTurn);
	}
	
	
	public boolean fiveInARow(int whichPlayer) {
		
		boolean isFive = false;
		
		for(int row = 0; row < NUM_SQUARES_SIDE; row++) {
			for(int col = 0; col < NUM_SQUARES_SIDE; col++) {
				for(int rl = -1; rl<= 1; rl++) {
					for(int uD = -1; uD <= 1; uD++) {
						if(rl == uD && rl==0) {continue;}
						if(fiveCheck(row, col, whichPlayer, rl, uD)) {
							isFive = true;
						}
					}
				}
			}
		}
		
		return isFive;
		
		
		
	}
	
	public boolean fiveCheck (int r, int c, int pt, int upDown, int rightLeft) {
		
		try {
			
		boolean five = false;
		
		if(!(upDown == 0 && rightLeft == 0)) {
	
		
			if(gameBoard[r][c].getState() == pt) {
			if(gameBoard[r+upDown][c+rightLeft].getState() == pt) {
				if(gameBoard[r+(upDown*2)][c+(rightLeft*2)].getState() == pt) {
					if(gameBoard[r+(upDown*3)][c+(rightLeft*3)].getState() == pt) {
						if(gameBoard[r+(upDown*4)][c+(rightLeft*4)].getState() == pt) {
							
								five = true;
								gameBoard[r][c].setWinningSquare(true);
								gameBoard[r+upDown][c+rightLeft].setWinningSquare(true);
								gameBoard[r+(upDown*2)][c+(rightLeft*2)].setWinningSquare(true);
								gameBoard[r+(upDown*3)][c+(rightLeft*3)].setWinningSquare(true);
								gameBoard[r+(upDown*4)][c+(rightLeft*4)].setWinningSquare(true);
								
								}
						}
					}
				}
	
			}
		}
	
		return five;
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		
		
	}
	public void checkForWin(int whichPlayer) {
		
		
		if(whichPlayer == this.PLAYER1_TURN) {
            if(this.p1Captures >= MAX_CAPTURES) {
                
                JOptionPane.showMessageDialog(null, "Congratulations: " + p1Name +  " wins!!" +
                        "\n with " + p1Captures + " captures");
                gameOver = true;
                
            } else {
                if(fiveInARow(whichPlayer)) {
                    System.out.println("Back from  fiveInARow(); for P1 and its true");
                    JOptionPane.showMessageDialog(null, "Congratulations: " 
                            + p1Name + " wins with 5 in a row");
                    gameOver = true;
                } 
            }
        } else {  //for player 2
            if(this.p2Captures >= MAX_CAPTURES) {
                //we win!!
                JOptionPane.showMessageDialog(null, "Congratulations: " + p2Name +  " wins " +
                        "\n with " + p2Captures + " captures");
                gameOver = true;  
            } else {
                if(fiveInARow(whichPlayer)) {
                    System.out.println("Back from  fiveInARow(); for P2 and its true");
                    JOptionPane.showMessageDialog(null, "Congratulations: " 
                            + p2Name + " wins with 5 in a row");
                    gameOver = true;
                }
            } 
        }
	}

	
	 
public void checkClick(int clickX, int clickY) {
	
	if(!gameOver) {
	
	for(int row = 0; row < NUM_SQUARES_SIDE; row++) {
		for(int col = 0; col < NUM_SQUARES_SIDE; col++) {
			
			boolean squareClicked = gameBoard[row][col].isClicked(clickX, clickY);
			if(squareClicked) {
				//System.out.println("Clicked square at [" + row + ", " + col + "]");
				if(gameBoard[row][col].getState() == EMPTY) {
					if(!darkSquareProblem(row, col)) {
					gameBoard[row][col].setState(playerTurn);
					this.paintImmediately(0, 0, bWidth, bHeight);
					checkForCaptures(row, col, playerTurn);
					this.repaint();
					
					//this.paintImmediately(0, 0, bWidth, bHeight);
					
					checkForWin(playerTurn);
					this.changePlayerTurn();
					checkForComputerMove(playerTurn);
					
					} else {
						JOptionPane.showMessageDialog(null, "Second move must be outside of the yellow square");
					}
				} else {
					JOptionPane.showMessageDialog(null, "This square is occupied, click on another square");
					}
				}
			}
		}	
	}
}

public void checkForComputerMove(int whichPlayer) {
	
	if(whichPlayer == this.PLAYER1_TURN && this.player1IsComputer == true) {
		int[] nextMove = this.p1ComputerPlayer.getComputerMove();
		int newR = nextMove[0];
		int newC = nextMove[1];
		gameBoard[newR][newC].setState(playerTurn);
		this.paintImmediately(0, 0, bWidth, bHeight);
		//this.repaint();
		checkForCaptures(newR, newC, playerTurn);
		this.repaint();
		checkForWin(playerTurn);
		
		if(!gameOver) {
			this.changePlayerTurn();
			checkForComputerMove(playerTurn);
		}
		
	} else if (whichPlayer == this.PLAYER2_TURN && this.player2IsComputer == true) {
		int[] nextMove = this.p2ComputerPlayer.getComputerMove();
		
		int newR = nextMove[0];
		int newC = nextMove[1];
		gameBoard[newR][newC].setState(playerTurn);
		this.paintImmediately(0, 0, bWidth, bHeight);
		checkForCaptures(newR, newC, playerTurn);
		checkForWin(playerTurn);
		
		if(!gameOver) {
			this.changePlayerTurn();
			checkForComputerMove(playerTurn);
		}
	}
	this.repaint();
}

public boolean darkSquareProblem(int r, int c) {
	
	boolean dsp = false;
	
	if((!darkStoneMove2Taken) && (playerTurn == BLACKSTONE))
	{
		
			if ( (r >= INNER_START && r <= INNER_END) && (
					c >= INNER_START && c <= INNER_END)) {
					dsp = true;
				
			} else {
				darkStoneMove2Taken = true;
			}
	}
	
	return dsp;
}

public boolean darkSquareProblemComputerMoveList(int r, int c) {
	
	boolean dsp = false;
	
	if((!darkStoneMove2Taken) && (playerTurn == BLACKSTONE))
	{
		
			if ( (r >= INNER_START && r <= INNER_END) && (
					c >= INNER_START && c <= INNER_END)) {
					dsp = true;
				
			} else {
				
			}
	}
	
	return dsp;
}

	public void checkForCaptures(int r, int c, int pt) { //Method to check for captures
		
	boolean didCapture;
	
	//Horizontal checks
	for(int rl = -1; rl<= 1; rl++) {
		for(int uD = -1; uD <= 1; uD++) {
			didCapture = checkForCaptures(r, c, pt, rl, uD);
		}
	}
		
	}
	
	public boolean checkForCaptures(int r, int c, int pt, int upDown, int rightLeft) {
		try {
		
		boolean cap = false;
		
		if(gameBoard[r+upDown][c+rightLeft].getState() == pt * -1) {
			if(gameBoard[r+(upDown*2)][c+(rightLeft*2)].getState() == pt * -1) {
				if(gameBoard[r+(upDown*3)][c+rightLeft*3].getState() == pt) {
					
						//System.out.println("Right side capture");
						gameBoard[r+ upDown][c+rightLeft].setState(EMPTY);
						gameBoard[r+(upDown*2)][c+(rightLeft*2)].setState(EMPTY);
						cap = true;
						if(pt == this.PLAYER1_TURN) {
							p1Captures++;
							myScoreBoard.setCaptures(p1Captures, playerTurn);
							
						} else {
							p2Captures++;
							myScoreBoard.setCaptures(p2Captures, playerTurn);
							
						}
					}
				}
			}
		
		return cap;
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		
		
}

	

		
		
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		this.checkClick((e.getX()), e.getY());
		
	
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void initialPente() {
		gameBoard[5][5].setState(BLACKSTONE);
		gameBoard[5][6].setState(BLACKSTONE);
		gameBoard[5][7].setState(BLACKSTONE);
		gameBoard[5][8].setState(BLACKSTONE);
		gameBoard[5][9].setState(BLACKSTONE);
		gameBoard[5][10].setState(BLACKSTONE);
		gameBoard[5][11].setState(BLACKSTONE);
		gameBoard[5][12].setState(BLACKSTONE);
		gameBoard[5][13].setState(BLACKSTONE);
		
		gameBoard[6][9].setState(WHITESTONE);
		gameBoard[7][9].setState(WHITESTONE);
		gameBoard[8][9].setState(WHITESTONE);
		gameBoard[9][9].setState(WHITESTONE);
		gameBoard[10][9].setState(WHITESTONE);
		gameBoard[11][9].setState(WHITESTONE);
		
		gameBoard[11][5].setState(BLACKSTONE);
		gameBoard[11][6].setState(BLACKSTONE);
		gameBoard[11][7].setState(BLACKSTONE);
		gameBoard[11][8].setState(BLACKSTONE);
		gameBoard[11][9].setState(BLACKSTONE);
		gameBoard[11][10].setState(BLACKSTONE);
		gameBoard[11][11].setState(BLACKSTONE);
		gameBoard[11][12].setState(BLACKSTONE);
		gameBoard[11][13].setState(BLACKSTONE);
		
		gameBoard[8][5].setState(WHITESTONE);
		gameBoard[8][6].setState(WHITESTONE);
		gameBoard[8][7].setState(WHITESTONE);
		gameBoard[8][8].setState(WHITESTONE);
		gameBoard[8][9].setState(WHITESTONE);
		gameBoard[8][10].setState(WHITESTONE);
		gameBoard[8][11].setState(WHITESTONE);
		
		gameBoard[8][11].setState(WHITESTONE);
		gameBoard[9][11].setState(WHITESTONE);
		gameBoard[10][11].setState(WHITESTONE);
		
		
		
		
		
		
	}
	
	public PenteBoardSquare[] [] getBoard() {
		return gameBoard;
	}
	public boolean getDarkStoneMove2Taken() {
		return darkStoneMove2Taken;
	}
	
	
}

