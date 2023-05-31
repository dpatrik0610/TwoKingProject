package Controllers;

import Models.BoardGame;
import Models.Cell;
import Models.Position;
import Models.State;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

import static Models.Cell.*;
public class BoardGameUIController {
    @FXML
    private GridPane board;
    @FXML
    private Label currentPlayerLabel;
    private BoardGame model = new BoardGame();
    @FXML
    private void initialize() {
        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare(i, j);
                board.add(square, j, i);
            }
        }

    }
    private StackPane createSquare(int i, int j) {
        var square = new StackPane();
        square.getStyleClass().add("square");
        var piece = new Circle(40);
        colorCircles(piece, i, j);
        square.getChildren().add(piece);
        // TODO square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }
    private void colorCircles(Circle circle, int i, int j){
        if(new Position(i, j).equals(model.getWhiteKing())){
            circle.getStyleClass().add("blackKing");
        } else if(new Position(i, j).equals(model.getBlackKing())){
            circle.getStyleClass().add("whiteKing");
        } else {
            circle.getStyleClass().add("transparentCircle");
        }
    }
    /*
    TODO
    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);

        Position newPos = new Position(row, col);
        model.move(newPos);
        if (gameOver()) {
            handleGameOver();
        } else {
            currentPlayerLabel.setText("Current player: " + model.getCurrentPlayer());
        }
    }
*/
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
