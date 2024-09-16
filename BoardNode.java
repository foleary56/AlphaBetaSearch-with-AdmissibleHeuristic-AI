
/**
 * Finn O'Leary and Conner Cutolo
 * Prof Weiss
 * April 5, 2024
 * Take That! Alphabeta Prune Project
 */

import java.util.ArrayList;
import java.util.List;

// Define the BoardNode class to represent a node in the game tree
public class BoardNode {
    // Declare instance variables
    private int[][] boardState;
    private int rowPlayerScore;
    private int colPlayerScore;
    private boolean isRowsTurn;
    private int currentRow;
    private int currentCol;
    private int index;
    private int ply;
    private int numReplaced;
    private int maxValAllowed;

    // Constructor to initialize a BoardNode with given parameters
    public BoardNode(int[][] boardState, int rowPlayerScore, int colPlayerScore, boolean isRowsTurn, int currentRow, int currentCol, int index, int ply) {
        // Initialize instance variables
        this.boardState = boardState;
        this.rowPlayerScore = rowPlayerScore;
        this.colPlayerScore = colPlayerScore;
        this.isRowsTurn = isRowsTurn;
        this.currentRow = currentRow;
        this.currentCol = currentCol;
        this.index = index;
        this.ply = ply;
        this.numReplaced = 0;
        this.maxValAllowed = 0;
    }

    // Method to get the maximum value in the current column
    public int getMaxColVal() {
        int maximum = -26;
        for (int i = 0; i < boardState.length; i++) {
            if (boardState[i][currentCol] != -100) {
                if (maximum > boardState[i][currentCol]) {
                    maximum = boardState[i][currentCol];
                }
            }
        }
        return maximum;
    }

    // Method to get the maximum value in the current row
    public int getMaxRowVal() {
        int maximum = -26;
        for (int i = 0; i < boardState.length; i++) {
            if (boardState[currentRow][i] != -100) {
                if (maximum < boardState[currentRow][i]) {
                    maximum = boardState[currentRow][i];
                }
            }
        }
        return maximum;
    }

    // Method to set the index of the node
    public void setIndex(int x) {
        this.index = x;
    }

    // Method implementing the alpha-beta pruning algorithm
    public int alphabeta(int alpha, int beta, boolean maximizingPlayer, int depth) {
        // Base case: reached the specified depth
        if (depth == 0) {
            return evaluate();
        }

        // Get the list of successor nodes
        List<BoardNode> successors = getSuccessors();
        if (maximizingPlayer) {
            // Maximizing player's turn
            int value = Integer.MIN_VALUE;
            for (BoardNode successor : successors) {
                value = Math.max(value, successor.alphabeta(alpha, beta, false, depth - 1));
                alpha = Math.max(alpha, value);
                if (beta <= alpha) {
                    break;
                }
            }
            return value;
        } else {
            // Minimizing player's turn
            int value = Integer.MAX_VALUE;
            for (BoardNode successor : successors) {
                value = Math.min(value, successor.alphabeta(alpha, beta, true, depth - 1));
                beta = Math.min(beta, value);
                if (beta <= alpha) {
                    break;
                }
            }
            return value;
        }
    }

    // Method to clone the current board state
    private int[][] cloneBoardState(int[][] boardState) {
        int[][] newBoardState = new int[boardState.length][boardState[0].length];
        for (int i = 0; i < boardState.length; i++) {
            System.arraycopy(boardState[i], 0, newBoardState[i], 0, boardState[i].length);
        }
        return newBoardState;
    }

    // Method to generate successor nodes
    private List<BoardNode> getSuccessors() {
        List<BoardNode> successors = new ArrayList<>();
        if (isRowsTurn) {
            // Row player's turn
            for (int i = 0; i < boardState[currentRow].length; i++) {
                if (boardState[currentRow][i] != -100) { //Checks to see if state is selected
                    int[][] newState = cloneBoardState(boardState); //CLones
                    int temp = newState[currentRow][i];
                    newState[currentRow][i] = -100; //Marks selected state as -100
                    BoardNode newnode = new BoardNode(newState, rowPlayerScore + boardState[currentRow][i], colPlayerScore, false, currentRow, i, -1, ply);
                    newnode.numReplaced = temp;
                    newnode.maxValAllowed = newnode.getMaxColVal();
                    successors.add(newnode);
                } else {
                    BoardNode newNode = new BoardNode(boardState, i, i, isRowsTurn, i, i, -100, i);
                    successors.add(newNode);
                }
            }
        } else {
            // Column player's turn
            for (int i = 0; i < boardState.length; i++) {
                if (boardState[i][currentCol] != -100) { //Checks to see if state is selected
                    int[][] newState = cloneBoardState(boardState);//CLones
                    int temp = newState[i][currentCol];
                    newState[i][currentCol] = -100; //Marks selected state as -100
                    BoardNode newnode = new BoardNode(newState, rowPlayerScore, boardState[i][currentCol] + colPlayerScore, true, i, currentCol, -1, ply);
                    newnode.numReplaced = temp;
                    newnode.maxValAllowed = newnode.getMaxRowVal();
                    successors.add(newnode);
                } else {
                    BoardNode newNode = new BoardNode(boardState, i, i, isRowsTurn, i, i, -100, i);
                    successors.add(newNode);
                }
            }
        }
        return successors;
    }

    // Method to check if a win is possible for the current player
    public int checkWin() {
        if (isRowsTurn) {
            for (int i = 0; i < boardState.length; i++) {
                if (boardState[currentRow][i] != -100) {
                    int rowScore = rowPlayerScore + boardState[currentRow][i];
                    if (rowScore > colPlayerScore && isTerminal()) {
                        return i;
                    }
                }
            }
        } else {
            for (int i = 0; i < boardState.length; i++) {
                if (boardState[i][currentCol] != -100) {
                    int colScore = colPlayerScore + boardState[i][currentCol];
                    if (colScore > rowPlayerScore && isTerminal()) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    // Method to check if the current node represents a terminal state
    private boolean isTerminal() {
        for (int col = 0; col < boardState[0].length; col++) {
            if (boardState[col][currentCol] != -100) {
                return false;
            }
        }
        for (int row = 0; row < boardState.length; row++) {
            if (boardState[currentRow][row] != -100) {
                return false;
            }
        }
        return true;
    }

    // Method to calculate the average value of the next row or column
    public double getNextRowOrColumnAverage(boolean isMaximizing) {
        double sum = 0;
        int count = 0;
        if (isMaximizing) {
            for (int i = 0; i < boardState.length; i++) {
                if (boardState[i][currentCol] != -100) {
                    sum += boardState[i][currentCol];
                    count++;
                }
            }
        } else {
            for (int i = 0; i < boardState[currentRow].length; i++) {
                if (boardState[currentRow][i] != -100) {
                    sum += boardState[currentRow][i];
                    count++;
                }
            }
        }
        return count > 0 ? sum / count : 0;
    }

    // Method to evaluate the current node
    public int evaluate() {
        int scoreDifference = isRowsTurn ? rowPlayerScore - colPlayerScore : colPlayerScore - rowPlayerScore; // #1 Evaluates scoreDifference at cell choice
        int possibleMovesLeft = 0; //#2 Initiates for the possible moves left evaluation
        double nextRowOrColumnAverage = getNextRowOrColumnAverage(!isRowsTurn); //#3 Evaluates average of next row or column's average.

        // Calculate possible moves left and total cell value
        if (isRowsTurn) {
            for (int i = 0; i < boardState[currentRow].length; i++) {
                if (boardState[currentRow][i] != -100) {
                    possibleMovesLeft++;
                }
            }
        } else {
            for (int i = 0; i < boardState.length; i++) {
                if (boardState[i][currentCol] != -100) {
                    possibleMovesLeft++;
                }
            }
        }

        // Weight the factors: 10% next cell's total average , 20% possible moves left, 70% score difference
        int evaluation = (int) (0.10 * nextRowOrColumnAverage + 0.20 * possibleMovesLeft + 0.70 * scoreDifference);

        return evaluation;
    }

    // Method to make the computer's choice using alpha-beta pruning
    public int makeComputerChoice() {
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int bestValue = Integer.MIN_VALUE;
        int bestIndex = -1;
        int winnable = checkWin();
        if (winnable != -1) {
            return winnable;
        }

        List<BoardNode> successors = this.getSuccessors();
        for (int i = 0; i < successors.size(); i++) {
            if (successors.get(i).index != -100) {
                

                int value = ((successors.get(i).numReplaced - successors.get(i).maxValAllowed)) + successors.get(i).alphabeta(alpha, beta, false, ply - 1);

                
                if (value > bestValue) {
                    bestValue = value;
                    bestIndex = i;
                    
                }
                alpha = Math.max(alpha, bestValue);
                beta = Math.min(beta, alpha); // Update beta here
            }
        }
        this.setIndex(bestIndex);
    
        return bestIndex;
    }
}
