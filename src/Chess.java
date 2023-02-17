import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.Process;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

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
    public static ImageIcon checkPicture = new ImageIcon("src\\images\\Check_Dot.png");
    public static JLabel checkDot = new JLabel();
    public static Piece previousPiece;
    public static boolean toPlay = true;
    public static String boardStockfish = "position startpos";
    public static String bestMove;
    public static Thread t1;
    public static boolean stockfishOn = true;
    public static boolean stockfishColor = false;
    public static int stockfishTime = 1000;
    public static int moveCount = 1;
    public static int size = 512;
    // public static StockfishSearch stockfish = new StockfishSearch();
    // public static StockfishMove stockfishMove = new StockfishMove();

    public static ImageIcon darkGreenSquarePicture = new ImageIcon("src\\images\\Green_Square_Dark.png");
    public static JLabel darkGreenSquare = new JLabel();

    public static ImageIcon lightGreenSquarePicture = new ImageIcon("src\\images\\Green_Square_Light.png");
    public static JLabel lightGreenSquare = new JLabel();
    
    public static JLabel[][] dots = new JLabel[8][8];

    public static void resetBoard() {

        White_King = new King(4, 7, true);
        Black_King = new King(4, 0, false);
        toPlay = true;
        currentPiece = null;
        copyPiece = null;
        previousPiece = null;
        moveCount = 0;
        boardStockfish = "position startpos";

// board wipe
        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                if(pieces[x][y] != null) {
                    pieces[x][y].remove();
                }
                pieces[x][y] = null;
            }
        }


// pawns
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
        frame.repaint();
    }

    public static boolean arrayContains(char[] arr, char num) {
        for(int j = 0; j < arr.length; j++) {
            if(num == arr[j]) return true;
        }
        return false;
    }
    public static Piece[][] boardFromFEN(String fen) {
        Piece[][] board = new Piece[8][8];

        String[] rows = fen.split("/");
        char[] nums = {'1', '2', '3', '4', '5', '6', '7', '8'};
        int curRow = 0;
        int curColumn = 0;
        for (String row : rows) {
            curColumn = 0;
            for(int i = 0; i < row.length(); i++) {
                // if(Arrays.stream(nums).anyMatch(row.charAt(i)::equals)) {
                //     for(int j = i;  j > 0; j--) {

                //     }
                // }
                if(arrayContains(nums, row.charAt(i))) {
                    for(int j = row.charAt(i); j > 0; j--) {
                        curColumn++;
                    }
                }
                
            }
            curRow++;
        }




        return null;
    }

    public static String stringToMove(String move) {
        String[] move_split = move.split(" ");
        char[] chars = move_split[1].toCharArray();
        // String[] strings = move2.split("");
        // System.out.println((int)chars[0]-97);
        // System.out.println(chars[1]);
        // System.out.println((int)chars[2]-97);
        // System.out.println(chars[3]);
     //    Piece p = Chess.pieces[((int)chars[0])-97][chars[1]];
        return String.format("%d %c %d %c", (int)chars[0]-97, chars[1], (int)chars[2]-97, chars[3]);
     }
    
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
        for(int x = column-1; x >= 0; x--) {
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
        // check for other king

        int[] king_arr = {-1, 0, 1};
        for (int i : king_arr) {
            for (int j : king_arr) {
                if(i == 0 && j == 0) continue;
                if(column + i < 0 || column + i > 7 || row + j < 0 || row + j > 7) continue;
                if(Chess.pieces_copy[column + i][row + j] != null && Chess.pieces_copy[column + i][row + j].isWhite() == isWhite && Chess.pieces_copy[column + i][row + j].getName() == "King") {
                    return true;
                }
            }
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
    
    public static void removeGreens(int column, int row) {
        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                if(dots[x][y] != null) {
                    frame.remove(dots[x][y]);
                    dots[x][y] = null;
                }
            }
        }
    }

    public static void drawGreens(int column, int row) {

        lightGreenSquare.setIcon(new ImageIcon(lightGreenSquarePicture.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH)));
        darkGreenSquare.setIcon(new ImageIcon(darkGreenSquarePicture.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH)));
        checkDot.setIcon(new ImageIcon(checkPicture.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH)));
    
        // int column = e.getX()/128;
        // int row = e.getY()/128;
        // currentPiece = pieces[column][row];
        if(currentPiece == null) return;
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
        
        if(currentPiece != previousPiece) {
            for(int x = 0; x < 8; x++) {
                for(int y = 0; y < 8; y++) {
                    if(dots_arr[x][y] && currentPiece.legalMove(x, y)) {
                        dots[x][y] = new GreenDot(x, y).getLabelImage();
                        frame.add(dots[x][y]);
                    }
                }
            }
        }

        previousPiece = currentPiece;
        frame.repaint();
    }
    


    public static void main(String[] args) throws IOException {

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
        frame.setSize(size, size);

        frame.setLocationRelativeTo(null);
        frame.setLayout(null);   
        
        

// board
        ImageIcon boardImage = new ImageIcon("res\\images\\" + theme + "Board.png");
        JLabel board = new JLabel();
        board.setIcon(new ImageIcon(boardImage.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH)));
        board.setBounds(0, 0, size, size);
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

        // stockfish.start();
        if(Chess.stockfishOn) {
            ProcessBuilder pb = new ProcessBuilder("stockfish\\stockfish-15.exe");
            pb.directory(new File("stockfish"));
            Process proc;
            BufferedWriter out;
            BufferedReader in;
            String text;
            proc = pb.start();
            out = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            text = in.readLine();
            
            t1 = new Thread(new Runnable() {
    
                @Override
                public void run() {
                    while(true) {
                        if(Chess.toPlay != Chess.stockfishColor) {
                            System.out.println("Pishka balatum");
                            continue;
                        }
                        try {
                            out.write(boardStockfish);
                            out.newLine();
                            out.write(String.format("go movetime %d", stockfishTime));
                            out.newLine();
                            out.flush();
                            Thread.sleep(stockfishTime + 1500);
                            String[] nextMove = Chess.stringToMove(Chess.bestMove).split(" ");
                            Chess.pieces[Integer.parseInt(nextMove[0])][8-Integer.parseInt(nextMove[1])].move(Integer.parseInt(nextMove[2]), 8-Integer.parseInt(nextMove[3]));
                            // System.out.println("zdr");
                        } catch (Exception e) {
                            System.out.println("kyp");
                            e.printStackTrace();
                        }
                    }
                    
                }
                
            });
            t1.start();

            Thread stockfishBestMove = new Thread(new Runnable() {

                @Override
                public void run() {
                    while(true) {
                        try {
                            bestMove = in.readLine();
                            System.out.println(bestMove);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        
                    }
                    
                }
                
            });
            stockfishBestMove.start();
            // Chess.pieces[4][6].move(4, 4); // plays e2e4 anyways
        }

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


        // ImageIcon darkGreenSquarePicture = new ImageIcon("src\\images\\Green_Square_Dark.png");
        // JLabel darkGreenSquare = new JLabel();
        // darkGreenSquare.setIcon(new ImageIcon(darkGreenSquarePicture.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH)));

        // ImageIcon lightGreenSquarePicture = new ImageIcon("src\\images\\Green_Square_Light.png");
        // JLabel lightGreenSquare = new JLabel();
        // lightGreenSquare.setIcon(new ImageIcon(lightGreenSquarePicture.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH)));
        
        // checkDot.setIcon(new ImageIcon(checkPicture.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH)));

        // JLabel[][] dots = new JLabel[8][8];
        frame.repaint();
        
        frame.addMouseListener(new MouseInputListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                int column = e.getX()/(size/8);
                int row = e.getY()/(size/8);
                if(pieces[column][row] == null) return;
                if(pieces[column][row].isWhite() != toPlay || (pieces[column][row].isWhite() == stockfishColor && stockfishOn)) return;
                currentPiece = pieces[column][row];
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
                if(curX < 0 || curX > size || curY < 0 || curY > size) {
                    currentPiece.resetPiece();
                    currentPiece = null;
                    return;
                }
                if(Chess.pieces[curX/(size/8)][curY/(size/8)] != null && Chess.pieces[curX/(size/8)][curY/(size/8)].isWhite() == currentPiece.isWhite() && Chess.pieces[curX/(size/8)][curY/(size/8)].getName() != "Rook") {
                    // currentPiece.move(copyPiece.getColumn(), copyPiece.getRow());
                    currentPiece.resetPiece();
                    currentPiece = null;
                    return;
                } 
                // removeGreens(currentPiece.getColumn(), currentPiece.getRow());
                currentPiece.move(curX/(size/8), curY/(size/8));
                // drawGreens(currentPiece.getColumn(), currentPiece.getRow());
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
                if(currentPiece.isWhite() != toPlay) return;
                currentPiece.movePixel(e.getX()-(size/16), e.getY()-(size/16));
                // if(currentPiece != previousPiece) {
                // drawGreens(copyPiece.getColumn(), copyPiece.getRow());
                // }
                frame.repaint();
            }
        });
        
        
        

        

    }
}
