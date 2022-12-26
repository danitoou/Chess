import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Image;

public class GreenDot {
    private int column;
    private int row;
    private ImageIcon image = new ImageIcon("src\\images\\Green_Dot.png");
    private JLabel labelImage = new JLabel();



    public JLabel getLabelImage() {
        return labelImage;
    }

    public GreenDot(int column, int row) {
        this.column = column;
        this.row = row;
        this.labelImage.setIcon(new ImageIcon(this.image.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH)));
        this.labelImage.setBounds(column*128, row*128, 128, 128);
    }

    
}