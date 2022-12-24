
public class Knight extends Piece {

    public Knight(int column, int row, boolean isWhite) {
        super(column, row, isWhite, "Knight");
    }

    @Override
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

    @Override
    public boolean[][] getValidTiles() {
        boolean[][] arr = new boolean[8][8];
        int curColumn = this.getColumn();
        int curRow = this.getRow();
        int[] arr2 = {-2, -1, 1, 2};
        for (int i : arr2) {
            for (int j : arr2) {
                if((curColumn + i + curRow + j) % 2 != (curColumn + curRow) % 2 && curColumn+i > 0 && curRow+j > 0 && curColumn+i < 8 && curRow+j < 8) {
                    arr[curColumn+i][curRow+j] = true;
                }
            }
        }
        return arr;
    }

    
}