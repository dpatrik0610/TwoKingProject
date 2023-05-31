package Controllers;

import Models.BoardGame;
import Models.Position;

public class BoardGameController {
    private BoardGame boardGame;

    public BoardGameController() {
        boardGame = new BoardGame();
        System.out.println(boardGame);
    }

    public void startGame() {
        while (!gameOver()) {
            boardGame.toString();
            Position to = getPlayerMoveFromInput();

            boardGame.move(to);
        }
        handleGameOver();
    }

    private boolean gameOver() {
        // TODO: Implement the game-over condition
        return false;
    }

    private Position getPlayerMoveFromInput() {
        // TODO: Implement the logic to get the player's position from user input
        return new Position(0,0);
    }

    private void handleGameOver() {
        // TODO: Implement the logic for handling the end of the game
        // Determine the winner, display a message, or perform other necessary actions
    }
}
