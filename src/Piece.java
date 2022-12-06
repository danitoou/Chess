import javax.swing.ImageIcon;
import javax.swing.JLabel;
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


    public void move(int column, int row) {
        switch(this.getName()) {
            case "Pawn":
                if(!((Pawn)this).validMove(column, row)) {
                    this.remove();
                    this.column = Chess.copyPiece.column;
                    this.row = Chess.copyPiece.row;
                    this.labelImage = this.getImageWithLabel();
                    this.draw();
                    return;
                }
                break;
            
            case "Knight":
                if(!((Knight)this).validMove(column, row)) {
                    this.remove();
                    this.column = Chess.copyPiece.column;
                    this.row = Chess.copyPiece.row;
                    this.labelImage = this.getImageWithLabel();
                    this.draw();
                    return;
                }
                break;
            
            case "Bishop":
                if(!((Bishop)this).validMove(column, row)) {
                    this.remove();
                    this.column = Chess.copyPiece.column;
                    this.row = Chess.copyPiece.row;
                    this.labelImage = this.getImageWithLabel();
                    this.draw();
                    return;
                }
                break;
            case "Rook":
                if(!((Rook)this).validMove(column, row)) {
                    this.remove();
                    this.column = Chess.copyPiece.column;
                    this.row = Chess.copyPiece.row;
                    this.labelImage = this.getImageWithLabel();
                    this.draw();
                    return;
                }
                break;
            case "Queen":
                if(!((Queen)this).validMove(column, row)) {
                    this.remove();
                    this.column = Chess.copyPiece.column;
                    this.row = Chess.copyPiece.row;
                    this.labelImage = this.getImageWithLabel();
                    this.draw();
                    return;
                }
                break;
            case "King":
                if(!((King)this).validMove(column, row)) {
                    this.remove();
                    this.column = Chess.copyPiece.column;
                    this.row = Chess.copyPiece.row;
                    this.labelImage = this.getImageWithLabel();
                    this.draw();
                    return;
                }
                break;
        }
        if(Chess.pieces[column][row] != null && Chess.pieces[column][row].isWhite() != this.isWhite()) Chess.pieces[column][row].remove();
        else if(Chess.pieces[column][row] != null && Chess.pieces[column][row].isWhite() == this.isWhite()) {
// moves piece where it was
            this.remove();
            this.column = Chess.copyPiece.column;
            this.row = Chess.copyPiece.row;
            this.labelImage = this.getImageWithLabel();
            this.draw();
            return;
        }
        Chess.pieces[this.column][this.row] = null;
        this.remove();
        this.column = column;
        this.row = row;
        this.labelImage = this.getImageWithLabel();
        this.draw();
        Chess.pieces[column][row] = this;
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
