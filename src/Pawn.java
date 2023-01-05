
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

    private boolean whitePawn(int column, int row) {
        int curColumn = this.getColumn();
        int curRow = this.getRow();
        // Piece p = Chess.pieces[column][row];

        if(curRow-1 == row && curColumn == column && Chess.pieces[column][row] == null) return true;
        if(this.firstMove && curRow-2 == row && curColumn == column && Chess.pieces[column][row] == null && Chess.pieces[column][row+1] == null) return true;
        else if(curRow-1 == row && column >= 0 && row >= 0 && column < 8 && row < 8) {
            if(curColumn-1 == column && Chess.pieces[column][row] != null && Chess.pieces[column][row].isWhite() != this.isWhite()) return true;
            if(curColumn+1 == column && Chess.pieces[column][row] != null && Chess.pieces[column][row].isWhite() != this.isWhite()) return true;
        } 
        
    
        return false;
    }

    private boolean blackPawn(int column, int row) {
        int curColumn = this.getColumn();
        int curRow = this.getRow();
        // Piece p = Chess.pieces[column][row];
        
        if(curRow+1 == row && curColumn == column && Chess.pieces[column][row] == null) return true;
        if(this.firstMove && curRow+2 == row && curColumn == column && Chess.pieces[column][row] == null && Chess.pieces[column][row-1] == null) return true;
        else if(curRow+1 == row && column >= 0 && row >= 0 && column < 8 && row < 8) {
            if(curColumn-1 == column && Chess.pieces[column][row] != null && Chess.pieces[column][row].isWhite() != this.isWhite()) return true;
            if(curColumn+1 == column && Chess.pieces[column][row] != null && Chess.pieces[column][row].isWhite() != this.isWhite()) return true;
        }

        

        return false;
    }

    @Override
    public boolean validMove(int column, int row) {
        if(this.isWhite()) return this.whitePawn(column, row);
        else return this.blackPawn(column, row);
    }

    @Override
    public boolean[][] getValidTiles() {
        boolean[][] arr = new boolean[8][8];
        int curColumn = this.getColumn();
        int curRow = this.getRow();
        for(int x = -1; x <= 1; x++) {
            for(int y = -2; y <= 2; y++) {
                if(x == 0 && y == 0) continue;
                if(this.validMove(curColumn+x, curRow+y)) arr[curColumn+x][curRow+y] = true;
            }
        }
        return arr;
    }

    public boolean canPromote() {
        if(this.isWhite()) {
            if(this.getRow() == 0) return true;
        }
        else {
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