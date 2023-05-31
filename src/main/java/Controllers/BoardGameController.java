package Controllers;

import Models.BoardGame;
import Models.Cell;
import Models.Position;
import Models.State;

public class BoardGameController {
    private BoardGame boardGame;

    public BoardGameController() {
        Position whiteKingPos = new Position(0, 2);
        Position blackKingPos = new Position(7, 3);
        boardGame = new BoardGame(whiteKingPos, blackKingPos);
        printBoard();
    }

    public void startGame() {
        while (!gameOver()) {
            printBoard();

            Position from = getPlayerMoveFromInput();
            Position to = getPlayerMoveFromInput();

            boardGame.move(from, to);
        }
        handleGameOver();
    }

    private boolean gameOver() {
        // TODO: Implement the game-over condition
        // Otherwise, return false
        return false;
    }

    private void printBoard() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < boardGame.BOARD_WIDTH; i++) {
            for (int j = 0; j < boardGame.BOARD_HEIGHT; j++) {
                switch (boardGame.getCellProperty(i, j)){
                    case CAPTURED -> {
                        sb.append("K ");
                        // TODO: Position of the 2 kings separately
                    }
                    case EXPLORED -> {
                        sb.append("X ");
                    }
                    case UNEXPLORED -> {
                        sb.append("O ");
                    }
                }
            }
            sb.append("\n");
        }
        System.out.println(sb);
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
