import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private TableCreator bookingstableCreator;
    private JTable CITable;
    private String bookingsfilePath;
    private int row;

    public ButtonEditor(JCheckBox checkBox, TableCreator bookingstableCreator, JTable CITable, String bookingsfilePath) {
        super(checkBox);
        this.bookingstableCreator = bookingstableCreator;
        this.CITable = CITable;
        this.bookingsfilePath = bookingsfilePath;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        this.row = row; // Store the row that the button is in
        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            // Implement your button click event here
            if (row >= 0) { // Check if a row is actually selected
                // Load the BT.csv file into a temporary JTable
                JTable tempTable = new JTable();
                bookingstableCreator.loadTableDataFromCSV(tempTable, bookingsfilePath);

                // Set the Status column to 2 and the State column to "Checked In"
                tempTable.setValueAt(2, row, 7); // Assuming 7 is the index of the Status column
                tempTable.setValueAt("Checked In", row, 8); // Assuming 8 is the index of the State column

                // Write the updated table data back to the BT.csv file
                bookingstableCreator.writeTableDataToCSV(tempTable, bookingsfilePath);
            }
        }
        isPushed = false;
        return label;
    }

    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}