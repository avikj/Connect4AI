// Single player Connect 4 against Monte Carlo Tree Search AI

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;

public class MCTS {
  private final MCTSNode root; // starting state
  private final int width;
  private static final double EXPLORATION_PARAMETER = Math.sqrt(2);
  private long givenTime = TimeUnit.SECONDS.toNanos(2);
  public MCTS(Connect4Board board, long givenTime) {
    this.width = board.width;
    this.givenTime = givenTime;
    root = new MCTSNode(null, board.copy());
  }

  public int getOptimalMove() {
    for (long stop = System.nanoTime()+givenTime; stop>System.nanoTime();) {
      MCTSNode selectedNode = select();
      if(selectedNode == null)
        continue;
      MCTSNode expandedNode = expand(selectedNode);
      double result = simulate(expandedNode);
      backpropagate(expandedNode, result);
    }

    int maxIndex = -1;
    for(int i = 1; i < width; i++) {
      if(root.children[i] != null) {
        if(maxIndex == -1 || (root.board.getNextTurn() == Connect4Board.PLAYER_1_TURN 
          ? root.children[i].getPlayer1Wins() 
          : (root.children[i].getVisits() - root.children[i].getPlayer1Wins()))/root.children[i].getVisits()
          > 
          (root.board.getNextTurn() == Connect4Board.PLAYER_1_TURN 
          ? root.children[maxIndex].getPlayer1Wins() 
          : (root.children[maxIndex].getVisits() - root.children[maxIndex].getPlayer1Wins())/root.children[maxIndex].getVisits())
          && root.board.canPlace(i))
            maxIndex = i;
      }
    }
    return maxIndex;
  }

  private MCTSNode select() {
    return select(root);
  }

  private MCTSNode select(MCTSNode parent) {
    // if parent has at least child without statistics, select parent
    for(int i = 0; i < width; i++) {
      if(parent.children[i] == null && parent.board.canPlace(i)) {
        return parent;
      }
    }
    // if all children have statistics, use UCT to select next node to visit
    double maxSelectionVal = -1;
    int maxIndex = -1;
    for(int i = 0; i < width; i++) {
      if(!parent.board.canPlace(i))
        continue;
      MCTSNode currentChild = parent.children[i];
      double wins = currentChild.board.getNextTurn() == Connect4Board.PLAYER_1_TURN 
        ? currentChild.getPlayer1Wins() 
        : (currentChild.getVisits()-currentChild.getPlayer1Wins());
      double selectionVal = wins/currentChild.getVisits() 
        + EXPLORATION_PARAMETER*Math.sqrt(Math.log(parent.getVisits())/currentChild.getVisits());// UCT
      if(selectionVal > maxSelectionVal) {
        maxSelectionVal = selectionVal;
        maxIndex = i;
      }
    }
    // SOMETIMES -1???
    if(maxIndex == -1)
      return null;
    return select(parent.children[maxIndex]);
  }

  private MCTSNode expand(MCTSNode selectedNode) {
    // get unvisited child nodes
    ArrayList<Integer> unvisitedChildrenIndices = new ArrayList<Integer>(width);
    for(int i = 0; i < width; i++) {
      if(selectedNode.children[i] == null && selectedNode.board.canPlace(i)) {
        unvisitedChildrenIndices.add(i);
      }
    }

    // randomly select unvisited child and create node for it
    int selectedIndex = unvisitedChildrenIndices.get((int)(Math.random()*unvisitedChildrenIndices.size()));
    selectedNode.children[selectedIndex] = new MCTSNode(selectedNode, selectedNode.board.getNextState(selectedIndex));
    return selectedNode.children[selectedIndex];
  } 

  // returns result of simulation
  private double simulate(MCTSNode expandedNode) {
    Connect4Board simulationBoard = expandedNode.board.copy();
    while(simulationBoard.currentGameState() == Connect4Board.ONGOING) {
      simulationBoard.place((int)(Math.random()*width));
    }
      // System.out.println(simulationBoard);

    switch(simulationBoard.currentGameState()) {
      case Connect4Board.PLAYER_1_WON:
        return 1;
      case Connect4Board.PLAYER_2_WON:
        return 0;
      default:
        return 0.5;
    }
  }

  private void backpropagate(MCTSNode expandedNode, double simulationResult) {
    MCTSNode currentNode = expandedNode;
    while(currentNode != null) {
      currentNode.incrementVisits();
      currentNode.incrementPlayer1Wins(simulationResult);
      currentNode = currentNode.parent;
    }
  }

  private class MCTSNode {
    private MCTSNode parent;
    // children[i] represents the next game state in which current player places disc at location i
    private MCTSNode[] children;
    private int visits;
    private double player1Wins;
    private boolean visited;
    private final Connect4Board board;
    public MCTSNode(MCTSNode parent, Connect4Board board) {
      this.parent = parent;
      this.board = board;
      this.visits = 0;
      this.player1Wins = 0;
      children = new MCTSNode[width];
      visited = false;
    }

    /*public void instantiateChildren() {
      for(int i = 0; i < width; i++) {
        if(children[i] == null) {
          children[i] = new MCTSNode(this, board.getNextState(i));
        }
      }
    }*/

    public int incrementVisits() {
      return ++visits;
    }

    public double incrementPlayer1Wins(double result) {
      player1Wins += result;
      return player1Wins;
    }

    public boolean hasStatistics() {
      return visited;
    }
    public void createStatistics() {
      visited = true;
    }

    public Connect4Board getBoard() {
      return board;
    }

    public int getVisits() {
      return visits;
    }
    public double getPlayer1Wins() {
      return player1Wins;
    }
  }

  public static void main(String[] args) {
    final long GIVEN_TIME = TimeUnit.SECONDS.toNanos(args.length > 0 ? Integer.parseInt(args[0]) : 2);
    Scanner in = new Scanner(System.in);
    Connect4Board board = new Connect4Board();
    boolean turn = Connect4Board.PLAYER_1_TURN;
    while(board.currentGameState() == Connect4Board.ONGOING) {
      System.out.println("\n\n"+board);
      int moveColumn;
      do {
        if(board.getNextTurn() == Connect4Board.PLAYER_1_TURN) {
          System.out.printf("Enter your move: ", board.getNextTurn() == Connect4Board.PLAYER_1_TURN ? 1 : 2);
          moveColumn = in.nextInt();
        }
        else {
          System.out.print("AI determining move: ");
          MCTS ai = new MCTS(board, GIVEN_TIME);
          moveColumn = ai.getOptimalMove();
          System.out.println(moveColumn);
        }
      } while(!board.canPlace(moveColumn));
      board.place(moveColumn);
    }
    int gameState = board.currentGameState();
    System.out.println("\n\n\n\n\n");
    System.out.println(board);
    switch(gameState) {
      case Connect4Board.PLAYER_1_WON:
        System.out.println("You won.\n");
        break;
      case Connect4Board.PLAYER_2_WON:
        System.out.println("AI won.\n");
        break;
      default:
        System.out.println("Tie.\n");
        break;
    }
  }
}