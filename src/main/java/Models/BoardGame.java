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
    public boolean move(Position to){
        Position from = getCurrentPlayerPosition();
        if(legalStep(from, to)){
            setCell(to, Cell.KING);
            setCell(from, Cell.REMOVED);
            movePlayer(to);
            switchTurn();
            return true;
        }
        return false;
    }
    public Position getCurrentPlayerPosition() {
        return (getCurrentPlayer() == State.WHITE) ? whiteKing : blackKing;
    }
    /**
     * Move the player to the specified position.
     * @param to The position to move the player to.
     */
    private void movePlayer(Position to){
        switch (getCurrentPlayer()){
            case WHITE -> whiteKing = to;
            case BLACK -> blackKing = to;
        }
    }

    /**
     * Switch the turn of players. For example, if the current player is BLACK,
     * switchTurn will set the current player to WHITE.
     */
    private void switchTurn(){
        switch (player){
            case BLACK -> player = State.WHITE;
            case WHITE -> player = State.BLACK;
        }
    }

    /**
     * Check if the step is legal. The step is considered legal if the "to" cell is in the UNEXPLORED {@link State}
     * and if it's a legal king move.
     *
     * @param from The position of the current player.
     * @param to   The position where the player wants to move.
     * @return True if the step is legal, False otherwise.
     */
    public boolean legalStep(Position from, Position to){
        return isCellUnexplored(to) && isKingMove(from,to);
    }

    /**
     * Check if the specified cell is in the UNEXPLORED state.
     *
     * @param p The position of the cell to check.
     * @return True if the cell is in the UNEXPLORED state, False otherwise.
     */
    private boolean isCellUnexplored(Position p){return getCell(p) == Cell.UNVISITED;}

    /**
     * Check if the move from the specified position to the specified position is a legal king move.
     *
     * @param from The starting position.
     * @param to   The destination position.
     * @return True if the move is a legal king move, False otherwise.
     */
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
