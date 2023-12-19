import java.util.List;
public class Main {
    public static List<Transaction> transactions = FileHandler.getAllTransactions();
    public static void main(String[] args){
        Count timer = new Count();
        timer.start();
        Interface.createWindow();
    }
}
