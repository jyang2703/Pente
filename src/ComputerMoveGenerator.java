import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ComputerMoveGenerator {
	
	public static final int OFFENSE = -1;
	public static final int DEFENSE = 1;
	
	public static final int ONE_IN_ROW_DEF = 1;
	public static final int TWO_IN_ROW_DEF = 2;
	public static final int TWO_IN_ROW_OPEN = 3;
	public static final int TWO_IN_ROW_CAP = 4;
	public static final int THREE_IN_ROW_DEF = 3;
	public static final int THREE_IN_ROW_OPEN = 7;
	public static final int THREE_IN_ROW_CAP = 5;
	public static final int EVERY_OTHER_THREE = 7;
	public static final int FOUR_IN_ROW_DEF = 4;
	public static final int FOUR_IN_ROW_OPEN = 2;
	public static final int FOUR_IN_ROW_CAP = 6;
	
	//Offense
	public static final int ONE_IN_ROW_OFF = 1; 
	public static final int TWO_IN_ROW_OFF = 2; 
	public static final int MAKE_THREE_ROW = 4; 
	public static final int MAKE_FOUR_ROW = 8; 
	public static final int FOUR_IN_ROW_OFF_OPEN = 9; 
	public static final int FOUR_IN_ROW_OFF_CAP = 8;
	public static final int FOUR_IN_ROW_OFF_GAP = 8;
	public static final int BLOCK_CAPTURES = 7;
	
	
	
	PenteGameBoard myGame; 
	int myStone;
	
	ArrayList<CMObject> allMoves = new ArrayList<CMObject>();
	
	public ComputerMoveGenerator(PenteGameBoard gb, int stoneColor) {
		
		myStone = stoneColor;
		myGame = gb;
		
		System.out.println("Computer playing as " + myStone);
		
	}
	
	public void sortDefPriorities() {
		
		
		Collections.sort(allMoves);
		
	}
	
	public void sortOffPriorities() {
		
		
		Collections.sort(allMoves);
		
	}
	

	public int[] getComputerMove () {
		
		int[] newMove = new int[2];
		
		newMove[0] = -1;
		newMove[1] = -1;
		
		allMoves.clear();
				
		findDefMoves();
		sortDefPriorities();
		
		System.out.println("First Def move is " + allMoves.get(0));
		System.out.println("Last Def move is " + allMoves.get(allMoves.size()-1)); 
		
		findOffMoves();
		sortOffPriorities();
		System.out.println("First Off move is " + allMoves.get(0));
		System.out.println("Last Off move is " + allMoves.get(allMoves.size()-1)); 
		
		//System.out.println("Here are all the moves in the list");
		//this.printMoveList();
		//System.out.println();
		
		int dHighestPriority = 0;
		if(allMoves.size() > 0) {
			
			int whichOne = (int)(Math.random() * allMoves.size());
			CMObject ourMove = allMoves.get(0);
			newMove[0] = ourMove.getRow();
			newMove[1] = ourMove.getCol();
			dHighestPriority = ourMove.getPriority();
			
		} else {
			newMove = generateRandomMove();
		}
		
		if(allMoves.size() > 0) {
		//check the off moves, if the first has higher priority, do off
		
		if(allMoves.size() > 0) {			
		CMObject ourMove = allMoves.get(0);			
		if(ourMove.getPriority() > dHighestPriority) {
		newMove[0] = ourMove.getRow();
		newMove[1] = ourMove.getCol();
				
}
			
			else {
				if(myStone == PenteGameBoard.BLACKSTONE && myGame.getDarkStoneMove2Taken() == false) {
					
					//1. set up variables - know how big the inner boxes are
					int newBStoneProbRow = -1;
					int newBStoneProbCol = -1;
					int innerSafeSquareSideLen = PenteGameBoard.INNER_END - PenteGameBoard.INNER_START + 1;
					
					//2. ensures that move is outside inner box, but inside the "safe" area
					
					while(myGame.getDarkStoneMove2Taken() == false) {
						
						newBStoneProbRow = (int)(Math.random() * (innerSafeSquareSideLen + 2) ) + 
								(innerSafeSquareSideLen + 1);
						newBStoneProbCol = (int)(Math.random() * (innerSafeSquareSideLen + 2)) + 
								(innerSafeSquareSideLen + 1);
						
						myGame.darkSquareProblem(newBStoneProbRow, newBStoneProbCol);
						
					}
					
					newMove[0] = newBStoneProbRow;
					newMove[1] = newBStoneProbCol;
					}
				}
			}
		} 
		
	
		
		
		
		try {
			sleepForAMove();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		return newMove;
	}
	
	
	

	
	public void printMoveList() {
		for(CMObject m: allMoves) {
			System.out.println(m);
		}
	}

	
	
	
	
	

	
	public void findDefMoves() {
		for(int row = 0; row < PenteGameBoard.NUM_SQUARES_SIDE; row++) {
			for(int col = 0; col < PenteGameBoard.NUM_SQUARES_SIDE; col++) {
					if(myGame.getBoard()[row][col].getState() == myStone * -1) {
					
						
					findFourDef(row, col);
					findThreeHack(row, col);
					findThreeDef(row, col);
					findTwoDef(row, col);
					findOneDef(row, col);

					
					
			}
		}		
	}
}
	
	public boolean isOnBoard(int r, int c) { //this method ensures that there will not be an ArrayOutOfBounds Exception (makes sure the square is on the board)
		
		boolean isOn = false; 
		
		if(r >= 0 && r < PenteGameBoard.NUM_SQUARES_SIDE) {
			if(c >= 0 && c < PenteGameBoard.NUM_SQUARES_SIDE) {
				isOn = true;
			}
		}
		
		return isOn;
	}
	
	public void setMove(int r, int c, int p, int type) {
		
		if(myStone == PenteGameBoard.BLACKSTONE && myGame.getDarkStoneMove2Taken() == false) {
			
			if(myGame.darkSquareProblemComputerMoveList(r, c) == false) {
				CMObject newMove = new CMObject();
				newMove.setRow(r);
				newMove.setCol(c);
				newMove.setPriority(p);
				newMove.setMoveType(type);
				allMoves.add(newMove);
			} else {
				System.out.println("Dark Square Problem");
			}
		} else {
			CMObject newMove = new CMObject();
			newMove.setRow(r);
			newMove.setCol(c);
			newMove.setPriority(p);
			newMove.setMoveType(type);
			allMoves.add(newMove);
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
						allMoves.add(newMove);
					}
					
				} catch (ArrayIndexOutOfBoundsException e) {
					
				}
			}
		}

		
		
		
		
	}

	public void findTwoDef(int r, int c) {
		
		for(int rL = -1; rL <= 1; rL++) {
			for(int uD = -1; uD <= 1; uD++) {
			try {
				if(myGame.getBoard()[r + rL][c + uD].getState() == myStone * -1 ) {
					if(myGame.getBoard()[r + (rL *2)][c + (uD*2)].getState() == PenteGameBoard.EMPTY ) {
							if(isOnBoard(r-rL, c-uD) == false) {
			                                    setMove(
			                                            r + (rL* 2), 
			                                            c + (uD * 2),
			                                            TWO_IN_ROW_DEF, DEFENSE);
			                                    
			                                    
			                                } else if (myGame.getBoard()[r - rL][c -uD].getState() == PenteGameBoard.EMPTY) {
			                                    setMove(r + (rL* 2), c + (uD * 2), this.TWO_IN_ROW_OPEN, DEFENSE );
			                                } else if (myGame.getBoard()[r - rL][c -uD].getState() == myStone){
			                                    setMove(
			                                            r + (rL* 2), 
			                                            c + (uD * 2),
			                                            this.TWO_IN_ROW_CAP, DEFENSE);
			                                }
			
			                            }
			                        }
			                    } catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
			                    }
			                }
			            }
			    }

	
	
public void findThreeDef(int r, int c) {
	for(int rL = -1; rL <= 1; rL++) {
		for(int uD = -1; uD <= 1; uD++) {
		try {
			if(myGame.getBoard()[r + rL][c + uD].getState() == myStone * -1 ) {
				if(myGame.getBoard()[r + (rL*2)][c + (uD*2)].getState() == myStone * -1 ) {
					if(myGame.getBoard()[r + (rL *3)][c + (uD*3)].getState() == PenteGameBoard.EMPTY ) {
						if(isOnBoard(r-rL, c-uD) == false) {
							setMove(r + (rL* 3),c + (uD * 3), THREE_IN_ROW_DEF, DEFENSE );
						} else if (myGame.getBoard()[r - rL][c - uD].getState() == PenteGameBoard.EMPTY) {
								setMove(r + (rL*3), c + (uD * 3), this.THREE_IN_ROW_OPEN, DEFENSE );
                         
                         
						} else if (
								myGame.getBoard()[r - rL][c -uD].getState() == myStone){
								setMove(r + (rL* 3), c + (uD * 3), this.THREE_IN_ROW_CAP, DEFENSE);
						} 
						
					}
				}
			}
		}
             catch (ArrayIndexOutOfBoundsException e) {

            }
        }
    }

				
}

public void findThreeHack(int r, int c) {
	for(int rL = -1; rL <= 1; rL++) {
		for(int uD = -1; uD <= 1; uD++) {
		try {
			if(myGame.getBoard()[r][c].getState() == myStone * -1 ) {
				if(myGame.getBoard()[r + (rL*2)][c + (uD*2)].getState() == myStone * -1 ) {
					if(myGame.getBoard()[r + (rL*4)][c + (uD*4)].getState() == myStone * -1 ) {
						if (myGame.getBoard()[r + (rL)][c + (uD)].getState() == PenteGameBoard.EMPTY) {
							setMove(r + (rL), c + (uD), this.EVERY_OTHER_THREE, DEFENSE);
						} else if (myGame.getBoard()[r + (rL*3)][c + (uD*3)].getState() == PenteGameBoard.EMPTY) {
							setMove(r + (rL*3), c + (uD * 3), this.EVERY_OTHER_THREE, DEFENSE);
						}
					}
				}
			}
			
			
			} catch (ArrayIndexOutOfBoundsException e) {
				
			}
		}
	}	
}
public void findFourDef(int r, int c) {
	for(int rL = -1; rL <= 1; rL++) {
		for(int uD = -1; uD <= 1; uD++) {
		try {
			if(myGame.getBoard()[r + rL][c + uD].getState() == myStone * -1 ) {
				if(myGame.getBoard()[r + (rL*2)][c + (uD*2)].getState() == myStone * -1 ) {
						if(myGame.getBoard()[r + (rL*3)][c + (uD*3)].getState() == myStone * -1 ) {
						if(myGame.getBoard()[r + (rL *4)][c + (uD*4)].getState() == PenteGameBoard.EMPTY ) {
							if(isOnBoard(r-rL, c-uD) == false) {
							setMove(r + (rL* 4),c + (uD * 4), FOUR_IN_ROW_DEF, DEFENSE );
						} else if (myGame.getBoard()[r - rL][c - uD].getState() == PenteGameBoard.EMPTY) {
								setMove(r + (rL*3), c + (uD * 3), this.FOUR_IN_ROW_OPEN, DEFENSE );
						} else if (
								myGame.getBoard()[r - rL][c -uD].getState() == myStone){
								setMove(r + (rL* 4), c + (uD * 4), this.FOUR_IN_ROW_CAP, DEFENSE);
						}
					}
				}
			}
		}
	}
             catch (ArrayIndexOutOfBoundsException e) {
            }
        }
    }

				
}

public void findOffMoves() {
	for(int row = 0; row < PenteGameBoard.NUM_SQUARES_SIDE; row++) {
		for(int col = 0; col < PenteGameBoard.NUM_SQUARES_SIDE; col++) {
				if(myGame.getBoard()[row][col].getState() == PenteGameBoard.EMPTY) {
				
				if(setCheckFour(row, col)) {
					return;
				}
				
				makeFourOff(row, col);
				blockCaptures(row, col);
				//makeThreeOff(row, col);
				findOneOff(row, col);
				
				

				
				
		}
	}		
}
}



			
//	public void setMove(int r, int c, int p) {
//		CMObject newMove = new CMObject();
//		newMove.setRow(r);
//		newMove.setCol(c);
//		newMove.setPriority(p);
//		newMove.setMoveType(OFFENSE);
//		allMoves.add(newMove);
//	}
	
	public void findOneOff(int r, int c) {
		for(int rL = -1; rL <= 1; rL++) {
			for(int uD = -1; uD <=1; uD++) {
				try {
					
				if(myGame.getBoard()[r][c].getState() == PenteGameBoard.EMPTY) {
						CMObject newMove = new CMObject();
						newMove.setRow(r);
						newMove.setCol(c);
						newMove.setPriority(ONE_IN_ROW_OFF);
						
						newMove.setMoveType(OFFENSE);
						allMoves.add(newMove);
				}
					
			} catch (ArrayIndexOutOfBoundsException e) {
					
				}
			}
		}
	}

	
	public void findFourOff(int r, int c) { 
		for(int rL = -1; rL <= 1; rL++) {
			for(int uD = -1; uD <= 1; uD++) {
			try {
				
			if(myGame.getBoard()[r][c].getState() == myStone) {
				if(myGame.getBoard()[r + rL][c + uD].getState() == myStone ) {
					if(myGame.getBoard()[r + (rL*2)][c + (uD*2)].getState() == myStone ) {
							if(myGame.getBoard()[r + (rL*3)][c + (uD*3)].getState() == myStone ) {
							if(myGame.getBoard()[r + (rL *4)][c + (uD*4)].getState() == PenteGameBoard.EMPTY ) {
								if(isOnBoard(r-rL, c-uD) == false) {
								setMove(r + (rL* 5),c + (uD * 5), FOUR_IN_ROW_OFF_OPEN, OFFENSE);
								} 
							}
						}
					}
				}
			}
		}
		
			
	             catch (ArrayIndexOutOfBoundsException e) {
	            }
			}
	    }
	}

	
	public void makeFourOff(int r, int c) {
		for(int rL = -1; rL <= 1; rL++) {
			for(int uD = -1; uD <= 1; uD++) {
				try {
					
					if(myGame.getBoard()[r][c].getState() == PenteGameBoard.EMPTY) {
						if(myGame.getBoard()[r+rL][c+uD].getState() == myStone) {
							if(myGame.getBoard()[r+(rL*2)][c+(uD*2)].getState() == myStone) {
								if(myGame.getBoard()[r+(rL*3)][c+(uD*3)].getState() == myStone) {
									if(myGame.getBoard()[r+(rL*4)][c+(uD*4)].getState() == PenteGameBoard.EMPTY) {
											
												setMove(r + (rL* 4),c + (uD * 4), MAKE_FOUR_ROW, OFFENSE);
										
									}
									
								} 
							} 
						}
					}
					
					if(myGame.getBoard()[r][c].getState() == PenteGameBoard.EMPTY) {
						if(myGame.getBoard()[r+rL][c+uD].getState() == myStone) {
							if(myGame.getBoard()[r+(rL*2)][c+(uD*2)].getState() == myStone) {
								if(myGame.getBoard()[r+(rL*3)][c+(uD*3)].getState() == PenteGameBoard.EMPTY) {
									if(myGame.getBoard()[r+(rL*4)][c+(uD*4)].getState() == myStone) {
											
												setMove(r + (rL* 3),c + (uD * 3), MAKE_FOUR_ROW, OFFENSE);
										 
									}
									
								} 
							} 
						}
					}
					
					if(myGame.getBoard()[r][c].getState() == PenteGameBoard.EMPTY) {
						if(myGame.getBoard()[r+rL][c+uD].getState() == myStone) {
							if(myGame.getBoard()[r+(rL*2)][c+(uD*2)].getState() == PenteGameBoard.EMPTY) {
								if(myGame.getBoard()[r+(rL*3)][c+(uD*3)].getState() == myStone) {
									if(myGame.getBoard()[r+(rL*4)][c+(uD*4)].getState() == myStone) {
											
												setMove(r + (rL* 2),c + (uD * 2), MAKE_FOUR_ROW, OFFENSE);
										 
									}
									
								} 
							} 
						}
					}
					
					if(myGame.getBoard()[r][c].getState() == PenteGameBoard.EMPTY) {
						if(myGame.getBoard()[r+rL][c+uD].getState() == PenteGameBoard.EMPTY) {
							if(myGame.getBoard()[r+(rL*2)][c+(uD*2)].getState() == myStone) {
								if(myGame.getBoard()[r+(rL*3)][c+(uD*3)].getState() == myStone) {
									if(myGame.getBoard()[r+(rL*4)][c+(uD*4)].getState() == myStone) {
											
												setMove(r + rL,c + uD, MAKE_FOUR_ROW, OFFENSE);
										 
									}
									
								} 
							} 
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					
				}
				
				
				
				
			}
	}
}
	
	public void makeThreeOff(int r, int c) {
		for(int rL = -1; rL <= 1; rL++) {
			for(int uD = -1; uD <= 1; uD++) {
				try {
				if(myGame.getBoard()[r][c].getState() == PenteGameBoard.EMPTY) {
					if(myGame.getBoard()[r+rL][c+uD].getState() == myStone) {
						if(myGame.getBoard()[r+(rL*2)][c+(uD*2)].getState() == myStone) {
							if(myGame.getBoard()[r+(rL*3)][c+(rL*3)].getState() == PenteGameBoard.EMPTY) {
								setMove(r + (rL*3),c + (uD*3), MAKE_THREE_ROW, OFFENSE);
							}
						}
					}
				}
				
				if(myGame.getBoard()[r][c].getState() == PenteGameBoard.EMPTY) {
					if(myGame.getBoard()[r+rL][c+uD].getState() == PenteGameBoard.EMPTY) {
						if(myGame.getBoard()[r+(rL*2)][c+(uD*2)].getState() == myStone) {
							if(myGame.getBoard()[r+(rL*3)][c+(rL*3)].getState() == myStone) {
								setMove(r + rL,c + uD, MAKE_THREE_ROW, OFFENSE);
							}
						}
					}
				}
				
				if(myGame.getBoard()[r][c].getState() == PenteGameBoard.EMPTY) {
					if(myGame.getBoard()[r+rL][c+uD].getState() == myStone) {
						if(myGame.getBoard()[r+(rL*2)][c+(uD*2)].getState() == PenteGameBoard.EMPTY) {
							if(myGame.getBoard()[r+(rL*3)][c+(rL*3)].getState() == myStone) {
								setMove(r+ (rL*2), c+(uD*2), MAKE_THREE_ROW, OFFENSE);
							}
						}
					}
				}
				
				
				
				
			} catch (ArrayIndexOutOfBoundsException e) {
				
			}
				
			}
		}
	}
	
	public void blockCaptures(int r, int c) {
		for(int rL = -1; rL <= 1; rL++) {
			for(int uD = -1; uD <= 1; uD++) {
				if(myGame.getBoard()[r][c].getState() == myStone * -1) {
					if(myGame.getBoard()[r+rL][c+uD].getState() == myStone) {
						if(myGame.getBoard()[r+(rL*2)][c+(uD*2)].getState() == myStone) {
							if(myGame.getBoard()[r+(rL*3)][c+(uD*3)].getState() == PenteGameBoard.EMPTY) {
						
								setMove(r + (rL*3), c+(uD*3), BLOCK_CAPTURES, OFFENSE);
							}
						}
					}
				}
			}
		}
	}
	public boolean setCheckFour(int r, int c) {
		for(int rL = -1; rL <= 1; rL++) {
			for(int uD = -1; uD <= 1; uD++) {
				if(myGame.getBoard()[r][c].getState() == PenteGameBoard.EMPTY) {
					
					if(checkComputerFive(r, c, myStone, uD, rL) == true) {
						setMove(r, c, FOUR_IN_ROW_OFF_OPEN, OFFENSE);
						return true;
					}
					
				}
			}
		}
		
		return false;
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
	

	public boolean checkComputerFive(int r, int c, int pt, int upDown, int rightLeft) {
		try {
			
			boolean five = false;
			
			if(!(upDown == 0 && rightLeft == 0)) {
		
			
				if(myGame.getBoard()[r][c].getState() == PenteGameBoard.EMPTY) {
				if(myGame.getBoard()[r+upDown][c+rightLeft].getState() == pt) {
					if(myGame.getBoard()[r+(upDown*2)][c+(rightLeft*2)].getState() == pt) {
						if(myGame.getBoard()[r+(upDown*3)][c+(rightLeft*3)].getState() == pt) {
							if(myGame.getBoard()[r+(upDown*4)][c+(rightLeft*4)].getState() == pt) {
								
									five = true;
									
									
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
	
	
}