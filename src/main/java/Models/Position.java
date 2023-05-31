package Models;

public record Position (int row, int col){

    @Override
    public String toString() {
        return String.format("(%d,%d)", this.row, this.col);
    }
}
