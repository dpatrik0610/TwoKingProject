package Models;

public class BoardGame {
    public static final int BOARD_WIDTH = 8;
    public static final int BOARD_HEIGHT = 6;
    private Cell[][] board;
    private Position whiteKing = new Position(2, 0);
    private Position blackKing = new Position(3, 7);
    private State player;

    public BoardGame(){
        board = new Cell[BOARD_HEIGHT][BOARD_WIDTH];

        for (int i = 0; i < BOARD_HEIGHT; i++){
            for (int j = 0; j < BOARD_WIDTH; j++){
                board[i][j] = Cell.UNVISITED;
            }
        }
        setCell(whiteKing, Cell.KING);
        setCell(blackKing, Cell.KING);
        player = State.WHITE;
    }

    public Cell getCell(Position p){
        return board[p.row()][p.col()];
    }

    public void setCell(Position pos, Cell cellType){
        board[pos.row()][pos.col()] = cellType;
    }

    public void move(Position to){
        Position from = getCurrentPlayerPosition();
        if(legalStep(from, to)){
            setCell(to, Cell.KING);
            setCell(from, Cell.REMOVED);
            movePlayer(to);
            switchTurn();
        }
    }
    private void movePlayer(Position to){
        if (player == State.WHITE) whiteKing = to;
        else blackKing = to;
    }
    /**
     * Switching the turn of players. ex: Black moved, then the current player will be white.
     */
    private void switchTurn(){
        switch (player){
            case BLACK -> player = State.WHITE;
            case WHITE -> player = State.BLACK;
        }
    }
    private Position getCurrentPlayerPosition() {
        return (player == State.BLACK) ? blackKing : whiteKing;
    }
    /**
     * Checking if the step is correct.
     * @param to The position where the piece wants to move.
     * @return True if the step is legal, False if step is not legal.
     */

    private boolean legalStep(Position from, Position to){
        return !isCellRemoved(to) && isKingMove(from,to);
    }
    private boolean isCellRemoved(Position p){return getCell(p) == Cell.REMOVED;}
    private boolean isKingMove(Position from, Position to){
        var dx = Math.abs(to.row() - from.row());
        var dy = Math.abs(to.col() - from.col());
        return dx + dy == 1 || dx * dy == 1;
    }
    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (var i = 0; i < BOARD_HEIGHT; i++) {
            for (var j = 0; j < BOARD_WIDTH; j++) {
                switch (board[i][j]){
                    case KING -> {
                        if(new Position(i, j).equals(whiteKing)){sb.append("W ");}
                        if(new Position(i, j).equals(blackKing)){sb.append("B ");}
                    }
                    case REMOVED -> {
                        sb.append("X ");
                    }
                    case UNVISITED -> {
                        sb.append("O ");
                    }
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
