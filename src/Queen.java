import javax.swing.ImageIcon;

public class Queen extends Piece {

    public Queen(int column, int row, boolean isWhite, ImageIcon image) {
        super(column, row, isWhite, "Queen", image);
    }

    public static Queen make(int column, int row, boolean isWhite) {
        ImageIcon image = new ImageIcon("src\\images\\" + Chess.theme + "White_Queen.png");
        if(!isWhite) image = new ImageIcon("src\\images\\" + Chess.theme + "Black_Queen.png");
        return new Queen(column, row, isWhite, image);
    }

    public boolean validMove(int column, int row) {
        Bishop copyBishop = Bishop.make(this.getColumn(), this.getRow(), this.isWhite());
        Rook copyRook = Rook.make(this.getColumn(), this.getRow(), this.isWhite());
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