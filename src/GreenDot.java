import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Image;

public class GreenDot {
    private int column;
    private int row;
    private ImageIcon image = new ImageIcon("src\\images\\Green_Dot.png");
    private JLabel labelImage = new JLabel();

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public JLabel getLabelImage() {
        return labelImage;
    }

    public GreenDot(int column, int row) {
        this.column = column;
        this.row = row;
        this.labelImage.setIcon(new ImageIcon(this.image.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        this.labelImage.setBounds(column*128 + 44, row*128 + 44, 40, 40);
    }

    
}