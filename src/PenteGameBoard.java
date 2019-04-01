import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PenteGameBoard extends JPanel{
	
	public static final int EMPTY = 0;
	public static final int BLACKSTONE = 1;
	public static final int WHITESTONE = -1;
	public static final int NUM_SQUARES_SIDE = 19;
	public static final int INNER_START = 7;
	public static final int INNER_END = 11;
	public static final int PLAYER1_TURN = 1;
	public static final int PLAYER2_TURN = -1;
	
	private int bWidth, bHeight;
	
	private PenteBoardSquare testSquare;
	private int squareW, squareH;
	private JFrame myFrame;
	
	private PenteBoardSquare [] [] gameBoard;
	
	//variables for game
	
	int playerTurn; 
	boolean player1IsComputer = false;
	boolean player2IsComputer = false;
	String p1Name, p2Name;
	
	
	
	public PenteGameBoard (int w, int h) {
		
		bWidth = w;
		bHeight = h;
		
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
				
				/*if((row+col)%2 == 0) {
					gameBoard[row][col].setState(BLACKSTONE);
				
				} else {
					gameBoard[row][col].setState(WHITESTONE);
				}
			*/	
			}
			
		}
		
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
			}
		}
	}
	
	public void startNewGame() {
		resetBoard();
		
		p1Name = JOptionPane.showInputDialog("Enter Player One Name or type 'c' for Computer");
		if(p1Name.equals('c') || p1Name.equals("computer") || p1Name.equals("comp")) {
			player1IsComputer = true;
		}
		
		
		p2Name = JOptionPane.showInputDialog("Enter Player One Name or type 'c' for Computer");
		if(p2Name.equals('c') || p2Name.equals("computer") || p2Name.equals("comp")){
			player2IsComputer = true;
		}
		
		playerTurn = this.PLAYER1_TURN;
		this.gameBoard[NUM_SQUARES_SIDE/2][NUM_SQUARES_SIDE/2].setState(BLACKSTONE);
		changePlayerTurn();
		
		
		this.repaint();
	}
	
	public void changePlayerTurn() {
		playerTurn *= -1;
	}
	
	
	
	
}
