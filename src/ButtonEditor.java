import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ButtonEditor extends DefaultCellEditor {
    private final JButton button;
    private final TableCreator bookingstableCreator;
    private final String bookingsfilePath;
    private int row;
    private final String tableType;

    public ButtonEditor(JCheckBox checkBox, TableCreator bookingstableCreator, JTable CITable, JTable BookingsTable, String bookingsfilePath, String tableType) {
        super(checkBox);
        this.bookingstableCreator = bookingstableCreator;
        this.bookingsfilePath = bookingsfilePath;
        this.tableType = tableType;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        button.setText(value == null ? "" : value.toString());
        this.row = row;
        return button;
    }

    public Object getCellEditorValue() {
        if (row >= 0) {
            System.out.println("Modifying row: " + row);

            if (tableType.equals("CITable")) {
                JTable CITempTable = new JTable();
                bookingstableCreator.loadTableDataFromCSV(CITempTable, bookingsfilePath);
                filterTableByStatus(CITempTable, 1); // Only add rows with status 1

                String guestFirstName = CITempTable.getValueAt(row, 0).toString();
                String guestLastName = CITempTable.getValueAt(row, 1).toString();

                updateTable(CITempTable, 2, "Checked In", guestFirstName, guestLastName);
            } else if (tableType.equals("COTable")) {
                JTable COTempTable = new JTable();
                bookingstableCreator.loadTableDataFromCSV(COTempTable, bookingsfilePath);
                filterTableByStatus(COTempTable, 2); // Only add rows with status 2

                String guestFirstName = COTempTable.getValueAt(row, 0).toString();
                String guestLastName = COTempTable.getValueAt(row, 1).toString();

                updateTable(COTempTable, 3, "Checked Out", guestFirstName, guestLastName);
            }
        }
        return button.getText();
    }

    private void updateTable(JTable table, int newStatus, String newState, String guestFirstName, String guestLastName) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String firstName = model.getValueAt(i, 0).toString();
            String lastName = model.getValueAt(i, 1).toString();
            if (firstName.equals(guestFirstName) && lastName.equals(guestLastName)) {
                model.setValueAt(newStatus, i, 7); // Assuming 7 is the index of the Status column
                model.setValueAt(newState, i, 8); // Assuming 8 is the index of the State column
            }
        }
        bookingstableCreator.writeTableDataToCSV(table, bookingsfilePath);
    }

    private void filterTableByStatus(JTable table, int status) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            Object value = model.getValueAt(i, 7);
            if (value == null || !value.toString().equals(String.valueOf(status))) {
                model.removeRow(i);
            }
        }
    }
}