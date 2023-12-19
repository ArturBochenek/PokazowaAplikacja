import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private final static String txtFilePath = System.getProperty("user.dir") + File.separator + "Transactions";
    private final static String CryptoListPath = System.getProperty("user.dir") + File.separator + "Cryptolist";
    public static void saveToFile(Transaction transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtFilePath, true))) {
            writer.write(transaction.getName() + "," + transaction.getQuantity() + "," + transaction.getInitialValue());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.transactions.add(transaction);
    }

    public static List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(txtFilePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0].trim();
                    double quantity = Double.parseDouble(parts[1].trim());
                    double initialValue = Double.parseDouble(parts[2].trim());

                    transactions.add(new Transaction(name, quantity, initialValue));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    public static void deleteTransaction(int index) throws IOException {
        List<Transaction> transactions = getAllTransactions();
        if (index >= 0 && index < transactions.size()) {
            transactions.remove(index);
            Main.transactions.remove(index);
            saveAllTransactions(transactions);
        }
        Interface.refreshTable();
    }

    private static void saveAllTransactions(List<Transaction> transactions) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtFilePath))) {
            for (Transaction transaction : transactions) {
                writer.write(transaction.getName() + "," + transaction.getQuantity() + "," + transaction.getInitialValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String[] LoadCryptoFromFile() {
        List<String> CryptoList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CryptoListPath))) {
            String linia;
            while ((linia = reader.readLine()) != null) {
                CryptoList.add(linia);
            }
        } catch (IOException ignored) {}
        String[] CryptoCurrencies = new String[CryptoList.size()];
        CryptoCurrencies = CryptoList.toArray(CryptoCurrencies);
        return CryptoCurrencies;
    }
}


