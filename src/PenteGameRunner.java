import javax.swing.JFrame;

public class PenteGameRunner {
	
	public static void main(String[] args) {
		
		int gWidth = 703;
		int gHeight = 703;
		
		JFrame theGame = new JFrame("Play Pente");
		theGame.setSize(gWidth, gHeight);
		theGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		PenteGameBoard gb = new PenteGameBoard(gWidth, gHeight);
		theGame.add(gb);
		
		theGame.setVisible(true);
		
		gb.startNewGame();
		
		
	}

}
