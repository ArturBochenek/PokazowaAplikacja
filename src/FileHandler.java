import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private final static String txtFilePath = System.getProperty("user.dir") + File.separator + "Transactions";
    private final static String CryptoFilePath = System.getProperty("user.dir") + File.separator + "Cryptolist";
    public static void saveToFile(Transaction transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtFilePath, true))) {
            writer.write(transaction.getName() + "," + transaction.getQuantity() + "," + transaction.getInitialValue());
            writer.newLine();
            System.out.println("Pomyślnie zapisano obiekt do pliku tekstowego.");
        } catch (IOException e) {
            System.out.println("Błąd podczas zapisywania do pliku tekstowego.");
            e.printStackTrace();
        }
        Main.tranzakcje.add(transaction);
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
            System.out.println("Błąd podczas odczytu z pliku tekstowego.");
            e.printStackTrace();
        }

        return transactions;
    }

    public static void deleteTransaction(int index) throws IOException {
        List<Transaction> transactions = getAllTransactions();
        if (index >= 0 && index < transactions.size()) {
            transactions.remove(index);
            Main.tranzakcje.remove(index);
            saveAllTransactions(transactions);
            System.out.println("Pomyślnie usunięto transakcję o indeksie " + index);
        } else {
            System.out.println("Nieprawidłowy indeks transakcji.");
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
            System.out.println("Błąd podczas zapisywania wszystkich transakcji do pliku tekstowego.");
            e.printStackTrace();
        }
    }
    public static String[] wczytajZPliku() {
        List<String> listaKryptowalut = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CryptoFilePath))) {
            String linia;
            while ((linia = reader.readLine()) != null) {
                listaKryptowalut.add(linia);
            }
        } catch (IOException e) {
            System.err.println("Wystąpił błąd podczas wczytywania pliku: " + e.getMessage());
        }


        String[] kryptowaluty = new String[listaKryptowalut.size()];
        kryptowaluty = listaKryptowalut.toArray(kryptowaluty);

        return kryptowaluty;
    }
}


