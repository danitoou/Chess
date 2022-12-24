
public class Queen extends Piece {

    public Queen(int column, int row, boolean isWhite) {
        super(column, row, isWhite, "Queen");
    }

    @Override
    public boolean validMove(int column, int row) {
        Bishop copyBishop = new Bishop(this.getColumn(), this.getRow(), this.isWhite());
        Rook copyRook = new Rook(this.getColumn(), this.getRow(), this.isWhite());
        if(copyBishop.validMove(column, row) || copyRook.validMove(column, row)) return true;
        return false;
    }

    @Override
    public boolean[][] getValidTiles() {
        boolean[][] arr = new boolean[8][8];
        Bishop copyBishop = new Bishop(this.getColumn(), this.getRow(), this.isWhite());
        Rook copyRook = new Rook(this.getColumn(), this.getRow(), this.isWhite());
        boolean[][] bishopTiles = copyBishop.getValidTiles();
        boolean[][] rookTiles = copyRook.getValidTiles();

        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                if(bishopTiles[x][y] || rookTiles[x][y]) arr[x][y] = true;
            }
        }
        return arr;
    }

    
}