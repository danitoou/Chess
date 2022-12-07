import javax.swing.ImageIcon;

public class King extends Piece {
    private boolean canCastle = true;

    public void setCanCastle(boolean canCastle) {
        this.canCastle = canCastle;
    }

    public boolean getCanCastle() {
        return this.canCastle;
    }

    public King(int column, int row, boolean isWhite, ImageIcon image) {
        super(column, row, isWhite, "King", image);
    }

    public static King make(int column, int row, boolean isWhite) {
        ImageIcon image = new ImageIcon("src\\images\\" + Chess.theme + "White_King.png");
        if(!isWhite) {
            image = new ImageIcon("src\\images\\" + Chess.theme + "Black_King.png");
        }
        return new King(column, row, isWhite, image);
    }

    public boolean validMove(int column, int row) {
        int curColumn = this.getColumn();
        int curRow = this.getRow();
        if(Math.abs(curColumn-column) <= 1 && Math.abs(curRow-row) <= 1) return true;
        if(this.legalCastle() > 0) return true;
        // if(this.legalCastle() > 1 && curColumn > column) return true;
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

    public int legalCastle() {
        int curRow = this.getRow();
        int output = 0;
        //short castle
        if(Chess.pieces[5][curRow] == null && Chess.pieces[6][curRow] == null && this.canCastle && ((Rook)Chess.pieces[7][curRow]).getCanCastle()) output += 1;
        if(Chess.pieces[1][curRow] == null && Chess.pieces[2][curRow] == null && Chess.pieces[3][curRow] == null && this.canCastle && ((Rook)Chess.pieces[0][curRow]).getCanCastle()) output += 2;
        return output;
    }

    public void castle(boolean shortCastle) {
        this.remove();
        this.setCanCastle(false);
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
        this.draw();
    }

    
}