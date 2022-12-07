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