import java.util.List;
public class Main {
    public static List<Transaction> tranzakcje = FileHandler.getAllTransactions();
    public static void main(String[] args){
        Count zegar = new Count();
        zegar.start();
        Interface.createWindow();
    }
}
