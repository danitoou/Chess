
public class Queen extends Piece {

    public Queen(int column, int row, boolean isWhite) {
        super(column, row, isWhite, "Queen");
    }

    public boolean validMove(int column, int row) {
        Bishop copyBishop = new Bishop(this.getColumn(), this.getRow(), this.isWhite());
        Rook copyRook = new Rook(this.getColumn(), this.getRow(), this.isWhite());
        if(copyBishop.validMove(column, row) || copyRook.validMove(column, row)) return true;
        return false;
    }

    // public boolean[][] getLegalTiles() {
    //     boolean[][] arr = new boolean[8][8];
    //     for(int x = 0; x < 8; x++) {
    //         for(int y = 0; y < 8; y++) {
    //             if(this.validMove(x, y)) arr[x][y] = true;
    //         }
    //     }
    //     return arr;
    // }

    
}