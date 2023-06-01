package Controllers;

import Models.BoardGame;
import Models.Cell;
import Models.Position;
import Models.State;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The BoardGameUIController class is responsible for managing the user interface of the board game.
 * It handles user input, updates the board state, and displays game-related information.
 */
public class BoardGameUIController {
    private static final Logger logger = LogManager.getLogger(BoardGameUIController.class);
    @FXML
    private GridPane board;

    private BoardGame model = new BoardGame();

    /**
     * Initializes the board game UI.
     * It adds squares to the board and paints the circles to their correct color.
     */
    @FXML
    private void initialize() {
        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare();
                board.add(square, j, i);
            }
        }
        paintKingCircles();
    }

    /**
     * Creates a {@link StackPane} and adds a {@link Circle} to it.
     * @return StackPane
     */
    private StackPane createSquare() {
        StackPane square = new StackPane();
        square.getStyleClass().addAll("square", "unvisited");
        Circle circle = createCircle();
        square.getChildren().add(circle);
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    /**
     * Creates a {@link Circle} with a size of 40 pixels and the "transparentCircle" CSS class.
     *
     * @return the created circle
     */
    private Circle createCircle() {
        Circle circle = new Circle(40);
        circle.getStyleClass().add("transparentCircle");
        return circle;
    }

    /**
     * Retrieves the stack pane at the specified position on the board.
     *
     * @param position the position of the stack pane on the board
     * @return the stack pane at the specified position
     * @throws AssertionError if the stack pane is not found
     */
    private StackPane getSquare(Position position) {
        for (var child : board.getChildren()) {
            if (GridPane.getRowIndex(child) == position.row() && GridPane.getColumnIndex(child) == position.col()) {
                return (StackPane) child;
            }
        }
        throw new AssertionError();
    }

    /**
     * Retrieves the circle child of a stack pane.
     *
     * @param square the {@link StackPane} containing the circle
     * @return the {@link Circle} object found within the stack pane
     * @throws AssertionError if a circle is not found within the StackPane
     */
    private Circle getCircleFromSquare(StackPane square) {
        for (Node child : square.getChildren()) {
            if (child instanceof Circle) {
                return (Circle) child;
            }
        }
        throw new AssertionError("Circle not found in the StackPane.");
    }

    /**
     * Paints the king circles on the board.
     * It removes the "transparentCircle" class and adds the "whiteKing" and "blackKing" classes to the corresponding circles.
     */
    private void paintKingCircles(){
        Position whitePos = model.getWhiteKing();
        Position blackPos = model.getBlackKing();
        Circle whiteCircle = getCircleFromSquare(getSquare(whitePos));
        Circle blackCircle = getCircleFromSquare(getSquare(blackPos));

        whiteCircle.getStyleClass().remove("transparentCircle");
        whiteCircle.getStyleClass().add("whiteKing");
        blackCircle.getStyleClass().remove("transparentCircle");
        blackCircle.getStyleClass().add("blackKing");
    }

    /**
     * Handles a mouse click event on a {@link StackPane}.
     * If the step is legal, it removes the previous cell, makes a move, and redraws the king circles.
     *
     * @param event the mouse click event
     */
    @FXML
    private void handleMouseClick(MouseEvent event) {
        Position from = model.getCurrentPlayerPosition();
        Position to = getCellPositionAtMouse(event);
        logger.info("Mouse click on cell {}", to);
        if(model.legalStep(from, to)){
            removeOldCell(getSquare(from));
            model.move(to);
            paintKingCircles();
        }
        if (gameOver()) handleGameOver();
    }

    /**
     * Retrieves the position of the {@link StackPane} based on the mouse click event.
     *
     * @param event the mouse click event
     * @return the position of the stack pane
     */
    private Position getCellPositionAtMouse(MouseEvent event){
        StackPane clickedCell = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(clickedCell);
        var col = GridPane.getColumnIndex(clickedCell);
        return new Position(row, col);
    }

    /**
     * Changes to .removed class on the previous {@link StackPane} cell. and deletes its {@link Circle} children.
     * @param oldCell StackPane of the previous cell.
     */
    private void removeOldCell(StackPane oldCell){
        Circle circle = getCircleFromSquare(oldCell);

        oldCell.getStyleClass().remove("unvisited");
        oldCell.getStyleClass().add("removed");
        oldCell.getChildren().remove(circle);
    }

    /**
     * Checks if the game is over.
     *
     * @return {@code true} if the current player has no legal moves, {@code false} otherwise.
     */
    private boolean gameOver() {
        return !hasLegalMoves(model.getCurrentPlayerPosition());
    }

    /**
     * Checks if a player has legal moves from the given position.
     *
     * @param playerPosition the position of the player
     * @return {@code true} if the player has legal moves, {@code false} otherwise
     */
    private boolean hasLegalMoves(Position playerPosition) {
        for (Position position : getAdjacentPositions(playerPosition)) {
            if (model.getCell(position) == Cell.UNVISITED) {
                return true;
            }
        }
        return false;
    }
    /**
     * Retrieves the adjacent positions of a given {@link Position}.
     *
     * @param position the position
     * @return a list of adjacent positions
     */
    private List<Position> getAdjacentPositions(Position position) {
        int row = position.row();
        int col = position.col();
        List<Position> adjacentPositions = new ArrayList<>();

        logger.debug("Adjacent cells around Position {}:",position);
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                Position adjacentCell = new Position(i, j);
                if (isValidPosition(adjacentCell) && (i != row || j != col)) {
                    adjacentPositions.add(adjacentCell);
                    logger.debug("Adjacent Cell: {}",adjacentCell);
                }
            }
        }
        return adjacentPositions;
    }
    /**
     * Checks if a {@link Position} is valid within the board boundaries.
     *
     * @param position the position
     * @return {@code true} if the given position is valid, {@code false} otherwise
     */
    private boolean isValidPosition(Position position) {
        int row = position.row();
        int col = position.col();
        return row >= 0 && row < BoardGame.BOARD_HEIGHT && col >= 0 && col < BoardGame.BOARD_WIDTH;
    }
    /**
     * Handles the game over scenario.
     * It displays an alert with the winner and their position.
     */
    private void handleGameOver() {
        State loser = model.getCurrentPlayer();
        State winner = (loser == State.BLACK) ? State.WHITE : State.BLACK;
        Position winnerPosition = (winner == State.BLACK) ? model.getBlackKing() : model.getWhiteKing();

        logger.info("Game Over.");
        logger.debug("WHITE position: {}", winnerPosition);
        logger.debug("BLACK position: {}", model.getCurrentPlayerPosition());
        logger.debug("Winner: {}", winner);

        showAlert(winner, winnerPosition);
    }

    /**
     * Shows an Alert with information about the winner, and it's location.
     * @param winner the {@link State} of the player who has legal moves left.
     * @param winnerPosition the {@link Position} of the winner.
     */
    private void showAlert(State winner, Position winnerPosition){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        alert.setTitle("Game Over");
        alert.setHeaderText("Winner: " + winner);
        alert.setContentText("Winner's position: " + winnerPosition);
        alert.showAndWait();
    }
}
