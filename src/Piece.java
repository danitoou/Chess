import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.tinylog.Logger;


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
        picture.setIcon(new ImageIcon(this.image.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH)));
        picture.setBounds(column*64, row*64, 64, 64);
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
                    if(!((King)this).validMove(column, row) || Chess.checkCheck(column, row, !this.isWhite())) {
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
            
            default:
                if(!this.validMove(column, row)) {
                    this.resetPiece();
                    return;
                }
                break;
                
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

        if(this.getName() == "Pawn") {
            if(((Pawn)this).getFirstMove() == true) {
                if(Math.abs(this.getRow() - row) == 2) {
                    ((Pawn)this).setEnPassant(true);
                } else {
                    ((Pawn)this).setEnPassant(false);
                }
            } else {
                ((Pawn)this).setEnPassant(false);
            }
            
            if(Math.abs(this.row - row) == 1 && Math.abs(this.column - column) == 1 && Chess.pieces[column][row] == null) {
                if(this.row > row) {
                    Chess.pieces[column][row+1].remove();
                    Chess.pieces[column][row+1] = null;
                } else {
                    Chess.pieces[column][row-1].remove();
                    Chess.pieces[column][row-1] = null;
                }
            }
        }
        
// logging move
        Logger.info(this.logPGN(column, row, takes));


        if(Chess.boardStockfish == "position startpos") Chess.boardStockfish += " moves";
        Chess.boardStockfish += " " + this.moveToString(column, row);
        // System.out.println(Chess.boardStockfish);

//         if(Chess.toPlay != Chess.stockfishColor && Chess.stockfishOn) {
//             ProcessBuilder pb = new ProcessBuilder("stockfish\\stockfish-15.exe");
//             pb.directory(new File("stockfish"));
// // finds best move
//             Thread t2 = new Thread(new Runnable() {
    
//                 @Override
//                 public void run() {
                    
//                     try {
//                         Process proc = pb.start();
//                         BufferedWriter out = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
//                         BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
    
//                         out.write(Chess.boardStockfish);
//                         out.newLine();
//                         out.write(String.format("go movetime %d\n", Chess.stockfishTime));
//                         // out.newLine();
//                         out.flush();
//                         String text;
//                         while((text = in.readLine()) != null) {
//                             System.out.println(text);
//                             Chess.bestMove = text;
//                         }
//                     } catch (Exception e) {
//                         e.printStackTrace();
//                     } 
//                 }
                
//             });
//             t2.setPriority(Thread.MIN_PRIORITY);
//             t2.start();
//             // if(Chess.moveCount == 0) Chess.stockfishMove.start();
//             // else {
//             //     Chess.stockfish.run();
//             //     Chess.stockfishMove.run();
//             // }
// // plays best move
//             Thread t3 = new Thread(new Runnable() {
    
//                 @Override
//                 public void run() {
//                     try {
//                         Thread.sleep(Chess.stockfishTime+1000);
//                     } catch (InterruptedException e) {
//                         e.printStackTrace();
//                     }
//                     String[] nextMove = Chess.stringToMove(Chess.bestMove).split(" ");
//                     Chess.pieces[Integer.parseInt(nextMove[0])][8-Integer.parseInt(nextMove[1])].move(Integer.parseInt(nextMove[2]), 8-Integer.parseInt(nextMove[3]));
//                 }
                
//             });
//             t3.setPriority(Thread.MIN_PRIORITY);
//             t3.start();
//         }
        
        Chess.moveCount++;
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
    
        // ImageIcon checkPicture = new ImageIcon("src\\images\\Check_Dot.png");
        // JLabel checkDot = new JLabel();
        // checkDot.setIcon(new ImageIcon(checkPicture.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH)));

        if(this.isWhite) Chess.Black_King.setChecked(black_check);
        else Chess.White_King.setChecked(white_check);
        
        // if(black_check || white_check) System.out.println("Shah bate.");


        // Chess.frame.remove(Chess.checkDot);
        if(black_check) {
            Chess.checkDot.setBounds(Chess.Black_King.getColumn()*128, Chess.Black_King.getRow()*128, 128, 128);
            Chess.frame.add(Chess.checkDot);
        } else if(white_check) {
            Chess.checkDot.setBounds(Chess.White_King.getColumn()*128, Chess.White_King.getRow()*128, 128, 128);
            Chess.frame.add(Chess.checkDot);
        } else Chess.frame.remove(Chess.checkDot);
        
// checkmate

        boolean black_mate = Chess.checkMate(false);
        boolean white_mate = Chess.checkMate(true);


        // if(black_check && black_mate) JOptionPane.showMessageDialog(Chess.frame, "Checkmate! \nWhite wins!", "Chess", 1);
        // else if(white_check && white_mate) System.out.println("Shah i mat bate. Cherniqt pecheli");

// stalemate

        // if(!black_check && black_mate) System.out.println("Pat bate.");
        // else if(!white_check && white_mate) System.out.println("Pat bate.");

// plays sound
        AudioInputStream audioInputStream;
        String moveSound = "src\\sounds\\move.wav";
        if(takes) moveSound = "src\\sounds\\capture.wav";
        if(black_check || white_check) moveSound = "src\\sounds\\check.wav";
        
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(moveSound));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e2) {
            e2.printStackTrace();
        }

// changes who plays next
        Chess.toPlay = !Chess.toPlay;

// checkmate
        if(black_check && black_mate) {
            Object[] options = {"Exit", "Restart", "Ok"};
            int what_doink = JOptionPane.showOptionDialog(Chess.frame, "Checkmate! \nWhite wins!", "Chess", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[2]);
            if(what_doink == 0) {
                System.exit(0);
            } else if(what_doink == 1) {
                Chess.resetBoard();
            }
        }

        else if(white_check && white_mate) {
            Object[] options = {"Exit", "Restart", "Ok"};
            int what_doink = JOptionPane.showOptionDialog(Chess.frame, "Checkmate! \nBlack wins!", "Chess", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[2]);
            if(what_doink == 0) {
                System.exit(0);
            } else if(what_doink == 1) {
                Chess.resetBoard();
            }
        }
        
// stalemate
        if(!black_check && black_mate) {
            Object[] options = {"Exit", "Restart", "Ok"};
            int what_doink = JOptionPane.showOptionDialog(Chess.frame, "Stalemate! It's a draw!", "Chess", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[2]);
            if(what_doink == 0) {
                System.exit(0);
            } else if(what_doink == 1) {
                Chess.resetBoard();
            }
        }
        else if(!white_check && white_mate) {
            Object[] options = {"Exit", "Restart", "Ok"};
            int what_doink = JOptionPane.showOptionDialog(Chess.frame, "Stalemate! It's a draw!", "Chess", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[2]);
            if(what_doink == 0) {
                System.exit(0);
            } else if(what_doink == 1) {
                Chess.resetBoard();
            }
        }

// sets pawn firstmove after its first move
        if(this.getName() == "Pawn" && ((Pawn)this).getFirstMove() == true) ((Pawn)this).setFirstMove(false);      

// pawn promotion
        if(this.getName() == "Pawn" && ((Pawn)this).canPromote()) {
            // JOptionPane theme_choice = new JOptionPane();
            // theme_choice.setSize(384, 384);
            // theme_choice.setVisible(true);
            
            Object[] options_prom = {"Knight", "Bishop", "Rook", "Queen"};
            
            int promote = JOptionPane.showOptionDialog(Chess.frame, "Choose a figure to promote to", "Chess", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options_prom, options_prom[3]);
            if(this.isWhite) {
                ((Pawn)this).promote(promote);
            }
            if(!this.isWhite) {
                ((Pawn)this).promote(promote);
            }

            black_check = Chess.checkCheck(Chess.Black_King.getColumn(), Chess.Black_King.getRow(), true);
            white_check = Chess.checkCheck(Chess.White_King.getColumn(), Chess.White_King.getRow(), false);

// checkmate

            black_mate = Chess.checkMate(false);
            white_mate = Chess.checkMate(true);


            if(black_check && black_mate) {
                Object[] options2 = {"Exit", "Restart", "Ok"};
                int what_doink = JOptionPane.showOptionDialog(Chess.frame, "Checkmate! \nWhite wins!", "Chess", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options2, options2[2]);
                if(what_doink == 0) {
                    System.exit(0);
                } else if(what_doink == 1) {
                    Chess.resetBoard();
                }
            }
            else if(white_check && white_mate) {
                Object[] options2 = {"Exit", "Restart", "Ok"};
                int what_doink = JOptionPane.showOptionDialog(Chess.frame, "Checkmate! \nBlack wins!", "Chess", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options2, options2[2]);
                if(what_doink == 0) {
                    System.exit(0);
                } else if(what_doink == 1) {
                    Chess.resetBoard();
                }
            }
        
// stalemate
            if(!black_check && black_mate) {
                Object[] options2 = {"Exit", "Restart", "Ok"};
                int what_doink = JOptionPane.showOptionDialog(Chess.frame, "Stalemate! It's a draw!", "Chess", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options2, options2[2]);
                if(what_doink == 0) {
                    System.exit(0);
                } else if(what_doink == 1) {
                    Chess.resetBoard();
                }
            }
            else if(!white_check && white_mate) {
                Object[] options2 = {"Exit", "Restart", "Ok"};
                int what_doink = JOptionPane.showOptionDialog(Chess.frame, "Stalemate! It's a draw!", "Chess", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options2, options2[2]);
                if(what_doink == 0) {
                    System.exit(0);
                } else if(what_doink == 1) {
                    Chess.resetBoard();
                }
            }


            if(takes) moveSound = "src\\sounds\\capture.wav";
            if(black_check || white_check) moveSound = "src\\sounds\\check.wav";
            try {
                audioInputStream = AudioSystem.getAudioInputStream(new File(moveSound));
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return;
        }
        
    }

    public boolean legalMove(int column, int row) {
        Chess.p = Chess.pieces_copy[column][row];
        //Piece copy = Chess.pieces_copy[this.column][this.row];
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

    public boolean getEnPassant() {
        return false;
    }

    private String moveToString(int column, int row) {
        String move = this.toString();
        move += (char)(97+column);
        move += 8 - row;
        return move;
    }

    private String logPGN(int column, int row, boolean takes) {
        String output = "";
        switch(this.getName()) {
            case "Knight":
                output += "N";
                break;
            case "Bishop":
                output += "B";
                break;
            case "Rook":
                output += "R";
                break;
            case "Queen":
                output += "Q";
                break;
            case "King":
                output += "K";
                break;
        }
        output += this.toString();
        if(takes) output += "x";
        output += (char)(97+column);
        output += 8 - row;
        return output;
    }

    public void movePixel(int x, int y) {
        this.labelImage.setLocation(x, y);
        this.draw();
    }

    @Override
    public String toString() {
        String output = "";
        // switch(this.getName()) {
        //     case "Knight":
        //         output += "N";
        //         break;
        //     case "Bishop":
        //         output += "B";
        //         break;
        //     case "Rook":
        //         output += "R";
        //         break;
        //     case "Queen":
        //         output += "Q";
        //         break;
        //     case "King":
        //         output += "K";
        //         break;
        // }
        output += (char)(97+column);
        output += 8 - row;
        // if(isWhite) return String.format("%s (%d, %d) %s", name, column, row, "White");
        // else return String.format("%s (%d, %d) %s", name, column, row, "Black");
        return output;
    }

    
}
