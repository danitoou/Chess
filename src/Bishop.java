
public class Bishop extends Piece {

    public Bishop(int column, int row, boolean isWhite) {
        super(column, row, isWhite, "Bishop");
    }

    private int downLeft() {
        int curColumn = this.getColumn();
        int curRow = this.getRow();
        for(int x = curColumn-1; x >= 0; x--) {
            int y = curRow + curColumn - x;
            if(y < 0 || y > 7) break;
            if(Chess.pieces[x][y] != null) return x;
        }
        return -1;
    }

    private int upRight() {
        int curColumn = this.getColumn();
        int curRow = this.getRow();
        for(int x = curColumn+1; x < 8; x++) {
            int y = curRow + curColumn - x;
            if(y < 0 || y > 7) break;
            if(Chess.pieces[x][y] != null) return x;
        }
        return -1;
    }

    private int upLeft() {
        int curColumn = this.getColumn();
        int curRow = this.getRow();
        int y = curRow-1;
        for(int x = curColumn-1; x > 0; x--) {
            if(y < 0 || y > 7) break;
            if(Chess.pieces[x][y] != null) return x;
            y--;
        }
        return -1;
    }

    private int downRight() {
        int curColumn = this.getColumn();
        int curRow = this.getRow();
        int y = curRow+1;
        for(int x = curColumn+1; x < 8; x++) {
            if(y < 0 || y > 7) break;
            if(Chess.pieces[x][y] != null) return x;
            y++;
        }
        return -1;
    }

    @Override
    public boolean validMove(int column, int row) {
        int curColumn = this.getColumn();
        int curRow = this.getRow();
        if((curColumn + curRow) % 2 != (column + row) % 2) return false;
        
        // Down-Up Diagonal (/)
        if(curColumn + curRow == column + row) {
            // Down Left
            if(curColumn > column) {
                int dl = this.downLeft();
                if(dl <= column || dl == -1) return true;
            }
            // Up Right
            if(curColumn < column) {
                int ur = this.upRight();
                if(ur >= column || ur == -1) return true;
            }
            
        }

        // Up-Down Diagonal (\)
        if(curColumn-column == curRow-row) {
            // Up Left
            if(curColumn > column) {
                int ul = this.upLeft();
                if(ul <= column || ul == -1) return true;
            }
            // Down Right
            if(curColumn < column) {
                int dr = this.downRight();
                if(dr >= column || dr == -1) return true;
            }
        }
        return false;
    }

    @Override
    public boolean[][] getValidTiles() {
        boolean[][] arr = new boolean[8][8];
        int curColumn = this.getColumn();
        int curRow = this.getRow();

        for(int x = curColumn-1; x >= 0; x--) { // Down Left
            int y = curRow + curColumn - x;
            if(y < 0 || y > 7) break;
            arr[x][y] = true;
            if(Chess.pieces[x][y] != null) break;
        }

        for(int x = curColumn+1; x < 8; x++) { // Up Right
            int y = curRow + curColumn - x;
            if(y < 0 || y > 7) break;
            arr[x][y] = true;
            if(Chess.pieces[x][y] != null) break;
        }

        int y = curRow-1;
        for(int x = curColumn-1; x > 0; x--) { // Up Left
            if(y < 0 || y > 7) break;
            arr[x][y] = true;
            if(Chess.pieces[x][y] != null) break;
            y--;
        }

        y = curRow+1;
        for(int x = curColumn+1; x < 8; x++) { // Down Right
            if(y < 0 || y > 7) break;
            arr[x][y] = true;
            if(Chess.pieces[x][y] != null) break;
            y++;
        }


        return arr;
    }

    
}