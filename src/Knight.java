import javax.swing.ImageIcon;

public class Knight extends Piece {

    public Knight(int column, int row, boolean isWhite, ImageIcon image) {
        super(column, row, isWhite, "Knight", image);
    }

    public static Knight make(int column, int row, boolean isWhite) {
        ImageIcon image = new ImageIcon("src\\images\\" + Chess.theme + "White_Knight.png");
        if(!isWhite) image = new ImageIcon("src\\images\\" + Chess.theme + "Black_Knight.png");
        return (Knight)new Knight(column, row, isWhite, image);
    }

    public boolean validMove(int column, int row) {
        int curColumn = this.getColumn();
        int curRow = this.getRow();

        if(curRow-2 == row) {
            if(curColumn-1 == column || curColumn+1 == column) return true;
        }

        if(curRow+2 == row) {
            if(curColumn-1 == column || curColumn+1 == column) return true;
        }

        if(curColumn-2 == column) {
            if(curRow-1 == row || curRow+1 == row) return true;
        }

        if(curColumn+2 == column) {
            if(curRow-1 == row || curRow+1 == row) return true;
        }

        return false;
    }

    public boolean[][] getLegalTiles() {
        boolean[][] arr = new boolean[8][8];
        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                if(this.validMove(x, y)) arr[x][y] = true;
            }
        }
        return arr;
    }

    
}