package Controllers;

import Models.BoardGame;
import Models.Cell;
import Models.Position;
import Models.State;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;


public class BoardGameUIController {
    @FXML
    private GridPane board;

    private BoardGame model = new BoardGame();
    @FXML
    private void initialize() {
        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare(i, j);
                board.add(square, j, i);
            }
        }
        colorCircles();
    }
    private StackPane createSquare(int i, int j) {
        StackPane square = new StackPane();
        square.getStyleClass().addAll("square", "unvisited");
        Circle circle = createCircle();
        square.getChildren().add(circle);
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    private Circle createCircle() {
        Circle circle = new Circle(40);
        circle.getStyleClass().add("transparentCircle");
        return circle;
    }

    private StackPane getSquare(Position position) {
        for (var child : board.getChildren()) {
            if (GridPane.getRowIndex(child) == position.row() && GridPane.getColumnIndex(child) == position.col()) {
                return (StackPane) child;
            }
        }
        throw new AssertionError();
    }
    private Circle getCircleFromSquare(StackPane square) {
        for (Node child : square.getChildren()) {
            if (child instanceof Circle) {
                return (Circle) child;
            }
        }
        // Never happens
        throw new AssertionError("Circle not found in the StackPane.");
    }
    private void colorCircles(){
        Position whitePos = model.getWhiteKing();
        Position blackPos = model.getBlackKing();
        Circle whiteCircle = getCircleFromSquare(getSquare(whitePos));
        Circle blackCircle = getCircleFromSquare(getSquare(blackPos));

        whiteCircle.getStyleClass().remove("transparentCircle");
        whiteCircle.getStyleClass().add("whiteKing");
        blackCircle.getStyleClass().remove("transparentCircle");
        blackCircle.getStyleClass().add("blackKing");
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        StackPane clickedCell = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(clickedCell);
        var col = GridPane.getColumnIndex(clickedCell);
        Position from = model.getCurrentPlayerPosition();
        Position to = new Position(row, col);

        if(model.legalStep(from, to)){
            makeCellRemoved(getSquare(from));
            model.move(to);
            colorCircles();
        }
        if (gameOver()) {
            handleGameOver();
        }
    }

    private void makeCellRemoved(StackPane oldCell){
        Circle circle = getCircleFromSquare(oldCell);

        oldCell.getStyleClass().remove("unvisited");
        oldCell.getStyleClass().add("removed");
        oldCell.getChildren().remove(circle);
    }
    private boolean gameOver() {
        return !hasLegalMoves(model.getCurrentPlayerPosition());
    }
    private boolean hasLegalMoves(Position playerPosition) {
        for (Position position : getAdjacentPositions(playerPosition)) {
            if (model.getCell(position) == Cell.UNVISITED) {
                return true;
            }
        }
        return false;
    }
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

    private void handleGameOver() {
        State loser = model.getCurrentPlayer();
        State winner = (loser == State.BLACK) ? State.WHITE : State.BLACK;
        Position winnerPosition = (winner == State.BLACK) ? model.getBlackKing() : model.getWhiteKing();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Winner: " + winner);
        alert.setContentText("Winner's position: " + winnerPosition);
        alert.showAndWait();
    }
}
