//package takethatgui;

/*
 * Take That!
 * by Scott Weiss
 * 
 * This class does most of the work. It displays the labels and grid buttons.
 * It changes the grid state after each move.
 * 
 * It also manages the computer players. You will be rewriting the method
 * makeComputerChoice so it intelligently picks a move based on the current game state.
 * 
 */
import javax.swing.*;

import java.awt.*;
import java.util.Random;

public class Board
    extends JPanel {
  private Player rowP; // the two players
  private Player colP;
  private boolean isRowsTurn; // whose turn is it
  private int currentRow, currentCol; // row and column of last move
  private JLabel[] colLabels; // the labels that indicate which column to play in
  private JLabel[] rowLabels; // the labels to indicate the row to play in
  private Cell[][] cells; // the grid of numbers
  private JLabel messageLabel; // label for turn information, winner
  public Board(int size, Player rp, Player cp, int min, int max, Random rand) {
	  // Constructor
	  // size is grid size (assume square)
	  // rp & cp are players
	  // min and max is range of possible numbers for a square
	  // rand is random number generator
    rowP = rp; // remember player information
    colP = cp;
    isRowsTurn = rand.nextInt()%2 == 0; // determine who moves first
    rowLabels = new JLabel[size]; // make arrays of labels for row/column headings
    colLabels = new JLabel[size];
    cells = new Cell[size][size]; // make 2D array of Cells
    this.setLayout(new GridLayout(size+2, size+1)); // set layout for Board
    // one extra row to accommodate winner message
    // extra row and column for labels
    this.add(new JLabel("",JLabel.CENTER)); // blank label to line things up nicely
    for (int i=0; i < size; i++) // add column headers across the top
    {
    	colLabels[i] = new JLabel(""+i,JLabel.CENTER); // label with column number
    	colLabels[i].setForeground(Color.RED); // make it large and red
    	colLabels[i].setFont(new Font(null, Font.BOLD, 20));
    	colLabels[i].setVisible(false); // no numbers shown yet
    	this.add(colLabels[i]);
    }
    for (int i=0; i < size; i++)
    {
    	// make label for row first
    	rowLabels[i] = new JLabel(""+i, JLabel.CENTER);
    	rowLabels[i].setForeground(Color.RED);
    	rowLabels[i].setFont(new Font(null, Font.BOLD, 20));
    	rowLabels[i].setVisible(false);
    	this.add(rowLabels[i]);
    	// then make cells in that row
    	for (int j=0; j < size; j++)
    	{
    		// create a new Cell object
    		Cell tempCell = new Cell(i,j,this, min, max, rand);
    		cells[i][j] = tempCell;
    		this.add(tempCell); // add to panel
    	}
    }
    if (isRowsTurn) // is it row player's turn to start?
    {
    	// pick a random row
    	currentRow = rand.nextInt(size);
    	// make that row's label visible
    	rowLabels[currentRow].setVisible(true);
    }
    else // do similar for columns
    {
    	currentCol = rand.nextInt(size);
    	colLabels[currentCol].setVisible(true);
    }
    // put text in the message label
    messageLabel = new JLabel("Time to play Take That");
    this.add(messageLabel);
  }

  
  public void nextTurn()
  // This method sets up the next player's turn
  {
	  if (!gameOver()) // if there are still turns to play
	  {
		  if (isRowsTurn) // tell appropriate player to take its turn
			  rowP.takeTurn();
		  else
			  colP.takeTurn();
	  }
  }

  public void makeComputerMove()
  // This performs the move for the computer player
  // After determining what the move should be, it executes that move on the board
  {
	  if (isRowsTurn) // is it row player's turn?
	  {
		  // tell user the computer player is thinking
		   messageLabel.setText(rowP.getName()+" thinking");
		   // determine the player's move
		   int colChoice = makeComputerChoice();
		   // attempt to make the move
		   if (makeMove(currentRow, colChoice, 
				   cells[currentRow][colChoice].getValue()))
			   cells[currentRow][colChoice].select(); // if legal move, change the grid
	  }
	  else // similar for column player
	  {
		   messageLabel.setText(colP.getName()+" thinking");
		   int rowChoice = makeComputerChoice();
		   if (makeMove(rowChoice, currentCol, 
				   cells[rowChoice][currentCol].getValue()))
			   cells[rowChoice][currentCol].select();
		  
	  }
	  nextTurn(); // have the next player go
  }
  public int getPly(){
    if(this.cells.length <=3) return 10;
    else if(this.cells.length == 4) return 9;
    else if(this.cells.length == 5) return 8;
    else if(this.cells.length == 6) return 7;
    else if(this.cells.length == 7) return 6;
    else if(this.cells.length == 8) return 5;
    else if(this.cells.length == 9) return 4;
    else return 3;
  }


/* 
  public int minimax(BoardNode node, int depth, boolean isMaximizingPlayer, int alpha, int beta){
    List<BoardNode> children = node.getAllNodes(isMaximizingPlayer); // call method to get all children and if no children, its at depth 
    if(children.size()==0){
      return node.evaluate(this,getPly()); //call the evaluate method
    }
    if(isMaximizingPlayer){
      int bestVal = Integer.MIN_VALUE;
      for(BoardNode child:children){
        int value = minimax(child, depth+1, isMaximizingPlayer, alpha, beta);
        bestVal = Math.max(value,bestVal);
        alpha = Math.max(alpha, bestVal);
        if(beta<=alpha) break;
      }
      return bestVal;
    }
    else{
      int bestVal = Integer.MAX_VALUE;
      for(BoardNode child:children){
        int value = minimax(child, depth+1, isMaximizingPlayer, alpha, beta);
        bestVal = Math.min(value,bestVal);
        beta = Math.min(beta, bestVal);
        if(beta<=alpha) break;
      }
      return bestVal;
    }
  }
  */
  

  public int[][] getCellValues(){
    int rows = cells.length;
    int cols = cells[0].length;
    int[][] ret = new int[rows][cols];
    for(int r=0;r<rows;r++){
      for(int c=0;c<cols;c++){
        if(cells[r][c].isSelected()){
          ret[r][c] = -100;
        }
        else
        {
          ret[r][c] = cells[r][c].getValue();
        }
      }
    }
    return ret;
  }
  public int makeComputerChoice() {
    BoardNode currentBoardNode = new BoardNode(getCellValues(), rowP.getScore(), colP.getScore(), isRowsTurn, currentRow, currentCol, 0, getPly());
    return currentBoardNode.makeComputerChoice();
}


  public boolean makeMove(int row, int col, int val)
  // Execute move made by player
  // row, col - coordinates of selected square
  // val - number in the square
  // returns true if move is legal, false otherwise
  {
    if (isRowsTurn && row==currentRow) // if row's turn and square is in current row
    {
      rowP.addToScore(val); // update player's score
      rowLabels[currentRow].setVisible(false); // hide previous visible label
      currentCol = col; // update column to that of selected square
      colLabels[currentCol].setForeground(Color.RED); // display header for selected column
      colLabels[currentCol].setVisible(true);
      isRowsTurn = false; // switch to column player's turn
      cells[row][col].select(); // select given square
      if (gameOver()) // if the game is over
      {
        add(new JLabel(rowP.getWinner(colP))); // announce the winner
      }
      return true;
    }
    else if (!isRowsTurn && col==currentCol) // do analogous things for columns
    {
      colP.addToScore(val);
      colLabels[currentCol].setVisible(false);
      currentRow = row;
      rowLabels[currentRow].setForeground(Color.RED);
      rowLabels[currentRow].setVisible(true);
      isRowsTurn = true;
      cells[row][col].select();
      if (gameOver())
        add(new JLabel(rowP.getWinner(colP)));
      return true;
    }
    else
      return false;
  }
  public boolean gameOver()
  // determine if game is over (current player does not have an available square)
  // returns true if game is over, false otherwise
  {
    if (isRowsTurn) // row turn
    {
      for (int i=0; i < cells.length; i++) // search current row
        if (cells[currentRow][i].isSelected() == false) // if some square can be picked
          return false; // game is not over
      return true; // all squares picked - game is over
    }
    else // similar for columns
    {
      for (int j=0; j < cells.length; j++)
        if (cells[j][currentCol].isSelected() == false)
          return false;
      return true;
    }
  }
  
  public void setMessage(String mesg)
  // change the message label
  // mesg - String to display
  {
	  messageLabel.setText(mesg);
  }
}
