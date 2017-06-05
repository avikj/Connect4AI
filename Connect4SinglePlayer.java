// Single player Connect 4 against Monte Carlo Tree Search AI

import java.util.concurrent.TimeUnit;
import java.util.Scanner;

public class Connect4SinglePlayer {
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
          Connect4AI ai = new Connect4AI(board, GIVEN_TIME);
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