import javax.swing.ImageIcon;

public class Rook extends Piece {
    private boolean canCastle = true;

    public Rook(int column, int row, boolean isWhite, ImageIcon image) {
        super(column, row, isWhite, "Rook", image);
    }

    public static Rook make(int column, int row, boolean isWhite) {
        ImageIcon image = new ImageIcon("src\\images\\" + Chess.theme + "White_Rook.png");
        if(!isWhite) image = new ImageIcon("src\\images\\" + Chess.theme + "Black_Rook.png");
        return new Rook(column, row, isWhite, image);
    }

    private int left(Rook rook) {
        int curColumn = rook.getColumn();
        int curRow = rook.getRow();
        for(int x = curColumn-1; x >= 0; x--) {
            if(Chess.pieces[x][curRow] != null) return x; 
        }
        return -1;
    }

    private int right(Rook rook) {
        int curColumn = rook.getColumn();
        int curRow = rook.getRow();
        for(int x = curColumn+1; x < 8; x++) {
            if(Chess.pieces[x][curRow] != null) return x; 
        }
        return -1;
    }

    private int up(Rook rook) {
        int curColumn = rook.getColumn();
        int curRow = rook.getRow();
        for(int y = curRow-1; y >= 0; y--) {
            if(Chess.pieces[curColumn][y] != null) return y;
        }
        return -1;
    }

    private int down(Rook rook) {
        int curColumn = rook.getColumn();
        int curRow = rook.getRow();
        for(int y = curRow+1; y < 8; y++) {
            if(Chess.pieces[curColumn][y] != null) return y;
        }
        return -1;
    }

    public boolean validMove(int column, int row) {
        int curColumn = this.getColumn();
        int curRow = this.getRow();

        if(curRow != row && curColumn != column) return false;

        //  Left-Right (-)
        if(curRow == row) {
            // Left
            if(curColumn > column) {
                int l = left(this);
                if(l <= column || l == -1) return true;
            }
            // Right
            if(curColumn < column) {
                int r = right(this);
                if(r >= column || r == -1) return true;
            }           
        }

        // Up-Down (|)
        if(curColumn == column) {
            // Up
            if(curRow > row) {
                int u = up(this);
                if(u <= row || u == -1) return true;
            }
            //Down
            if(curRow < row) {
                int d = down(this);
                if(d >= row || d == -1) return true;
            }
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