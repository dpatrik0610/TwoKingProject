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
    public State getCurrentPlayer(){
        return player;
    }
    public Position getWhiteKing(){return whiteKing;}
    public Position getBlackKing(){return blackKing;}
    public void move(Position to){
        Position from = getCurrentPlayerPosition();
        if(legalStep(from, to)){
            setCell(to, Cell.KING);
            setCell(from, Cell.REMOVED);
            movePlayer(to);
            switchTurn();
        }
    }
    public Position getCurrentPlayerPosition() {
        return (getCurrentPlayer() == State.WHITE) ? whiteKing : blackKing;
    }
    private void movePlayer(Position to){
        switch (getCurrentPlayer()){
            case WHITE -> whiteKing = to;
            case BLACK -> blackKing = to;
        }
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

    /**
     * Checking if the step is correct. Instead of checking if the "to" cell is in REMOVED or KING state,
     * we examine whether the cell is in UNEXPLORED state.
     * @param to The position of the King piece.
     * @param to The position where the piece wants to move.
     * @return True if the "to" cell is in UNEXPLORED state and if it's a legal king move, else False.
     */
    private boolean legalStep(Position from, Position to){
        return isCellUnexplored(to) && isKingMove(from,to);
    }

    private boolean isCellUnexplored(Position p){return getCell(p) == Cell.UNVISITED;}
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
