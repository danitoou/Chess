import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Image;

public class Piece {
    private int column;
    private int row;
    private boolean isWhite;
    private String name;
    private ImageIcon image;
    private JLabel labelImage;




    public Piece(int column, int row, boolean isWhite, String name) {
        this.setColumn(column);
        this.setRow(row);
        this.setWhite(isWhite);
        this.setName(name);
        ImageIcon image = new ImageIcon("src\\images\\" + Chess.theme + "White_" +  name + ".png");
        if(!isWhite) image = new ImageIcon("src\\images\\" + Chess.theme + "Black_" +  name + ".png");
        this.setImage(image);
        this.labelImage = this.getImageWithLabel();
    }

    public void draw() {
        Chess.frame.add(this.labelImage);
        Chess.frame.repaint();
    }
    public void remove() {
        Chess.frame.remove(this.labelImage);
        Chess.frame.repaint();
    }

    public ImageIcon getImage() {
        return image;
    }
    public JLabel getImageWithLabel() {
        JLabel picture = new JLabel();
        picture.setIcon(new ImageIcon(this.image.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH)));
        picture.setBounds(column*128, row*128, 128, 128);
        return picture;
    }
    public JLabel getLabelImage() {
        return labelImage;
    }
    public void setLabelImage(JLabel picture) {
        this.labelImage = picture;
    }
    public void setImage(ImageIcon image) {
        this.image = image;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isWhite() {
        return isWhite;
    }
    public void setWhite(boolean isWhite) {
        this.isWhite = isWhite;
    }
    public int getRow() {
        return row;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public int getColumn() {
        return column;
    }
    public void setColumn(int column) {
        this.column = column;
    }

    public void resetPiece() {
        this.remove();
        this.column = Chess.copyPiece.column;
        this.row = Chess.copyPiece.row;
        this.labelImage = this.getImageWithLabel();
        this.draw();
    }


    public void move(int column, int row) {
        switch(this.getName()) {
            case "Pawn":
                if(!((Pawn)this).validMove(column, row)) {
                    this.resetPiece();
                    return;
                }
                break;
            
            case "Knight":
                if(!((Knight)this).validMove(column, row)) {
                    this.resetPiece();
                    return;
                }
                break;
            
            case "Bishop":
                if(!((Bishop)this).validMove(column, row)) {
                    this.resetPiece();
                    return;
                }
                break;

            case "Rook":
                if(!((Rook)this).validMove(column, row)) {
                    this.resetPiece();
                    return;
                }
                break;

            case "Queen":
                if(!((Queen)this).validMove(column, row)) {
                    this.resetPiece();
                    return;
                }
                break;
    
            case "King":
                int legalCastle = ((King)this).legalCastle();

                //long castling on empty square
                if(((King)this).validCastle(false) && column == 2 && row == this.row) {
                    if(legalCastle > 1) {
                        ((King)this).castle(false);
                        ((Rook)Chess.pieces[0][row]).castle(false);
                    }
                    Chess.frame.repaint();
                    return;
                }

                //short castling on empty square
                if(((King)this).validCastle(true) && column == 6 && row == this.row) {
                    if(legalCastle == 1 || legalCastle == 3) {
                        ((King)this).castle(true);
                        ((Rook)Chess.pieces[7][row]).castle(true);
                    }
                    Chess.frame.repaint();
                    return;
                }

                //castling on rook
                if(Chess.pieces[column][row] == null) {
                    if(!((King)this).validMove(column, row)) {
                        this.resetPiece();
                        return;
                    } else {
                        ((King)this).setCanCastle(false);
                        break;
                    }
                } else { //castling on rook
                    if((Chess.pieces[column][row].getName() == "Rook" && legalCastle > 0)) {
                        //short castle
                        if(column > this.column && (legalCastle == 1 || legalCastle == 3)) {
                            ((King)this).castle(true);
                            ((Rook)Chess.pieces[7][row]).castle(true);
                            return;
                        }
                        //long castle
                        if(column < this.column && legalCastle > 1) {
                            ((King)this).castle(false);
                            ((Rook)Chess.pieces[0][row]).castle(false);
                            return;
                        }
                    }
                }

                if(!((King)this).validMove(column, row)) {
                    this.resetPiece();
                    return;
                }

                break;
        }


        if(Chess.pieces[column][row] != null && Chess.pieces[column][row].isWhite() != this.isWhite()) Chess.pieces[column][row].remove(); //take
        else if(Chess.pieces[column][row] != null && Chess.pieces[column][row].isWhite() == this.isWhite()) { //same color
            this.resetPiece();
            return;
        }

        if(this.name == "King") {
            Chess.pieces[this.column][this.row] = null;
            Chess.pieces[column][row] = this;
            
            if(this.isWhite) Chess.White_King = (King)this;
            else Chess.Black_King = (King)this;
        }

        Chess.pieces[this.column][this.row] = null;
        Chess.pieces[column][row] = this;
        if(this.isWhite && this.getName() == "King" && Chess.checkCheck(column, row, !this.isWhite())) {
            this.resetPiece();
            Chess.pieces[this.column][this.row] = this;
            Chess.pieces[column][row] = null;
            return;
        }
        if(this.isWhite) {
            if(Chess.checkCheck(Chess.White_King.getColumn(), Chess.White_King.getRow(), false)) {
                this.resetPiece();
                Chess.pieces[this.column][this.row] = this;
                Chess.pieces[column][row] = null;
                return;
            }
        } else {
            if(Chess.checkCheck(Chess.Black_King.getColumn(), Chess.Black_King.getRow(), true)) {
                this.resetPiece();
                Chess.pieces[this.column][this.row] = this;
                Chess.pieces[column][row] = null;
                return;
            }
        }

        

// changes piece variables and redraws the correct image
        Chess.pieces[this.column][this.row] = null;
        this.remove();
        this.column = column;
        this.row = row;
        this.labelImage = this.getImageWithLabel();
        this.draw();
        Chess.pieces[column][row] = this;

        


        if(this.getName() == "Pawn" && ((Pawn)this).getFirstMove() == true) ((Pawn)this).setFirstMove(false); // sets pawn firstmove after its first move
        // pawn promote
        if(this.getName() == "Pawn" && ((Pawn)this).canPromote() && (row == 0 || row == 7)) {
            JOptionPane theme_choice = new JOptionPane();
            theme_choice.setSize(384, 384);
            theme_choice.setVisible(true);
            
            Object[] options = {"Knight", "Bishop", "Rook", "Queen"};
            
            int promote = JOptionPane.showOptionDialog(Chess.frame, "Choose a figure to promote to", "Chess", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if(this.isWhite() && row == 0) {
                ((Pawn)this).promote(promote);
            }
            if(!this.isWhite() && row == 7) {
                ((Pawn)this).promote(promote);
            }
            return;
        }
    }


    // public boolean[][] getLegalTiles() {
    //     boolean[][] arr = new boolean[8][8];
    //     switch(this.getName()) {
    //         case "Pawn":
    //             for(int x = 0; x < 8; x++) {
    //                 for(int y = 0; y < 8; y++) {
    //                     if(((Pawn)this).validMove(x, y)) arr[x][y] = true;
    //                     else arr[x][y] = false;
    //                 }
    //             }
    //             return arr;
    //         case "Knight":
    //             for(int x = 0; x < 8; x++) {
    //                 for(int y = 0; y < 8; y++) {
    //                     if(((Knight)this).validMove(x, y)) arr[x][y] = true;
    //                     else arr[x][y] = false;
    //                 }
    //             }
    //             return arr;
    //         case "Bishop":
    //             for(int x = 0; x < 8; x++) {
    //                 for(int y = 0; y < 8; y++) {
    //                     if(((Bishop)this).validMove(x, y)) arr[x][y] = true;
    //                     else arr[x][y] = false;
    //                 }
    //             }
    //             return arr;
    //         case "Rook":
    //             for(int x = 0; x < 8; x++) {
    //                 for(int y = 0; y < 8; y++) {
    //                     if(((Rook)this).validMove(x, y)) arr[x][y] = true;
    //                     else arr[x][y] = false;
    //                 }
    //             }
    //             return arr;
    //         case "Queen":
    //             for(int x = 0; x < 8; x++) {
    //                 for(int y = 0; y < 8; y++) {
    //                     if(((Queen)this).validMove(x, y)) arr[x][y] = true;
    //                     else arr[x][y] = false;
    //                 }
    //             }
    //                 return arr;
    //         case "King":
    //             for(int x = 0; x < 8; x++) {
    //                 for(int y = 0; y < 8; y++) {
    //                     if(((King)this).validMove(x, y)) arr[x][y] = true;
    //                     else arr[x][y] = false;
    //                 }
    //             }
    //                 return arr;
    //     }
    //     return arr;
        
    // }

    public boolean validMove(int column, int row) {
        switch(this.getName()) {
            case "Pawn":
                return ((Pawn)this).validMove(column, row);
            case "Knight":
                return ((Knight)this).validMove(column, row);
            case "Bishop":
                return ((Bishop)this).validMove(column, row);
            case "Rook":
                return ((Rook)this).validMove(column, row);
            case "Queen":
                return ((Queen)this).validMove(column, row);
            case "King":
                return ((King)this).validMove(column, row);
        }
        return false;
    }

    public void movePixel(int x, int y) {
        this.labelImage.setLocation(x, y);
        this.draw();
    }

    public String toString() {
        if(isWhite) return String.format("%s (%d, %d) %s", name, column, row, "White");
        else return String.format("%s (%d, %d) %s", name, column, row, "Black");
        
    }

    
}
