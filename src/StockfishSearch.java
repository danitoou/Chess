import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class StockfishSearch implements Runnable{
    private Thread t;
    private String threadName;

    public StockfishSearch() {
        this.threadName = "Stockfish Search";
        System.out.println("Creating " + this.threadName);
    }

    public void run() {
        System.out.println("Running " + this.threadName);
        try {
            ProcessBuilder pb = new ProcessBuilder("stockfish\\stockfish-15.exe");
            pb.directory(new File("stockfish"));
            Process proc = pb.start();
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            out.write("position startpos");
            out.newLine();
            out.write(String.format("go movetime %d", Chess.stockfishTime));
            out.newLine();
            out.flush();

            String text;
            while((text = in.readLine()) != null) {
                System.out.println(text);
                Chess.bestMove = text;
            }
            System.out.println("test zdr");
            Thread.sleep(1500);


            String[] nextMove = Chess.stringToMove(Chess.bestMove).split(" ");
            Chess.pieces[Integer.parseInt(nextMove[0])][8-Integer.parseInt(nextMove[1])].move(Integer.parseInt(nextMove[2]), 8-Integer.parseInt(nextMove[3]));


            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("Thread " +  this.threadName + " interrupted.");
        }
        System.out.println("Thread " +  this.threadName + " exiting.");
    }

    public void start () {
        System.out.println("Starting " +  this.threadName );
        if (this.t == null) {
           this.t = new Thread(this, this.threadName);
           this.t.start();
        }
     }


}
