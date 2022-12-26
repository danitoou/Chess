
public class Rook extends Piece {
    private boolean canCastle = true;

    public void setCanCastle(boolean canCastle) {
        this.canCastle = canCastle;
    }

    public boolean getCanCastle() {
        return this.canCastle;
    }

    public Rook(int column, int row, boolean isWhite) {
        super(column, row, isWhite, "Rook");
    }

    private int left() {
        int curColumn = this.getColumn();
        int curRow = this.getRow();
        for(int x = curColumn-1; x >= 0; x--) {
            if(Chess.pieces[x][curRow] != null) {
                if(Chess.pieces[x][curRow].isWhite() == this.isWhite()) return x+1;
                return x;
            }
        }
        return 0;
    }

    private int right() {
        int curColumn = this.getColumn();
        int curRow = this.getRow();
        for(int x = curColumn+1; x < 8; x++) {
            if(Chess.pieces[x][curRow] != null) {
                if(Chess.pieces[x][curRow].isWhite() == this.isWhite()) return x-1;
                return x;
            }
        }
        return 7;
    }

    private int up() {
        int curColumn = this.getColumn();
        int curRow = this.getRow();
        for(int y = curRow-1; y >= 0; y--) {
            if(Chess.pieces[curColumn][y] != null) {
                if(Chess.pieces[curColumn][y].isWhite() == this.isWhite()) return y+1;
                return y;
            }
        }
        return 0;
    }

    private int down() {
        int curColumn = this.getColumn();
        int curRow = this.getRow();
        for(int y = curRow+1; y < 8; y++) {
            if(Chess.pieces[curColumn][y] != null) {
                if(Chess.pieces[curColumn][y].isWhite() == this.isWhite()) return y-1;
                return y;
            }
        }
        return 7;
    }

    @Override
    public boolean validMove(int column, int row) {
        int curColumn = this.getColumn();
        int curRow = this.getRow();

        if(curRow != row && curColumn != column) return false;

        //  Left-Right (-)
        if(curRow == row) {
            // Left
            if(curColumn > column) {
                int l = this.left();
                if(l <= column) return true;
            }
            // Right
            if(curColumn < column) {
                int r = this.right();
                if(r >= column) return true;
            }           
        }

        // Up-Down (|)
        if(curColumn == column) {
            // Up
            if(curRow > row) {
                int u = this.up();
                if(u <= row) return true;
            }
            //Down
            if(curRow < row) {
                int d = this.down();
                if(d >= row) return true;
            }
        }
        return false;
    }

    public boolean[][] getValidTiles() {
        boolean[][] arr = new boolean[8][8];
        int curColumn = this.getColumn();
        int curRow = this.getRow();

        for(int x = this.left(); x < curColumn; x++) arr[x][curRow] = true; //  LEFT

        for(int x = this.right(); x > curColumn; x--) arr[x][curRow] = true; // RIGHT

        for(int y = this.up(); y < curRow; y++) arr[curColumn][y] = true; // UP

        for(int y = this.down(); y > curRow; y--) arr[curColumn][y] = true; // DOWN
        
        return arr;
    }

    public void castle(boolean shortCastle) {
        if(shortCastle) {
            this.setColumn(5);
            Chess.pieces[7][this.getRow()] = null;
            Chess.pieces[5][this.getRow()] = this;
        }
        else {
            this.setColumn(3);
            Chess.pieces[0][this.getRow()] = null;
            Chess.pieces[3][this.getRow()] = this;
        }
        this.setCanCastle(false);
        this.remove();
        this.setLabelImage(this.getImageWithLabel());
        this.draw();
    }

}