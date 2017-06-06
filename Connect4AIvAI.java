// Connect 4 with Monte Carlo Tree Search AI playing both sides
import java.util.concurrent.TimeUnit;

public class Connect4AIvAI {
  public static void main(String[] args) {
    final long GIVEN_TIME = TimeUnit.SECONDS.toNanos(args.length > 0 ? Integer.parseInt(args[0]) : 2);
    Connect4Board board = new Connect4Board();
    while(board.currentGameState() == Connect4Board.ONGOING) {
      System.out.println("\n\n"+board);
      int moveColumn;
      do {
        System.out.printf("AI %d determining move: ", board.getNextTurn() == Connect4Board.PLAYER_1_TURN ? 1 : 2);
        Connect4AI ai = new Connect4AI(board, GIVEN_TIME);
        moveColumn = ai.getOptimalMove();
        System.out.println(moveColumn);
      } while(!board.canPlace(moveColumn));
      board.place(moveColumn);
    }
    int gameState = board.currentGameState();
    System.out.println("\n\n\n\n\n");
    System.out.println(board);
    switch(gameState) {
      case Connect4Board.PLAYER_1_WON:
        System.out.println("AI 1 won.\n");
        break;
      case Connect4Board.PLAYER_2_WON:
        System.out.println("AI 2 won.\n");
        break;
      default:
        System.out.println("Tie.\n");
        break;
    }
  }
}