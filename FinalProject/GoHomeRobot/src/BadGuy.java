public class BadGuy {
    private int row, col;

    public BadGuy(int startRow, int startCol) {
        this.row = startRow;
        this.col = startCol;
    }

    public void moveTowards(int targetRow, int targetCol) {
        if (Math.abs(targetRow - row) > Math.abs(targetCol - col)) {
            row += (targetRow > row) ? 1 : -1;
        } else {
            col += (targetCol > col) ? 1 : -1;
        }
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
}