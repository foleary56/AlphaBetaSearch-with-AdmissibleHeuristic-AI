//package takethatgui;

/*
 * Take That!
 * by Scott Weiss
 * GUI version allowing human or computer players
 * 
 */
import javax.swing.*;
import java.awt.*;
import java.util.Random;
public class Game
    extends JFrame {
	// This class represents the window where the game is played
	// It sets up the components and initiates play
	// Most of the work is done by the Board class
  Player rowPlayer; // the two players
  Player colPlayer;
  Board brd; // the grid where the numbers appear
  Container contents; // the section of the window where information is displayed
  public Game() {
	  // Constructor
    super("Take That!");
    contents = this.getContentPane();
    Random rand = getGenerator(); // make a random number generator
    // we need to do this to ensure that two different programs can use the same
    // board for the tournament
    
    // get input data about the game
    String rowName = JOptionPane.showInputDialog("Row player, please enter your name");
    String rowComp = JOptionPane.showInputDialog("Is "+rowName+" a human (yes/no)");
    String colName = JOptionPane.showInputDialog("Column player, please enter your name");
    String colComp = JOptionPane.showInputDialog("Is "+colName+" a human (yes/no)");
    String sizeStr = JOptionPane.showInputDialog("Enter size of board");
    int size = Integer.parseInt(sizeStr);
    rowPlayer = new Player(rowName, rowComp.equals("no")); // create Player objects
    colPlayer = new Player(colName, colComp.equals("no"));
    brd = new Board(size, rowPlayer, colPlayer, -25, 75, rand); // create the Board
    rowPlayer.setBoard(brd); // make sure Players can call Board methods
    colPlayer.setBoard(brd);
    contents.setLayout(new GridLayout(3,1)); // set up layout of window
    contents.add(brd); // add all the components
    contents.add(rowPlayer);
    contents.add(colPlayer);
    setSize(50*size+400,50*size+100); // make sure window is big enough
    setVisible(true); // display the window
    brd.nextTurn(); // start the game
  }
  
  private Random getGenerator()
  // This method creates the Random object that decides on the numbers in the grid
  // and the first player
  {
	  Random r = new Random();
	  long seed; // this will control the generated numbers
	  
	  // first determine if the seed will be entered by the user or generated
	  // by the program
	  String pickSeedStr = JOptionPane.showInputDialog("Do I pick the seed? (yes/no)");
	  if (pickSeedStr.equalsIgnoreCase("yes")) // if program picks the seed
	  {
		  seed = r.nextLong(); // randomly generate it
		  JOptionPane.showMessageDialog(null, "Seed is: "+seed);
		  // display it so another program can enter the seed number
	  }
	  else
	  {
		  // read seed from user
		  String seedStr = JOptionPane.showInputDialog("Enter seed");
		  seed = Long.parseLong(seedStr);
	  }
	  r.setSeed(seed); // use seed to initialize random number generator
	  return r;
  }

  public static void main(String[] args)
  {
	  // create Game object 
    Game g = new Game();
    g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}
