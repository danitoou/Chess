import javax.swing.ImageIcon;

public class Bishop extends Piece {

    public Bishop(int column, int row, boolean isWhite, ImageIcon image) {
        super(column, row, isWhite, "Bishop", image);
    }

    public static Bishop make(int column, int row, boolean isWhite) {
        ImageIcon image = new ImageIcon("src\\images\\" + Chess.theme + "White_Bishop.png");
        if(!isWhite) image = new ImageIcon("src\\images\\" + Chess.theme + "Black_Bishop.png");
        return (Bishop)new Bishop(column, row, isWhite, image);
    }

    private int downLeft(Bishop bishop) {
        int curColumn = bishop.getColumn();
        int curRow = bishop.getRow();
        for(int x = curColumn-1; x >= 0; x--) {
            int y = curRow + curColumn - x;
            if(y < 0 || y > 7) break;
            if(Chess.pieces[x][y] != null) return x;
        }
        return -1;
    }

    private int upRight(Bishop bishop) {
        int curColumn = bishop.getColumn();
        int curRow = bishop.getRow();
        for(int x = curColumn+1; x < 8; x++) {
            int y = curRow + curColumn - x;
            if(y < 0 || y > 7) break;
            if(Chess.pieces[x][y] != null) return x;
        }
        return -1;
    }

    private int upLeft(Bishop bishop) {
        int curColumn = bishop.getColumn();
        int curRow = bishop.getRow();
        int y = curRow-1;
        for(int x = curColumn-1; x > 0; x--) {
            if(y < 0 || y > 7) break;
            if(Chess.pieces[x][y] != null) return x;
            y--;
        }
        return -1;
    }

    private int downRight(Bishop bishop) {
        int curColumn = bishop.getColumn();
        int curRow = bishop.getRow();
        int y = curRow+1;
        for(int x = curColumn+1; x < 8; x++) {
            if(y < 0 || y > 7) break;
            if(Chess.pieces[x][y] != null) return x;
            y++;
        }
        return -1;
    }

    public boolean validMove(int column, int row) {
        int curColumn = this.getColumn();
        int curRow = this.getRow();
        if((curColumn + curRow) % 2 != (column + row) % 2) return false;
        
        // Down-Up Diagonal (/)
        if(curColumn + curRow == column + row) {
            // Down Left
            if(curColumn > column) {
                int dl = downLeft(this);
                if(dl <= column || dl == -1) return true;
            }
            // Up Right
            if(curColumn < column) {
                int ur = upRight(this);
                if(ur >= column || ur == -1) return true;
            }
            
        }

        // Up-Down Diagonal (\)
        if(curColumn-column == curRow-row) {
            // Up Left
            if(curColumn > column) {
                int ul = upLeft(this);
                if(ul <= column || ul == -1) return true;
            }
            // Down Right
            if(curColumn < column) {
                int dr = downRight(this);
                if(dr >= column || dr == -1) return true;
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