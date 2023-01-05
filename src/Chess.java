import java.awt.Image;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;



public class Chess extends JFrame{

    public static JFrame frame = new JFrame("Chess");
    public static Piece[][] pieces = new Piece[8][8];
    public static Piece currentPiece;
    public static Piece copyPiece = currentPiece;
    public static String theme = "Lichess\\";
    public static King White_King = new King(4, 7, true);
    public static King Black_King = new King(4, 0, false);
    public static Piece[][] pieces_copy = new Piece[8][8];
    public static Piece p;
    public static Piece previousPiece;
    
    public static boolean checkMate(boolean isWhite) {
        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                if(pieces[x][y] == null || pieces[x][y].isWhite() != isWhite) continue;
                boolean[][] validTiles = pieces[x][y].getValidTiles();
                if(validTiles == null) continue;
                for(int a = 0; a < 8; a++) {
                    for(int b = 0; b < 8; b++) {
                        if(validTiles[a][b] && pieces[x][y].legalMove(a, b)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    public static boolean checkCheck(int column, int row, boolean isWhite) {
        // check for knight
        int[] arr = {-2, -1, 1, 2};
        for (int i : arr) {
            for (int j : arr) {
                if((column + i + row + j) % 2 != (column + row) % 2) {
                    try {
                        Piece k = Chess.pieces_copy[column+i][row+j]; 
                        if(k != null && k.getName() == "Knight" && k.isWhite() == isWhite) return true;
                    } catch(IndexOutOfBoundsException e) {
                        continue;
                    }
                    
                }
            }
        }

        // <- LEFT
        Piece p;
        for(int x = column-1; x >= 0; x--) {
            p = Chess.pieces_copy[x][row];
            if(p != null) {
                if((p.getName() == "Rook" || p.getName() == "Queen") && p.isWhite() == isWhite) return true;
                else break;
            }
        }
        // -> RIGHT
        for(int x = column+1; x < 8; x++) {
            p = Chess.pieces_copy[x][row];
            if(p != null) {
                if((p.getName() == "Rook" || p.getName() == "Queen") && p.isWhite() == isWhite) return true;
                else break;
            }
        }
        // ^ UP
        for(int y = row-1; y >= 0; y--) {
            p = Chess.pieces_copy[column][y];
            if(p != null) {
                if((p.getName() == "Rook" || p.getName() == "Queen") && p.isWhite() == isWhite) return true;
                else break;
            }
        }
        // v DOWN
        for(int y = row+1; y < 8; y++) {
            p = Chess.pieces_copy[column][y];
            if(p != null) {
                if((p.getName() == "Rook" || p.getName() == "Queen") && p.isWhite() == isWhite) return true;
                else break;
            }
        }
        // down left
        for(int x = column-1; x >= 0; x--) {
            int y = row + column - x;
            if(y < 0 || y > 7) break;
            p = Chess.pieces_copy[x][y];
            if(p != null) {
                if((p.getName() == "Bishop" || p.getName() == "Queen") && p.isWhite() == isWhite) return true;
                else break;
            }
        }
        // up right
        for(int x = column+1; x < 8; x++) {
            int y = row + column - x;
            if(y < 0 || y > 7) break;
            p = Chess.pieces_copy[x][y];
            if(p != null) {
                if((p.getName() == "Bishop" || p.getName() == "Queen") && p.isWhite() == isWhite) return true;
                else break;
            }
        }
        //up left
        int y = row - 1;
        for(int x = column-1; x > 0; x--) {
            if(y < 0 || y > 7) break;
            p = Chess.pieces_copy[x][y];
            if(p != null) {
                if((p.getName() == "Bishop" || p.getName() == "Queen") && p.isWhite() == isWhite) return true;
                else break;
            }
            y--;
        }
        //down right
        y = row + 1;
        for(int x = column+1; x < 8; x++) {
            if(y < 0 || y > 7) break;
            p = Chess.pieces_copy[x][y];
            if(p != null) {
                if((p.getName() == "Bishop" || p.getName() == "Queen") && p.isWhite() == isWhite) return true;
                else break;
            }
            y++;
        }

        int pawnRow;
        if(isWhite) pawnRow = row+1;
        else pawnRow = row-1;

        try {
            p = Chess.pieces_copy[column-1][pawnRow];
            if(p.getName() == "Pawn" && p.isWhite() == isWhite) return true;
        } catch (Exception e) {}

        try {
            p = Chess.pieces_copy[column+1][pawnRow];
            if(p.getName() == "Pawn" && p.isWhite() == isWhite) return true;
        } catch (Exception e) {}
        
        return false;
    }

    


    public static void main(String[] args) {

        // JOptionPane theme_choice = new JOptionPane();
        // theme_choice.setSize(384, 384);
        // theme_choice.setVisible(true);
        
        // Object[] options = {"Chess.com", "Lichess"};
        
        // int theme_option = JOptionPane.showOptionDialog(frame, "Choose a chess theme", "Chess", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

        // if(theme_option == 0) theme = "Chesscom\\";
        // else theme = "Lichess\\";
        
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
                if(y == 1) pieces[x][y] = new Pawn(x, y, false);
                else pieces[x][y] = new Pawn(x, y, true);
                pieces[x][y].draw();
            }
        }

// black pieces
        pieces[0][0] = new Rook(0, 0, false);
        pieces[0][0].draw();
        pieces[1][0] = new Knight(1, 0, false);
        pieces[1][0].draw();
        pieces[2][0] = new Bishop(2, 0, false);
        pieces[2][0].draw();
        pieces[3][0] = new Queen(3, 0, false);
        pieces[3][0].draw();
        pieces[4][0] = new King(4, 0, false);
        pieces[4][0].draw();
        pieces[5][0] = new Bishop(5, 0, false);
        pieces[5][0].draw();
        pieces[6][0] = new Knight(6, 0, false);
        pieces[6][0].draw();
        pieces[7][0] = new Rook(7, 0, false);
        pieces[7][0].draw();

// white pieces
        pieces[0][7] = new Rook(0, 7, true);
        pieces[0][7].draw();
        pieces[1][7] = new Knight(1, 7, true);
        pieces[1][7].draw();
        pieces[2][7] = new Bishop(2, 7, true);
        pieces[2][7].draw();
        pieces[3][7] = new Queen(3, 7, true);
        pieces[3][7].draw();
        pieces[4][7] = new King(4, 7, true);
        pieces[4][7].draw();
        pieces[5][7] = new Bishop(5, 7, true);
        pieces[5][7].draw();
        pieces[6][7] = new Knight(6, 7, true);
        pieces[6][7].draw();
        pieces[7][7] = new Rook(7, 7, true);
        pieces[7][7].draw();

        pieces_copy = pieces.clone();

// board
        // JPanel[][] panel_board = new JPanel[8][8];
        // for(int x = 0; x < 8; x++) {
        //     for(int y = 0; y < 8; y++) {
        //         JPanel panel = new JPanel();
        //         panel.setBounds(128*x, 128*y, 128, 128);
        //         if(theme == "Chesscom") {
        //             if((x+y) % 2 == 0) panel.setBackground(new Color(125, 148, 93, 255));
        //             else panel.setBackground(new Color(238, 238, 213, 255));
        //         } else {
        //             if((x+y) % 2 == 0) panel.setBackground(new Color(125, 148, 93, 255));
        //             else panel.setBackground(new Color(238, 238, 213, 255));
        //         }
        //         frame.add(panel);
        //         panel_board[x][y] = panel;
                
        //     }
        // }


        ImageIcon darkGreenSquarePicture = new ImageIcon("src\\images\\Green_Square_Dark.png");
        JLabel darkGreenSquare = new JLabel();
        darkGreenSquare.setIcon(new ImageIcon(darkGreenSquarePicture.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH)));

        ImageIcon lightGreenSquarePicture = new ImageIcon("src\\images\\Green_Square_Light.png");
        JLabel lightGreenSquare = new JLabel();
        lightGreenSquare.setIcon(new ImageIcon(lightGreenSquarePicture.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH)));
        
        

        frame.repaint();
        JLabel[][] dots = new JLabel[8][8];
        
        frame.addMouseListener(new MouseInputListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = e.getX()/128;
                int row = e.getY()/128;
                currentPiece = pieces[column][row];
                for(int x = 0; x < 8; x++) {
                    for(int y = 0; y < 8; y++) {
                        if(dots[x][y] != null) {
                            frame.remove(dots[x][y]);
                            dots[x][y] = null;
                        }
                    }
                }
            
                boolean[][] dots_arr = currentPiece.getValidTiles();
                if((column + row) % 2 == 1) {
                    frame.remove(lightGreenSquare);
                    darkGreenSquare.setBounds(column * 128, row * 128, 128, 128);
                    frame.add(darkGreenSquare);
                } else {
                    frame.remove(darkGreenSquare);
                    lightGreenSquare.setBounds(column * 128, row * 128, 128, 128);
                    frame.add(lightGreenSquare);
                }
                
                
                for(int x = 0; x < 8; x++) {
                    for(int y = 0; y < 8; y++) {
                        if(dots_arr[x][y] && currentPiece.legalMove(x, y)) {
                            dots[x][y] = new GreenDot(x, y).getLabelImage();
                            frame.add(dots[x][y]);
                        }
                    }
                }

                previousPiece = currentPiece;
                frame.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                int column = e.getX()/128;
                int row = e.getY()/128;
                if(pieces[column][row] == null) return;
                currentPiece = pieces[column][row];
                copyPiece = currentPiece;
                if(previousPiece != currentPiece) {
                    for(int x = 0; x < 8; x++) {
                        for(int y = 0; y < 8; y++) {
                            if(dots[x][y] != null) {
                                frame.remove(dots[x][y]);
                                dots[x][y] = null;
                            }
                        }
                    }
                    frame.remove(lightGreenSquare);
                    frame.remove(darkGreenSquare);
                }
                
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
                if(curX < 0 || curX > 1024 || curY < 0 || curY > 1024) {
                    currentPiece.resetPiece();
                    return;
                }
                if(Chess.pieces[curX/128][curY/128] != null && Chess.pieces[curX/128][curY/128].isWhite() == currentPiece.isWhite() && Chess.pieces[curX/128][curY/128].getName() != "Rook") {
                    // currentPiece.move(copyPiece.getColumn(), copyPiece.getRow());
                    currentPiece.resetPiece();
                    return;
                } 
                currentPiece.move(curX/128, curY/128);
                currentPiece = null;
                pieces_copy = pieces.clone();
                frame.repaint();
                
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
