
public class Pawn extends Piece {
    private boolean firstMove = true;

    public void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }

    public boolean getFirstMove() {
        return firstMove;
    }

    public Pawn(int column, int row, boolean isWhite) {
        super(column, row, isWhite, "Pawn");
    }

    public boolean validMove(int column, int row) {
        int curColumn = this.getColumn();
        int curRow = this.getRow();
        if(this.isWhite()) {
            if(curRow-1 == row && curColumn == column && Chess.pieces[column][row] == null) return true;
            if(curRow-2 == row && curColumn == column && Chess.pieces[column][row] == null && Chess.pieces[column][row+1] == null && this.firstMove) return true;
            else if(curRow-1 == row) {
                if(curColumn-1 == column && Chess.pieces[column][row] != null) return true;
                if(curColumn+1 == column && Chess.pieces[column][row] != null) return true;
            }
        }
        if(!this.isWhite()) {
            if(curRow+1 == row && curColumn == column && Chess.pieces[column][row] == null) return true;
            if(curRow+2 == row && curColumn == column && Chess.pieces[column][row] == null && Chess.pieces[column][row-1] == null && this.firstMove) return true;
            else if(curRow+1 == row) {
                if(curColumn-1 == column && Chess.pieces[column][row] != null) return true;
                if(curColumn+1 == column && Chess.pieces[column][row] != null) return true;
            }
        }
        return false;
    }

    // public boolean[][] getLegalTiles() {
    //     boolean[][] arr = new boolean[8][8];
    //     for(int x = 0; x < 8; x++) {
    //         for(int y = 0; y < 8; y++) {
    //             if(this.validMove(x, y)) arr[x][y] = true;
    //             else arr[x][y] = false;
    //         }
    //     }
    //     return arr;
    // }

    public boolean canPromote() {
        if(this.isWhite()) {
            if(this.getRow() == 0) return true;
        }
        if(!this.isWhite()) {
            if(this.getRow() == 7) return true;
        }
        return false;
    }

    public void promote(int promote) {
        // 0 - knight
        // 1 - bishop
        // 2 - rook
        // 3 - queen
        switch(promote) {
            case 0:
                Chess.pieces[this.getColumn()][this.getRow()] = new Knight(this.getColumn(), this.getRow(), this.isWhite());
                this.remove();
                Chess.pieces[this.getColumn()][this.getRow()].draw();
                break;
            case 1:
                Chess.pieces[this.getColumn()][this.getRow()] = new Bishop(this.getColumn(), this.getRow(), this.isWhite());
                this.remove();
                Chess.pieces[this.getColumn()][this.getRow()].draw();
                break;
            case 2:
                Chess.pieces[this.getColumn()][this.getRow()] = new Rook(this.getColumn(), this.getRow(), this.isWhite());
                this.remove();
                Chess.pieces[this.getColumn()][this.getRow()].draw();
                break;
            case 3:
                Chess.pieces[this.getColumn()][this.getRow()] = new Queen(this.getColumn(), this.getRow(), this.isWhite());
                this.remove();
                Chess.pieces[this.getColumn()][this.getRow()].draw();
                break;
        }
    }

    
}