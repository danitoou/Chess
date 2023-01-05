import java.awt.Image;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

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
        // checks if the piece moves like that (doesn't consider check)
        // resets the piece if it can't move
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

                // normal move for king
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
        if(Chess.lastMove != null) {
            String[] prevMoveString = Chess.lastMove.split("_");
            // String prevPiece = prevMoveString[0];
            String tempColor = prevMoveString[1];
            boolean prevColor = false;
            if(tempColor == "White") prevColor = true;
            if(this.getName() == "Pawn" && prevColor != this.isWhite() && prevMoveString.length == 3) { //en passant
            
            }
        }
        
        
        boolean takes = false; // for sound
        if(Chess.pieces[column][row] != null && Chess.pieces[column][row].isWhite() != this.isWhite()) { //takes
            Chess.pieces[column][row].remove();
            takes = true;

        }
        else if(Chess.pieces[column][row] != null && Chess.pieces[column][row].isWhite() == this.isWhite()) { //same color
            this.resetPiece();
            return;
        }

        if(!this.legalMove(column, row)) { // check if own king is checked, resets if it is
            // if(this.name == "King") {
            //     this.resetPiece();
            //     Chess.pieces_copy[this.column][this.row] = this;
            //     Chess.pieces_copy[column][row] = Chess.p;
            //     if(Chess.p != null) {
            //         Chess.p.draw();
            //     }
            // } else this.resetChecked(Chess.p, column, row);
            
            this.resetChecked(Chess.p, column, row);

            
        
            return;
        }

        String temp = "_Black";
        if(this.isWhite) temp = "_White";
        if(this.getName() == "Pawn" && Math.abs(this.row - row) == 2) {
            temp += "_Double";
        }

        Chess.lastMove = this.getName() + temp;

// changes piece variables and redraws the correct image
// actually moves the piece
        Chess.pieces[this.column][this.row] = null;
        this.remove();
        this.column = column;
        this.row = row;
        this.labelImage = this.getImageWithLabel();
        this.draw();
        Chess.pieces[column][row] = this;
////////////////////////////////////////////////////////
// other king is checked

        boolean black_check = Chess.checkCheck(Chess.Black_King.getColumn(), Chess.Black_King.getRow(), true);
        boolean white_check = Chess.checkCheck(Chess.White_King.getColumn(), Chess.White_King.getRow(), false);
    
        ImageIcon checkPicture = new ImageIcon("src\\images\\Check_Dot.png");
        JLabel checkDot = new JLabel();
        checkDot.setIcon(new ImageIcon(checkPicture.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH)));

        if(this.isWhite) Chess.Black_King.setChecked(black_check);
        else Chess.White_King.setChecked(white_check);
        
        // if(black_check || white_check) System.out.println("Shah bate.");


        if(black_check) {
            checkDot.setBounds(Chess.Black_King.getColumn()*128, Chess.Black_King.getRow()*128, 128, 128);
            Chess.frame.add(checkDot);
        } else if(white_check) {
            checkDot.setBounds(Chess.White_King.getColumn()*128, Chess.White_King.getRow()*128 + 32, 128, 128);
            Chess.frame.add(checkDot);
        } else Chess.frame.remove(checkDot);
// checkmate

        boolean black_mate = Chess.checkMate(false);
        boolean white_mate = Chess.checkMate(true);


        if(black_check && black_mate) System.out.println("Shah i mat bate. Beliqt pecheli");
        else if(white_check && white_mate) System.out.println("Shah i mat bate. Cherniqt pecheli");

// stalemate

        if(!black_check && black_mate) System.out.println("Pat bate.");
        else if(!white_check && white_mate) System.out.println("Pat bate.");

// plays sound
        // AudioInputStream audioInputStream;
        // String moveSound = "src\\sounds\\move.wav";
        // if(takes) moveSound = "src\\sounds\\capture.wav";
        // if(black_check || white_check) moveSound = "src\\sounds\\check.wav";
        
        // try {
        //     audioInputStream = AudioSystem.getAudioInputStream(new File(moveSound));
        //     Clip clip = AudioSystem.getClip();
        //     clip.open(audioInputStream);
        //     clip.start();
        //     clip.close();
        // } catch (Exception e2) {
        //     e2.printStackTrace();
        // }

        

// sets pawn firstmove after its first move
        if(this.getName() == "Pawn" && ((Pawn)this).getFirstMove() == true) ((Pawn)this).setFirstMove(false);

// pawn promotion
        if(this.getName() == "Pawn" && ((Pawn)this).canPromote() && (row == 0 || row == 7)) {
            JOptionPane theme_choice = new JOptionPane();
            theme_choice.setSize(384, 384);
            theme_choice.setVisible(true);
            
            Object[] options = {"Knight", "Bishop", "Rook", "Queen"};
            
            int promote = JOptionPane.showOptionDialog(Chess.frame, "Choose a figure to promote to", "Chess", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if(this.isWhite && row == 0) {
                ((Pawn)this).promote(promote);
            }
            if(!this.isWhite && row == 7) {
                ((Pawn)this).promote(promote);
            }
            return;
        }
        
    }

    public boolean legalMove(int column, int row) {
        Chess.p = Chess.pieces_copy[column][row];
        // Piece copy = Chess.pieces_copy[this.column][this.row];
        Chess.pieces_copy[this.column][this.row] = null;
        Chess.pieces_copy[column][row] = this;

        // if moving king sets the new "fake" king position
        if(this.name == "King") {
            if(this.isWhite) Chess.White_King = (King)this;
            else Chess.Black_King = (King)this;
        }

        // if moving king checks if it's legal
        if(this.name == "King" && Chess.checkCheck(column, row, !this.isWhite())) {
            Chess.pieces_copy[this.column][this.row] = this;
            Chess.pieces_copy[column][row] = Chess.p;
            return false;
        }

        // moving anything else checks if it's legal
        if(this.isWhite && this.name != "King") { // moving a white piece
            if(Chess.checkCheck(Chess.White_King.getColumn(), Chess.White_King.getRow(), false)) {
                // this.resetChecked(p, column, row);
                Chess.pieces_copy[this.column][this.row] = this;
                Chess.pieces_copy[column][row] = Chess.p;
                return false;
            }
        } else if(this.name != "King"){ // moving a black piece
            if(Chess.checkCheck(Chess.Black_King.getColumn(), Chess.Black_King.getRow(), true)) {
                // this.resetChecked(p, column, row);
                Chess.pieces_copy[this.column][this.row] = this;
                Chess.pieces_copy[column][row] = Chess.p;
                return false;
            }
        }
        
        Chess.pieces_copy[this.column][this.row] = this;
        Chess.pieces_copy[column][row] = Chess.p;
        return true;
    }

    private void resetChecked(Piece p, int column, int row) {
        this.resetPiece();
        Chess.pieces_copy[this.column][this.row] = this;
        Chess.pieces_copy[column][row] = Chess.p;
        if(p != null) {
            p.draw();
        }
    }


    public boolean[][] getValidTiles() {
        return null;
    }

    public boolean validMove(int column, int row) {
        return true;
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
