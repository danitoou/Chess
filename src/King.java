import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.tinylog.Logger;

public class King extends Piece {
    private boolean canCastle = true;
    private boolean isChecked = false;

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean getChecked() {
        return this.isChecked;
    }

    public void setCanCastle(boolean canCastle) {
        this.canCastle = canCastle;
    }

    public boolean getCanCastle() {
        return this.canCastle;
    }

    public King(int column, int row, boolean isWhite) {
        super(column, row, isWhite, "King");
    }

    @Override
    public boolean validMove(int column, int row) {
        int curColumn = this.getColumn();
        int curRow = this.getRow();
        if(Math.abs(curColumn-column) <= 1 && Math.abs(curRow-row) <= 1) return true;
        // if(this.legalCastle() > 0)return true;
        return false;
    }

    public boolean validCastle(boolean shortCastle) {
        if(this.legalCastle() > 1 && !shortCastle) return true;
        if((this.legalCastle() == 1 || this.legalCastle() == 3) && shortCastle) return true;
        return false;
    }

    @Override
    public boolean[][] getValidTiles() {
        boolean[][] arr = new boolean[8][8];
        int curColumn = this.getColumn();
        int curRow = this.getRow();
        for(int x = -1; x <= 1; x++) {
            for(int y = -1; y <= 1; y++) {
                if(x == 0 && y == 0) continue;
                if(curColumn + x < 0 || curColumn + x > 7 || curRow + y < 0 || curRow + y > 7) continue;
                if(Chess.pieces[curColumn + x][curRow + y] != null && Chess.pieces[curColumn + x][curRow + y].isWhite() == this.isWhite()) continue;
                arr[curColumn + x][curRow + y] = true;
            }
        }
        return arr;
    }

    public int legalCastle() {
        int curRow = this.getRow();
        boolean isWhite = !this.isWhite();
        int output = 0;
        boolean shortAttacked = Chess.checkCheck(4, curRow, isWhite) || Chess.checkCheck(5, curRow, isWhite) || Chess.checkCheck(6, curRow, isWhite);
        boolean longAttacked = Chess.checkCheck(2, curRow, isWhite) || Chess.checkCheck(3, curRow, isWhite) || Chess.checkCheck(4, curRow, isWhite);
        //short castle
        if(Chess.pieces[7][curRow] != null && Chess.pieces[5][curRow] == null && Chess.pieces[6][curRow] == null && this.canCastle && ((Rook)Chess.pieces[7][curRow]).getCanCastle() && !shortAttacked) output += 1;
        //long castle
        if(Chess.pieces[0][curRow] != null && Chess.pieces[1][curRow] == null && Chess.pieces[2][curRow] == null && Chess.pieces[3][curRow] == null && this.canCastle && ((Rook)Chess.pieces[0][curRow]).getCanCastle() && !longAttacked) output += 2;
        return output;
    }

    public void castle(boolean shortCastle) {
        if(shortCastle) {
            this.setColumn(6);
            Chess.pieces[4][this.getRow()] = null;
            Chess.pieces[6][this.getRow()] = this;
            Logger.info("O-O");
            Chess.boardStockfish += String.format(" e%dg%d", 8-(char)this.getRow(), 8-(char)this.getRow());
        }
        else {
            this.setColumn(2);
            Chess.pieces[4][this.getRow()] = null;
            Chess.pieces[2][this.getRow()] = this;
            Logger.info("O-O-O");
            Chess.boardStockfish += String.format(" e%dc%d", 8-(char)this.getRow(), 8-(char)this.getRow());
        }
        // if(Chess.toPlay != Chess.stockfishColor && Chess.stockfishOn) {
        //     ProcessBuilder pb = new ProcessBuilder("stockfish\\stockfish-15.exe");
        //     pb.directory(new File("stockfish"));
        //     Thread t2 = new Thread(new Runnable() {
    
        //         @Override
        //         public void run() {
                    
        //             try {
        //                 Process proc = pb.start();
        //                 BufferedWriter out = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
        //                 BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
    
        //                 out.write(Chess.boardStockfish);
        //                 out.newLine();
        //                 out.write(String.format("go movetime %d", Chess.stockfishTime));
        //                 out.newLine();
        //                 out.flush();
        //                 String text;
        //                 while((text = in.readLine()) != null) {
        //                     // System.out.println(text);
        //                     Chess.bestMove = text;
        //                 }
        //             } catch (Exception e) {
        //                 e.printStackTrace();
        //             } 
        //         }
                
        //     });
        //     t2.setPriority(Thread.MIN_PRIORITY);
        //     t2.start();
    
        //     Thread t3 = new Thread(new Runnable() {
    
        //         @Override
        //         public void run() {
        //             try {
        //                 Thread.sleep(Chess.stockfishTime+500);
        //             } catch (InterruptedException e) {
        //                 e.printStackTrace();
        //             }
        //             System.out.println(Chess.boardStockfish);
        //             String[] nextMove = Chess.stringToMove(Chess.bestMove).split(" ");
        //             Chess.pieces[Integer.parseInt(nextMove[0])][8-Integer.parseInt(nextMove[1])].move(Integer.parseInt(nextMove[2]), 8-Integer.parseInt(nextMove[3]));
        //         }
                
        //     });
        //     t3.setPriority(Thread.MIN_PRIORITY);
        //     t3.start();
        // }
        
        Chess.moveCount++;
        this.setCanCastle(false);
        this.remove();
        this.setLabelImage(this.getImageWithLabel());
        this.draw();
        if(this.isWhite()) {
            Chess.White_King = this; 
        } else {
            Chess.Black_King = this;
        }
        AudioInputStream audioInputStream;
        String moveSound = "src\\sounds\\castle.wav";
        
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(moveSound));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        
        Chess.toPlay = !Chess.toPlay;
    }

    
}