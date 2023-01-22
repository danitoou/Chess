public class StockfishMove implements Runnable{
    private Thread t;
    private String threadName;

    public StockfishMove() {
        this.threadName = "Stockfish Move";
        System.out.println("Creating " + this.threadName);
    }

    public void run() {
        System.out.println("Running " + this.threadName);
        try {
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
