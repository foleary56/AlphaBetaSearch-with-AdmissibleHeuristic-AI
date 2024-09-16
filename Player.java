//package takethatgui;

import javax.swing.*;
import java.awt.*;
/*
 * This class keeps track of player information
 */
public class Player
    extends JPanel {
  private String name; // name for player
  private int score; // score so far
  private boolean isComputer; // is this player a computer?
  private JLabel scoreLabel; // what to display in the gui
  private Board theBoard; // link to board
  public Player(String n, boolean isComp) {
	  // n - name
	  // isComp - is this a computer player
    name = n;
    score = 0; // no points yet
    isComputer = isComp;
    this.setLayout(new GridLayout(1,2)); // make the panel of info
    this.add(new JLabel(name+": ", JLabel.RIGHT));
    scoreLabel = new JLabel(""+score, JLabel.LEFT);
    this.add(scoreLabel);
  }
  public void setBoard(Board b)
  { // set the link to the board
	  theBoard = b;
  }
  public void addToScore(int s)
  {
	  // update player's score
    score += s;
    scoreLabel.setText(""+score); // change gui too
  }
  public boolean isComputer()
  { // accessor
	  return isComputer;
  }
  public int getScore()
  {
	  // accessor for score
	  return score;
  }
  public String getWinner(Player other)
  { // compares players to see who won
    if (this.score > other.score)
      return name+" wins!";
    else if (this.score < other.score)
      return other.name+" wins!";
    else
      return "It's a tie!";
  }
  public void takeTurn()
  { // make this player take a turn
	 if (isComputer)
		 theBoard.makeComputerMove(); // board manages computer players
	 else
		 theBoard.setMessage("Go,"+name); // humans click when they feel like it
  }
}
