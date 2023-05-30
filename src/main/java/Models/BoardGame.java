package Models;

import javafx.geometry.Pos;

public class BoardGame {
    public static final int BOARD_WIDTH = 8;
    public static final int BOARD_HEIGHT = 6;
    private Cell[][] board;

    private State player;

    /**
     * Setting the initial state of board and the position of the two King pieces.
     * @param whitePos is the position of the white king.
     * @param blackPos is the position of the black king.
     */
    public BoardGame(Position whitePos, Position blackPos){
        board = new Cell[BOARD_WIDTH][BOARD_HEIGHT];

        for (int i = 0; i < BOARD_WIDTH; i++){
            for (int j = 0; j < BOARD_HEIGHT; j++){
                board[i][j] = Cell.UNEXPLORED;
            }
        }
        setCellProperty(whitePos, Cell.CAPTURED);
        setCellProperty(blackPos, Cell.CAPTURED);
        player = State.WHITE;
    }

    public Cell getCellProperty(int i, int j){
        return board[i][j];
    }
    public void setCellProperty(Position pos, Cell cellType){
        board[pos.getRow()][pos.getCol()] = cellType;
    }

    public void move(Position from, Position to){
        if(legalStep(from, to)){
            board[from.getRow()][from.getCol()] = Cell.EXPLORED;
            board[to.getRow()][to.getCol()] = Cell.CAPTURED;
            switchTurn();
        }
    }
    /**
     * Switching the turn of players. ex: Black moved, then the current player will be white.
     */
    private void switchTurn(){
        switch (this.player){
            case BLACK -> player = State.WHITE;
            case WHITE -> player = State.BLACK;
        }
    }

    /**
     * Checking if the step is correct.
     * @param from The position of the Piece.
     * @param to The position where the piece wants to move.
     * @return True if the step is legal, False if step is not legal.
     */
    private boolean legalStep(Position from, Position to){
        if (board[to.getRow()][to.getCol()] == Cell.EXPLORED) return false;
        var dx = Math.abs(to.getRow() - from.getRow());
        var dy = Math.abs(to.getCol() - from.getCol());
        return dx + dy == 1 || dx * dy == 1;
    }

}
