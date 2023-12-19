import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Count extends Thread {
    public static int seconds = 60;
    @Override
    public void run() {
        try {
            while (true) {
                if (seconds % 60 == 0) {
                    refreshPrices();
                }
                Interface.refreshTable();
                Thread.sleep(1000);
                seconds--;
                if (seconds == 0) {
                    seconds = 60;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void refreshPrices() {
        List<Transaction> transactions = Main.transactions;
        List<String> cryptocurrencyNames = transactions.stream()
                .map(Transaction::getName)
                .collect(Collectors.toList());

        try {
            List<Double> prices = Api.GetExchange(cryptocurrencyNames);

            if (!prices.isEmpty() && prices.size() == transactions.size()) {
                for (int i = 0; i < transactions.size(); i++) {
                    Transaction transaction = transactions.get(i);
                    double currentPrice = prices.get(i);
                    transaction.setCurrentvalue(currentPrice);
                }
                Interface.refreshTable();
            } else {
                System.out.println("Not compatible Currency");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
