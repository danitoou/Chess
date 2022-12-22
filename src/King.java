
public class King extends Piece {
    private boolean canCastle = true;

    public void setCanCastle(boolean canCastle) {
        this.canCastle = canCastle;
    }

    public boolean getCanCastle() {
        return this.canCastle;
    }

    public King(int column, int row, boolean isWhite) {
        super(column, row, isWhite, "King");
    }

    public boolean validMove(int column, int row) {
        int curColumn = this.getColumn();
        int curRow = this.getRow();
        if(Math.abs(curColumn-column) <= 1 && Math.abs(curRow-row) <= 1) return true;
        // if(this.legalCastle() > 0) return true;
        return false;
    }

    public boolean validCastle(boolean shortCastle) {
        if(this.legalCastle() > 1 && !shortCastle) return true;
        if((this.legalCastle() == 1 || this.legalCastle() == 3) && shortCastle) return true;
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

    public int legalCastle() {
        int curRow = this.getRow();
        boolean isWhite = !this.isWhite();
        int output = 0;
        boolean shortAttacked = Chess.checkCheck(4, curRow, isWhite) || Chess.checkCheck(5, curRow, isWhite) || Chess.checkCheck(6, curRow, isWhite);
        boolean longAttacked = Chess.checkCheck(2, curRow, isWhite) || Chess.checkCheck(3, curRow, isWhite) || Chess.checkCheck(4, curRow, isWhite);
        //short castle
        if(Chess.pieces[5][curRow] == null && Chess.pieces[6][curRow] == null && this.canCastle && ((Rook)Chess.pieces[7][curRow]).getCanCastle() && !shortAttacked) output += 1;
        //long castle
        if(Chess.pieces[1][curRow] == null && Chess.pieces[2][curRow] == null && Chess.pieces[3][curRow] == null && this.canCastle && ((Rook)Chess.pieces[0][curRow]).getCanCastle() && !longAttacked) output += 2;
        return output;
    }

    public void castle(boolean shortCastle) {
        if(shortCastle) {
            this.setColumn(6);
            Chess.pieces[4][this.getRow()] = null;
            Chess.pieces[6][this.getRow()] = this;
        }
        else {
            this.setColumn(2);
            Chess.pieces[4][this.getRow()] = null;
            Chess.pieces[2][this.getRow()] = this;
        }
        this.setCanCastle(false);
        this.remove();
        this.setLabelImage(this.getImageWithLabel());
        this.draw();
    }

    
}