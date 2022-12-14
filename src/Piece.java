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




    public Piece(int column, int row, boolean isWhite, String name, ImageIcon image) {
        this.setColumn(column);
        this.setRow(row);
        this.setWhite(isWhite);
        this.setName(name);
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

    private void resetPiece() {
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
                break;
        }


        if(Chess.pieces[column][row] != null && Chess.pieces[column][row].isWhite() != this.isWhite()) Chess.pieces[column][row].remove(); //take
        else if(Chess.pieces[column][row] != null && Chess.pieces[column][row].isWhite() == this.isWhite()) { //same color
            this.resetPiece();
            return;
        }

        // pawn promote
        if(this.getName() == "Pawn" && ((Pawn)this).canPromote() && (row == 0 || row == 7)) {
            JOptionPane theme_choice = new JOptionPane();
            theme_choice.setSize(384, 384);
            theme_choice.setVisible(true);
            
            Object[] options = {"Knight", "Bishop", "Rook", "Queen"};
            
            int promote = JOptionPane.showOptionDialog(Chess.frame, "Choose a figure to promote to", "Chess", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[3]);
            if(this.isWhite() && row == 0) {
                Chess.pieces[this.column][this.row] = null;
                this.remove();
                this.column = column;
                this.row = row;
                this.labelImage = this.getImageWithLabel();
                this.draw();
                Chess.pieces[column][row] = this;
                ((Pawn)this).promote(promote);
            }
            if(!this.isWhite() && row == 7) {
                Chess.pieces[this.column][this.row] = null;
                this.remove();
                this.column = column;
                this.row = row;
                this.labelImage = this.getImageWithLabel();
                this.draw();
                Chess.pieces[column][row] = this;
                ((Pawn)this).promote(promote);
            }
            return;
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
