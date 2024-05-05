import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ButtonEditor extends DefaultCellEditor {
    private final JButton button;
    private final TableCreator bookingstableCreator;
    private final String bookingsfilePath;
    private int row;
    private final String tableType;

    public ButtonEditor(JCheckBox checkBox, TableCreator bookingstableCreator, String bookingsfilePath, String tableType) {
        super(checkBox);
        this.bookingstableCreator = bookingstableCreator;
        this.bookingsfilePath = bookingsfilePath;
        this.tableType = tableType;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (tableType.equals("CITable")) {
            button.setText("Check In");
        } else if (tableType.equals("COTable")) {
            button.setText("Check Out");
        } else {
            button.setText(value == null ? "" : value.toString());
        }
        this.row = row;
        return button;
    }

    public Object getCellEditorValue() {
        if (row >= 0) {
            JTable tempTable = new JTable();
            bookingstableCreator.loadTableDataFromCSV(tempTable, bookingsfilePath);
            filterTableByStatus(tempTable, tableType.equals("CITable") ? 1 : 2);

            String guestFirstName = tempTable.getValueAt(row, 0).toString();
            String guestLastName = tempTable.getValueAt(row, 1).toString();

            updateTable(tempTable, tableType.equals("CITable") ? 2 : 3, tableType.equals("CITable") ? "Checked In" : "Checked Out", guestFirstName, guestLastName);
        }
        return button.getText();
    }

    private void updateTable(JTable table, int newStatus, String newState, String guestFirstName, String guestLastName) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String firstName = model.getValueAt(i, 0).toString();
            String lastName = model.getValueAt(i, 1).toString();
            if (firstName.equals(guestFirstName) && lastName.equals(guestLastName)) {
                model.setValueAt(newStatus, i, 7);
                model.setValueAt(newState, i, 8);
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