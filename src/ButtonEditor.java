import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.Arrays;

/**
 * ButtonEditor class is responsible for handling the editing of buttons in a table cell.
 * It extends DefaultCellEditor.
 */
public class ButtonEditor extends DefaultCellEditor {
    private final JButton button;
    private final TableCreator bookingstableCreator;
    private final String bookingsfilePath;
    private int row;
    private final String tableType;

    /**
     * Constructor for the ButtonEditor class.
     * @param checkBox The checkbox to be used in the editor.
     * @param bookingstableCreator The TableCreator object for the bookings table.
     * @param bookingsfilePath The file path of the bookings table.
     * @param tableType The type of table.
     */
    public ButtonEditor(JCheckBox checkBox, TableCreator bookingstableCreator, String bookingsfilePath, String tableType) {
        super(checkBox);
        this.bookingstableCreator = bookingstableCreator;
        this.bookingsfilePath = bookingsfilePath;
        this.tableType = tableType;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    /**
     * Returns the component used for editing the cell.
     * @param table The JTable that is asking the editor to edit.
     * @param value The value of the cell to be edited.
     * @param isSelected True if the cell is to be edited with the selection highlighted; otherwise false.
     * @param row The row index of the cell being edited.
     * @param column The column index of the cell being edited.
     * @return The component used for editing the cell.
     */
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

    /**
     * Returns the value contained in the editor.
     * @return The value contained in the editor.
     */
    public Object getCellEditorValue() {
        if (row >= 0) {
            JTable tempTable = new JTable();
            bookingstableCreator.loadTableDataFromCSV(tempTable, bookingsfilePath);
            filterTableByStatus(tempTable, tableType.equals("CITable") ? 1 : 2);

            if (tempTable.getRowCount() > row) {
                Object[] rowValues = new Object[tempTable.getColumnCount()];
                for (int i = 0; i < tempTable.getColumnCount(); i++) {
                    rowValues[i] = tempTable.getValueAt(row, i);
                }

                if (rowValues.length > 6) {
                    String guestFirstName = rowValues[0].toString();
                    String guestLastName = rowValues[1].toString();
                    String room = rowValues[2].toString();
                    String checkIn = rowValues[3].toString();
                    String checkOut = rowValues[4].toString();
                    String numAdults = rowValues[5].toString();
                    String numChildren = rowValues[6].toString();

                    System.out.println("Modifying row: " + Arrays.toString(new String[]{guestFirstName, guestLastName, room, checkIn, checkOut, numAdults, numChildren}));

                    if (updateTable(tableType.equals("CITable") ? 2 : 3, tableType.equals("CITable") ? "Checked In" : "Checked Out", guestFirstName, guestLastName, room, checkIn, checkOut, numAdults, numChildren)) {
                        System.out.println("Row updated successfully in BT.csv");
                    } else {
                        System.out.println("Failed to update row in BT.csv");
                    }
                }
            }
        }
        return button.getText();
    }

    /**
     * Updates the bookings table with the new status and state.
     * @param newStatus The new status to update the row with.
     * @param newState The new state to update the row with.
     * @param guestFirstName The first name of the guest.
     * @param guestLastName The last name of the guest.
     * @param room The room number.
     * @param checkIn The check-in date.
     * @param checkOut The check-out date.
     * @param numAdults The number of adults.
     * @param numChildren The number of children.
     * @return True if the row was updated successfully; otherwise false.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    private boolean updateTable(int newStatus, String newState, String guestFirstName, String guestLastName, String room, String checkIn, String checkOut, String numAdults, String numChildren) {
        boolean isRowUpdated = false;
        File inputFile = new File(bookingsfilePath);
        File tempFile = new File("src/Tables/temp.csv");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;
            while((currentLine = reader.readLine()) != null) {
                String[] rowData = currentLine.split(",");
                if (rowData.length > 6 && rowData[0].trim().equals(guestFirstName) && rowData[1].trim().equals(guestLastName) && rowData[2].trim().equals(room) && rowData[3].trim().equals(checkIn) && rowData[4].trim().equals(checkOut) && rowData[5].trim().equals(numAdults) && rowData[6].trim().equals(numChildren)) {
                    rowData[7] = String.valueOf(newStatus);
                    rowData[8] = newState;
                    currentLine = String.join(",", rowData);
                    isRowUpdated = true;
                    System.out.println("Updated row: " + Arrays.toString(rowData));
                }
                writer.write(currentLine + System.lineSeparator());
            }
            writer.close();
            reader.close();

            if (!inputFile.delete()) {
                System.out.println("Could not delete original file");
                return false;
            }

            boolean successful = tempFile.renameTo(inputFile);
            if (!successful) {
                System.out.println("Could not rename file");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return isRowUpdated;
    }

    /**
     * Filters the table by status.
     * @param table The table to filter.
     * @param status The status to filter by.
     */
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