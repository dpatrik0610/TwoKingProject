package Controllers;

import Models.BoardGame;
import Models.Cell;
import Models.Position;
import Models.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BoardGameConsoleController {
    private BoardGame boardGame;

    public BoardGameConsoleController() {
        this.boardGame = new BoardGame();
    }

    public void playGame() {
        Scanner scanner = new Scanner(System.in);
        boolean gameRunning = true;

        System.out.println("Welcome to the Two King Game!");

        while (gameRunning) {
            System.out.println(boardGame);

            System.out.println("Current player: " + boardGame.getCurrentPlayer());
            Position to = getPlayerMoveFromInput(scanner);
            boardGame.move(to);

            if (gameOver()) {
                gameRunning = false;
                handleGameOver();
            }
        }
    }

    private boolean gameOver() {
        return !hasLegalMoves(boardGame.getCurrentPlayerPosition());
    }
    private boolean hasLegalMoves(Position playerPosition){
        List<Position> adjacentPositions = getAdjacentPositions(playerPosition);
        for (Position position : adjacentPositions) {
            if (boardGame.getCell(position) == Cell.UNVISITED) {
                return true;
            }
        }
        return false;
    }

    /**
     * Iterate over a 3x3 grid centered around the given position.
     * Check if each grid position is within the bounds of the board and exclude the current position itself.
     * @param position of a cell.
     * @return Array containing adjacent Positions
     */
    private List<Position> getAdjacentPositions(Position position) {
        int row = position.row();
        int col = position.col();
        List<Position> adjacentPositions = new ArrayList<>();

        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                Position adjacentCell = new Position(i, j);
                if (isValidPosition(adjacentCell) && (i != row || j != col)) {
                    adjacentPositions.add(adjacentCell);
                }
            }
        }
        return adjacentPositions;
    }
    private boolean isValidPosition(Position position) {
        int row = position.row();
        int col = position.col();
        return row >= 0 && row < BoardGame.BOARD_HEIGHT && col >= 0 && col < BoardGame.BOARD_WIDTH;
    }

    private Position getPlayerMoveFromInput(Scanner scanner) {
        System.out.print("Enter row of the position to move to: ");
        int row = scanner.nextInt();
        System.out.print("Enter column of the position to move to: ");
        int col = scanner.nextInt();
        return new Position(row, col);
    }

    private void handleGameOver() {
        State loser = boardGame.getCurrentPlayer();
        State winner = (loser == State.BLACK ? State.WHITE : State.BLACK);
        Position winnerPosition = (winner == State.BLACK ? boardGame.getBlackKing() : boardGame.getWhiteKing());

        System.out.println("Game Over");
        System.out.println("Winner: " + winner);
        System.out.println("Winner's position: " + winnerPosition);
    }
}
