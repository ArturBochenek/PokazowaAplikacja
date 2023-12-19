import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
public class Interface {
    private static JFrame frame;
    private static JTable table;
    public static void createWindow() {
        frame = new JFrame("Szwagro-Inwesto-Pol");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 300);
        JPanel panel = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Szwagro-Inwesto-Pol");
        JButton showInvestmentsButton = new JButton("Show My Trades");
        JButton showAddInvestmentsButton = new JButton("Add Trade");

        panel.add(welcomeLabel, BorderLayout.NORTH);
        panel.add(showInvestmentsButton, BorderLayout.WEST);
        panel.add(showAddInvestmentsButton, BorderLayout.EAST);

        showInvestmentsButton.addActionListener(e -> {
            try {
                showInvestmentsInformation();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        showAddInvestmentsButton.addActionListener(e -> addNewTransaction());

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
    private static void showInvestmentsInformation() throws IOException {
        List<Transaction> transactions = Main.transactions;

        Object[][] data = new Object[transactions.size()][8];

        for (int i = 0; i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);
            data[i][0] = transaction.getName();
            data[i][1] = transaction.getQuantity();
            data[i][2] = transaction.getInitialValue();
            data[i][3] = transaction.getCurrentValue();
            data[i][4] = transaction.getCurrentValue() * transaction.getQuantity();
            data[i][5] = transaction.getCurrentValue() * transaction.getQuantity() - transaction.getInitialValue();
            data[i][6] = transaction.getCurrentValue() * transaction.getQuantity() / transaction.getInitialValue() * 100;
            data[i][7] = "Delete";
        }
        String[] columnNames = {"Name", "Quantity", "Bought for", "Exchange", "Current price", "USD Balance", "%", String.valueOf(Count.seconds)};

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(150);
        table.getColumnModel().getColumn(7).setPreferredWidth(200);

        table.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor());

        JOptionPane.showMessageDialog(frame, new JScrollPane(table), "Show My Trades", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void refreshTable() throws IOException {
        if (table != null) {
            List<Transaction> transactions = Main.transactions;

            Object[][] data = new Object[transactions.size()][8];

            for (int i = 0; i < transactions.size(); i++) {
                Transaction transaction = transactions.get(i);
                data[i][0] = transaction.getName();
                data[i][1] = transaction.getQuantity();
                data[i][2] = transaction.getInitialValue();
                data[i][3] = transaction.getCurrentValue();
                data[i][4] = transaction.getCurrentValue() * transaction.getQuantity();
                data[i][5] = transaction.getCurrentValue() * transaction.getQuantity() - transaction.getInitialValue();
                data[i][6] = transaction.getCurrentValue() * transaction.getQuantity() / transaction.getInitialValue() * 100;
                data[i][7] = "Delete";
            }

            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            tableModel.setDataVector(data, getColumnNames());

            table.getColumnModel().getColumn(0).setPreferredWidth(150);
            table.getColumnModel().getColumn(1).setPreferredWidth(100);
            table.getColumnModel().getColumn(2).setPreferredWidth(100);
            table.getColumnModel().getColumn(3).setPreferredWidth(100);
            table.getColumnModel().getColumn(4).setPreferredWidth(150);
            table.getColumnModel().getColumn(5).setPreferredWidth(100);
            table.getColumnModel().getColumn(6).setPreferredWidth(150);
            table.getColumnModel().getColumn(7).setPreferredWidth(200);

            table.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
            table.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor());
            tableModel.fireTableDataChanged();
        }
    }

    private static String[] getColumnNames() {
        return new String[]{"Name", "Quantity", "Bought for", "Exchange", "Current price", "USD Balance", "%", String.valueOf(Count.seconds)};
    }

    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    static class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private final JButton deleteButton;
        private int currentRow;

        public ButtonEditor() {
            deleteButton = new JButton("Delete");
            deleteButton.setOpaque(true);
            deleteButton.addActionListener(this);
        }

        @Override
        public Object getCellEditorValue() {
            return deleteButton.getText();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRow = row;
            return deleteButton;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentRow >= 0 && currentRow < Main.transactions.size()) {
                try {
                    FileHandler.deleteTransaction(currentRow);
                    refreshTable();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            fireEditingStopped();
        }

        private void refreshTable() {
            try {
                Interface.refreshTable();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void addNewTransaction() {
        JFrame addTransactionFrame = new JFrame("Add new trade");
        addTransactionFrame.setSize(400, 200);
        addTransactionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 2));

        JLabel cryptoLabel = new JLabel("Cryptocurrency:");

        JComboBox<String> cryptoComboBox = new JComboBox<>(Api.CryptoCurrencies);

        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField();

        JLabel valueLabel = new JLabel("Bought for total USD:");
        JTextField valueField = new JTextField();

        JButton confirmButton = new JButton("Approve");

        confirmButton.addActionListener(e -> {
            String selectedCrypto = (String) cryptoComboBox.getSelectedItem();
            double quantity = Double.parseDouble(quantityField.getText());
            double value = Double.parseDouble(valueField.getText());
            Transaction transaction = new Transaction(selectedCrypto, quantity, value);
            FileHandler.saveToFile(transaction);

            addTransactionFrame.dispose();
            try {
                refreshTable();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        panel.add(cryptoLabel);
        panel.add(cryptoComboBox);
        panel.add(quantityLabel);
        panel.add(quantityField);
        panel.add(valueLabel);
        panel.add(valueField);
        panel.add(confirmButton);
        addTransactionFrame.getContentPane().add(panel);
        addTransactionFrame.setVisible(true);
    }
}
