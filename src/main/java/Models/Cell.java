package Models;
/**
 * The {@link Cell} enum represents the possible states of a cell on the board game.
 * Each cell can be in one of the following states: UNVISITED, REMOVED, or KING.
 */
public enum Cell {
    /**
     * Represents an unvisited cell on the board.
     */
    UNVISITED,
    /**
     * Represents a removed cell on the board.
     */
    REMOVED,
    /**
     * Represents a cell with a king on the board.
     */
    KING
}
