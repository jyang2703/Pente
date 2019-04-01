import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class PenteBoardSquare {
	
	//Data
	
	private int xLoc, yLoc;
	private int sWidth, sHeight;
	
	private int sState;
	
	private Color sColor; //square color
	private Color lColor; //line color
	private Color bColor; //border color
	private Color innerC;
	boolean isInner = false;
	
	private Color darkStoneColor = new Color(4, 9, 64);
	private Color darkStoneTop = new Color(63, 69, 158);
	private Color darkStoneHighlight = new Color(188, 199, 255);
	
	private Color whiteStoneColor = new Color(224, 222, 204);
	private Color shadowGrey = new Color(169, 173, 142);
	private Color whiteStoneTop = new Color(250, 250, 250);
	
	//Constructor
	public PenteBoardSquare (int x, int y, int w, int h) {
		xLoc = x;
		yLoc = y;
		sWidth = w;
		sHeight = h;
		
		innerC = new Color (255, 238, 134);
		sColor = new Color(255, 219, 104);
		bColor = new Color(0, 0, 0);
		lColor = new Color(255, 245, 214);
		
		
		sState = PenteGameBoard.EMPTY;
		
		
	}
	
public void setInner() {
	isInner = true;
}
	
public void drawMe(Graphics g) {
	
	if(isInner) {
		g.setColor(innerC);
		
	} else {
		g.setColor(sColor);
	}
	
	//Rectangle fill color
	g.fillRect(xLoc, yLoc, sWidth, sHeight);
	
	//Border
	g.setColor(bColor);
	g.drawRect(xLoc, yLoc, sWidth, sHeight);
	
	if(sState != PenteGameBoard.EMPTY) {
		g.setColor(shadowGrey);
		g.fillOval(xLoc,  yLoc+ 6, sWidth-8, sHeight-8);
	}
	//set line color
	
	g.setColor(lColor);
	
	//Horizontal
	g.drawLine(xLoc, yLoc + sHeight/2, xLoc + sWidth, yLoc + sHeight/2);
	
	//Vertical
	g.drawLine(xLoc + sWidth/2, yLoc, xLoc + sWidth/2, yLoc + sHeight);
	
	
	
	
	if(sState == PenteGameBoard.BLACKSTONE) {
		g.setColor(darkStoneColor);
		g.fillOval(xLoc + 4, yLoc + 4, sWidth - 8, sHeight - 8);
		
		g.setColor(darkStoneTop);
		g.fillOval(xLoc + 8, yLoc + 6, sWidth - 12, sHeight - 8);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(3));
		
		
		g2.setColor(darkStoneHighlight);
		
		g2.setStroke(new BasicStroke(1));
		
		//g.fillOval(xLoc + (int)(sWidth*0.55), yLoc +10, (int)(sWidth * 0.15), (int)(sHeight*0.25));
		g2.drawArc(xLoc + (int)(sWidth*0.4), 
				yLoc +10, 
				(int)(sWidth * 0.3), 
				(int)(sHeight*0.35), 
				0, 
				90);
				
	}
	
	
	if(sState == PenteGameBoard.WHITESTONE) {
		g.setColor(whiteStoneColor);
		g.fillOval(xLoc + 4, yLoc + 4, sWidth - 8, sHeight - 8);
		
		
		g.setColor(whiteStoneTop);
		g.fillOval(xLoc + 8, yLoc + 6, sWidth - 12, sHeight - 10);
		
	}
	
	}
//methods
	public void setState(int newState) {
		if(newState < -1 || newState > 1) {
			System.out.println(newState + "is an illegal. State must be between - 1 and 1");
		} else {
			sState = newState;
		}
	}


}
