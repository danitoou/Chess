import java.awt.event.MouseEvent;
import java.awt.*;

import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.*;



public class Chess extends JFrame{

    public static JFrame frame = new JFrame("Chess");
    public static Piece[][] pieces = new Piece[8][8];
    public static Piece currentPiece;
    public static Piece copyPiece = currentPiece;
    public static String theme;


    public static void main(String[] args) {

        JOptionPane theme_choice = new JOptionPane();
        theme_choice.setSize(384, 384);
        theme_choice.setVisible(true);
        
        Object[] options = {"Chess.com", "Lichess"};
        
        int theme_option = JOptionPane.showOptionDialog(frame, "Choose a chess theme", "Chess", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

        if(theme_option == 0) theme = "Chesscom\\";
        else theme = "Lichess\\";
        
        frame = new JFrame("Chess");
        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1024, 1024);

        frame.setLocationRelativeTo(null);
        frame.setLayout(null);

// board
        ImageIcon boardImage = new ImageIcon("src\\images\\" + theme + "Board.png");
        JLabel board = new JLabel();
        board.setIcon(new ImageIcon(boardImage.getImage().getScaledInstance(1024, 1024, Image.SCALE_SMOOTH)));
        board.setBounds(0, 0, 1024, 1024);
        frame.setContentPane(board);
        
        

        for(int y = 1; y < 8; y += 5) {
            for(int x = 0; x < 8; x++) {
                if(y == 1) pieces[x][y] = Pawn.make(x, y, false);
                else pieces[x][y] = Pawn.make(x, y, true);
                pieces[x][y].draw();
            }
        }

// black pieces
        pieces[0][0] = Rook.make(0, 0, false);
        pieces[0][0].draw();
        pieces[1][0] = Knight.make(1, 0, false);
        pieces[1][0].draw();
        pieces[2][0] = Bishop.make(2, 0, false);
        pieces[2][0].draw();
        pieces[3][0] = King.make(3, 0, false);
        pieces[3][0].draw();
        pieces[4][0] = Queen.make(4, 0, false);
        pieces[4][0].draw();
        pieces[5][0] = Bishop.make(5, 0, false);
        pieces[5][0].draw();
        pieces[6][0] = Knight.make(6, 0, false);
        pieces[6][0].draw();
        pieces[7][0] = Rook.make(7, 0, false);
        pieces[7][0].draw();

// white pieces
        pieces[0][7] = Rook.make(0, 7, true);
        pieces[0][7].draw();
        pieces[1][7] = Knight.make(1, 7, true);
        pieces[1][7].draw();
        pieces[2][7] = Bishop.make(2, 7, true);
        pieces[2][7].draw();
        pieces[3][7] = King.make(3, 7, true);
        pieces[3][7].draw();
        pieces[4][7] = Queen.make(4, 7, true);
        pieces[4][7].draw();
        pieces[5][7] = Bishop.make(5, 7, true);
        pieces[5][7].draw();
        pieces[6][7] = Knight.make(6, 7, true);
        pieces[6][7].draw();
        pieces[7][7] = Rook.make(7, 7, true);
        pieces[7][7].draw();

// board
        JPanel[][] panel_board = new JPanel[8][8];
        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                JPanel panel = new JPanel();
                panel.setBounds(128*x, 128*y, 128, 128);
                if(theme == "Chesscom") {
                    if((x+y) % 2 == 0) panel.setBackground(new Color(125, 148, 93, 255));
                    else panel.setBackground(new Color(238, 238, 213, 255));
                } else {
                    if((x+y) % 2 == 0) panel.setBackground(new Color(125, 148, 93, 255));
                    else panel.setBackground(new Color(238, 238, 213, 255));
                }
                // frame.add(panel);
                panel_board[x][y] = panel;
                
            }
        }

        ImageIcon greenDotPicture = new ImageIcon("src\\images\\Green_Dot.png");
        JLabel greenDot = new JLabel();
        greenDot.setIcon(new ImageIcon(greenDotPicture.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH)));
        

        // az sam dani
        frame.repaint();
        JLabel[][] dots = new JLabel[8][8];
        
        frame.addMouseListener(new MouseInputListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(pieces[e.getX()/128][e.getY()/128] == null) return;
                currentPiece = pieces[e.getX()/128][e.getY()/128];
                copyPiece = currentPiece;
                frame.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(currentPiece == null) return;
                int curX = e.getX();
                int curY = e.getY();
                if(Chess.pieces[curX/128][curY/128] != null && Chess.pieces[curX/128][curY/128].isWhite() == currentPiece.isWhite()) {
                    currentPiece.move(copyPiece.getColumn(), copyPiece.getRow());
                    // currentPiece.remove();
                    // currentPiece = copyPiece;
                    // currentPiece.draw();
                    // frame.repaint();
                    return;
                } 
                currentPiece.move(curX/128, curY/128);
                currentPiece = null; 
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) { 
            }

            @Override
            public void mouseDragged(MouseEvent arg0) {                
            }
        });

        frame.addMouseMotionListener(new MouseInputAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // System.out.println("Drag");
                if(currentPiece == null) return;
                currentPiece.movePixel(e.getX()-64, e.getY()-64);
                frame.repaint();
            }
        });
        
        
        

        

    }
}
