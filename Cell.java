//package takethatgui;

import javax.swing.*;
import java.awt.event.*;
import java.util.Random;
/*
 * This class represents one square on the Take That! board.
 */
public class Cell
    extends JButton {
  private int value; // number in the cell
  private boolean selected; // have we picked it already?
  private int row; // coordinates of the cell on the board
  private int col;
  private Board brd; // the Board object it is part of
  public Cell(int r, int c, Board b, int min, int max, Random rand) {
	  // r & c - coordinates of the cell
	  // b - Board it is a part of
	  // min & max - range for this cell's randomly chosen value
	  // rand - the random number generator we'll use to pick the value
    super(); // do Button stuff
    selected = false; // not picked yet
    brd = b;
    row = r;
    col = c;
    value=rand.nextInt(max-min+1)+min;
    this.setText(""+value); // display number in cell
    this.addActionListener(new CellListener()); // set up to be clicked
    this.setVisible(true);
  }
  public boolean isSelected() { return selected; } // accessors
  public int getValue() { return value; }
  public void select() // mark as selected; change the text too
  {
    selected = true;
    setText("***");
  }

  public Cell clone() {
    Cell clonedCell = new Cell(this.row, this.col, this.brd, 0, 0, new Random());
    clonedCell.selected = this.selected;
    clonedCell.value = this.value;
    return clonedCell;
  }

  private class CellListener implements ActionListener
  {
    public void actionPerformed(ActionEvent event)
    {
    	// what to do when a cell is clicked
      if (selected == false) // only matters if not picked yet
      {
    	  // report coordinates and value to board
    	  // see if the move is legal
        boolean result = brd.makeMove(row,col,value);
        if (result) // if it is legal
        {
          selected = true; // select it
          setText("***");
        }
        brd.nextTurn(); // tell board current player should take a turn
      }
    }
  }

}
