import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class MainUI {

    private final TableCreator roomtableCreator = new TableCreator();
    private final String roomfilePath = "src/Tables/RT.csv";

    private final TableCreator bookingstableCreator = new TableCreator();
    private final String bookingsfilePath = "src/Tables/BT.csv";

    // Panels
    private JPanel MainPanel, Card, HomePanel, Bookings, Rooms, RoomsCard, RoomTableJP, RoomDetail, RoomDetailList, SaveCancel;
    private JPanel BookingsTableJP, BookingsDetail, BookingsDetailList, Savecancel, BookingsCard, OperationCard, CICOperation;

    // Buttons
    private JButton homeButton, bookingsButton, roomsButton, RoomsaveButton, RoomcancelButton, BookingsGARButton, BookingsBRButton;
    private JButton BookingsCancelButton, checkInButton, checkOutButton, cancelBookingButton, cancelButton, searchButton;

    // Text Fields
    private JTextField RNField, ACField, CCField, PField, FNField, LNField, AField, CField, SearchF;

    // Tables
    private JTable RoomTable, BookingsTable, CITable, COTable;

    // Scroll Panes
    private JScrollPane TBScrollPane, BTScroll;

    // Date Pickers
    private DatePicker CIField, COField;

    // Labels
    private JLabel BookingsRoomStatus;

    // Combo Box
    private JComboBox comboBox;
    private JPanel CIJPanel;
    private JPanel COJPanel;
    private JPanel MainButtons;
    private JPanel SearchFilterJP;

    public MainUI() {

        loadCheckInsToTable();
        loadCheckOutsToTable();

        // Assuming comboBox is your JComboBox instance
        comboBox.addItem("Booked");
        comboBox.addItem("Checked In");
        comboBox.addItem("Checked Out");
        comboBox.addItem("Canceled");

        Filter filter = new Filter();
        filter.applyFilters(RNField, ACField, CCField, PField);

        homeButton.addActionListener(e -> {
            switchPanel(HomePanel);
            RoomTable.revalidate();
            RoomTable.repaint();
            BookingsTable.revalidate();
            BookingsTable.repaint();
            loadCheckInsToTable();
            loadCheckOutsToTable();
        });
        bookingsButton.addActionListener(e -> {
            switchPanel(Bookings);
            RoomTable.revalidate();
            RoomTable.repaint();
            BookingsTable.revalidate();
            BookingsTable.repaint();
            bookingstableCreator.loadTableDataFromCSV(BookingsTable, bookingsfilePath);
        });
        roomsButton.addActionListener(e -> {
            switchPanel(Rooms);
            RoomTable.revalidate();
            RoomTable.repaint();
            BookingsTable.revalidate();
            BookingsTable.repaint();
            roomtableCreator.loadTableDataFromCSV(RoomTable, roomfilePath);
        });

        RoomTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    setRoomDetailsFromSelectedRow();
                    switchPanel(RoomDetail);
                }
            }
        });

        RoomcancelButton.addActionListener(e -> {
            // Clear all the fields
            RNField.setText("");
            ACField.setText("");
            CCField.setText("");
            PField.setText("");

            switchPanel(RoomTableJP);
        });
        RoomsaveButton.addActionListener(e -> {
            if (RNField.getText().isEmpty() || ACField.getText().isEmpty() || CCField.getText().isEmpty() || PField.getText().isEmpty()) {
                // If any field is empty, show a message dialog
                JOptionPane.showMessageDialog(MainPanel, "Fill the form", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                updateRoomTableFromFields();
                roomtableCreator.writeTableDataToCSV(RoomTable, roomfilePath);
                switchPanel(RoomTableJP);
            }

        });

        BookingsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    setBookingDetailsFromSelectedRow();
                    switchPanel(BookingsDetail); // Switch to BookingsDetail first
                    int selectedRow = BookingsTable.getSelectedRow();
                    if (selectedRow >= 0) { // Check if a row is actually selected
                        Object statusValue = BookingsTable.getValueAt(selectedRow, 7); // Assuming 7 is the index of the Status column
                        if (statusValue != null && statusValue.toString().equals("1")) {
                            // If the status is 1 (Booked), retrieve the room details and display them in the BookingsRoomStatus label
                            Object roomIdValue = BookingsTable.getValueAt(selectedRow, 2); // Assuming 2 is the index of the Room column
                            if (roomIdValue != null) {
                                int roomId = Integer.parseInt(roomIdValue.toString());
                                Room room = roomtableCreator.getRoomById(roomId, roomfilePath); // Assuming roomtableCreator and roomfilePath are available
                                if (room != null) {
                                    BookingsRoomStatus.setText("Room " + room.id() + " is booked. Price: " + room.price());
                                }
                            }
                            switchPanel(BookingsDetail);
                            CICOperation.setVisible(true);
                            Savecancel.setVisible(false);
                        } else {
                            switchPanel(BookingsDetail);
                            Savecancel.setVisible(true);
                            CICOperation.setVisible(false);
                        }
                    }
                }
            }
        });

        checkInButton.addActionListener(e -> {
            int selectedRow = BookingsTable.getSelectedRow();
            if (selectedRow >= 0) { // Check if a row is actually selected
                // Set the Status column to 2 and the State column to "Checked In"
                BookingsTable.setValueAt(2, selectedRow, 7); // Assuming 7 is the index of the Status column
                BookingsTable.setValueAt("Checked In", selectedRow, 8); // Assuming 8 is the index of the State column

                // Write the updated table data back to the BT.csv file
                bookingstableCreator.writeTableDataToCSV(BookingsTable, bookingsfilePath);

                // Switch back to the BookingsTableJP panel
                switchPanel(BookingsTableJP);
            }
        });

        checkOutButton.addActionListener(e -> {
            int selectedRow = BookingsTable.getSelectedRow();
            if (selectedRow >= 0) { // Check if a row is actually selected
                // Set the Status column to 3 and the State column to "Checked Out"
                BookingsTable.setValueAt(3, selectedRow, 7); // Assuming 7 is the index of the Status column
                BookingsTable.setValueAt("Checked Out", selectedRow, 8); // Assuming 8 is the index of the State column

                // Write the updated table data back to the BT.csv file
                bookingstableCreator.writeTableDataToCSV(BookingsTable, bookingsfilePath);

                // Switch back to the BookingsTableJP panel
                switchPanel(BookingsTableJP);
            }
        });

        cancelBookingButton.addActionListener(e -> {
            int selectedRow = BookingsTable.getSelectedRow();
            if (selectedRow >= 0) { // Check if a row is actually selected
                // Set the Status column to 4 and the State column to "Canceled"
                BookingsTable.setValueAt(4, selectedRow, 7); // Assuming 7 is the index of the Status column
                BookingsTable.setValueAt("Canceled", selectedRow, 8); // Assuming 8 is the index of the State column

                // Write the updated table data back to the BT.csv file
                bookingstableCreator.writeTableDataToCSV(BookingsTable, bookingsfilePath);

                // Switch back to the BookingsTableJP panel
                switchPanel(BookingsTableJP);
            }
        });

        BookingsCancelButton.addActionListener(e -> {
            // Clear all the fields
            FNField.setText("");
            LNField.setText("");
            CIField.setDate(null);
            COField.setDate(null);
            AField.setText("");
            CField.setText("");

            // Clear the JLabel
            BookingsRoomStatus.setText("");
            switchPanel(BookingsTableJP);
        });

        BookingsGARButton.addActionListener(e -> {
            // Check if the AField and CField are filled
            if (AField.getText().isEmpty() || CField.getText().isEmpty()) {
                // If any field is empty, show a message dialog
                JOptionPane.showMessageDialog(MainPanel, "Fill the form", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // If all fields are filled, proceed with the existing logic
                // Load data from CSV files
                List<Room> rooms = roomtableCreator.loadRoomsFromCSV(roomfilePath);
                List<Booking> bookings = bookingstableCreator.loadBookingsFromCSV(bookingsfilePath);

                // Create an instance of RoomBookingSystem
                RoomBookingSystem roomBookingSystem = new RoomBookingSystem();

                // Get the input values from the UI
                int numberOfAdults = Integer.parseInt(AField.getText());
                int numberOfChildren = Integer.parseInt(CField.getText());
                LocalDate checkInDate = CIField.getDate();
                LocalDate checkOutDate = COField.getDate();
                int canceledStatus = 4; // Assuming 4 is the status for canceled bookings

                // Use the searchAvailableRooms method to find the available rooms
                List<Room> availableRooms = roomBookingSystem.searchAvailableRooms(rooms, bookings, numberOfAdults, numberOfChildren, checkInDate, checkOutDate, canceledStatus);

                // Mostra o resultado na obtido UI
                if (!availableRooms.isEmpty()) {
                    Room room = availableRooms.getFirst(); // Get the first available room
                    BookingsRoomStatus.setText("Room " + room.id() + " is available. Price: " + room.price());

                    // Update the Room column of the selected row in the BookingsTable with the room details
                    int selectedRow = BookingsTable.getSelectedRow();
                    if (selectedRow >= 0) { // Check if a row is actually selected
                        DefaultTableModel model = (DefaultTableModel) BookingsTable.getModel();
                        model.setValueAt(room.id(), selectedRow, 2); // 2 is the index of the Room column
                        // Write the updated table data back to the BT.csv file
                        bookingstableCreator.writeTableDataToCSV(BookingsTable, bookingsfilePath);
                    }
                } else {
                    BookingsRoomStatus.setText("No rooms available");
                }
            }
        });

        BookingsBRButton.addActionListener(e -> {
            // Check if all required fields are filled
            if (FNField.getText().isEmpty() || LNField.getText().isEmpty() || CIField.getDate() == null || COField.getDate() == null) {
                // If any field is empty, show a message dialog
                JOptionPane.showMessageDialog(MainPanel, "Fill all the Fields!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Check if a room has been selected
                int selectedRow = BookingsTable.getSelectedRow();
                if (selectedRow >= 0) { // Check if a row is actually selected
                    Object roomValue = BookingsTable.getValueAt(selectedRow, 2); // 2 is the index of the Room column
                    if (roomValue == null || roomValue.toString().isEmpty()) {
                        // If no room has been selected, show a message dialog
                        JOptionPane.showMessageDialog(MainPanel, "Get Available Rooms First!", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // If all fields are filled and a room has been selected, proceed with the existing logic
                        updateBookingsTableFromFields();
                        BookingsTable.setValueAt(AField.getText(), selectedRow, 5);
                        BookingsTable.setValueAt(CField.getText(), selectedRow, 6);
                        BookingsTable.setValueAt(1, selectedRow, 7);
                        BookingsTable.setValueAt("Booked", selectedRow, 8);
                        bookingstableCreator.writeTableDataToCSV(BookingsTable, bookingsfilePath);
                        switchPanel(BookingsTableJP);
                    }
                } else {
                    // If no row has been selected, show a message dialog
                    JOptionPane.showMessageDialog(MainPanel, "Select a booking first!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        searchButton.addActionListener(e -> {
            String selectedState = Objects.requireNonNull(comboBox.getSelectedItem()).toString();
            String searchText = SearchF.getText();
            String firstName = "";
            String lastName = "";

            if (!searchText.isEmpty()) {
                String[] names = searchText.split(" ", 2);
                firstName = names[0];
                lastName = names.length > 1 ? names[1] : "";
            }

            JTable tempTable = new JTable();
            bookingstableCreator.loadTableDataFromCSV(tempTable, bookingsfilePath);

            DefaultTableModel model = (DefaultTableModel) BookingsTable.getModel();
            model.setRowCount(0); // Clear the BookingsTable

            for (int i = 0; i < tempTable.getRowCount(); i++) {
                Object stateObj = tempTable.getValueAt(i, 8); // Assuming 8 is the index of the State column
                Object guestFirstNameObj = tempTable.getValueAt(i, 0); // Assuming 0 is the index of the Guest First Name column
                Object guestLastNameObj = tempTable.getValueAt(i, 1); // Assuming 1 is the index of the Guest Last Name column

                if (stateObj != null) {
                    String state = stateObj.toString();
                    String guestFirstName = guestFirstNameObj != null ? guestFirstNameObj.toString() : "";
                    String guestLastName = guestLastNameObj != null ? guestLastNameObj.toString() : "";

                    if (state.equals(selectedState) && (searchText.isEmpty() || (guestFirstName.equals(firstName) && guestLastName.equals(lastName)))) {
                        model.addRow(new Object[]{
                                guestFirstName,
                                guestLastName,
                                tempTable.getValueAt(i, 2),
                                tempTable.getValueAt(i, 3),
                                tempTable.getValueAt(i, 4),
                                tempTable.getValueAt(i, 5),
                                tempTable.getValueAt(i, 6),
                                tempTable.getValueAt(i, 7),
                                state
                        });
                    }
                }
            }
        });

    }

    private void switchPanel(JPanel panel) {
        Card.removeAll();
        Card.add(panel);
        Card.revalidate();
        Card.repaint();
    }

    private void setRoomDetailsFromSelectedRow() {
        int row = RoomTable.getSelectedRow();
        RNField.setText(getValueAt(RoomTable, row, 0));
        ACField.setText(getValueAt(RoomTable, row, 1));
        CCField.setText(getValueAt(RoomTable, row, 2));
        PField.setText(getValueAt(RoomTable, row, 3));
    }

    private void updateRoomTableFromFields() {
        int row = RoomTable.getSelectedRow();
        System.out.println("Updating RoomTable at row: " + row);
        RoomTable.setValueAt(Integer.parseInt(RNField.getText()), row, 0);
        RoomTable.setValueAt(Integer.parseInt(ACField.getText()), row, 1);
        RoomTable.setValueAt(Integer.parseInt(CCField.getText()), row, 2);
        RoomTable.setValueAt(Float.parseFloat(PField.getText()), row, 3);
    }

    private void setBookingDetailsFromSelectedRow() {
        int row = BookingsTable.getSelectedRow();
        FNField.setText(getValueAt(BookingsTable, row, 0));
        LNField.setText(getValueAt(BookingsTable, row, 1));
        CIField.setDate(getDateAt(BookingsTable, row, 3));
        COField.setDate(getDateAt(BookingsTable, row, 4));
        AField.setText(getValueAt(BookingsTable, row, 5)); // Assuming 5 is the index of the Number of Adults column
        CField.setText(getValueAt(BookingsTable, row, 6)); // Assuming 6 is the index of the Number of Children column
    }

    private void updateBookingsTableFromFields() {
        int row = BookingsTable.getSelectedRow();
        System.out.println("Updating BookingsTable at row: " + row);
        if (row >= 0) { // Check if a row is actually selected
            BookingsTable.setValueAt(FNField.getText(), row, 0);
            BookingsTable.setValueAt(LNField.getText(), row, 1);
            BookingsTable.setValueAt(CIField.getDate(), row, 3);
            BookingsTable.setValueAt(COField.getDate(), row, 4);
            BookingsTable.setValueAt(AField.getText(), row, 5); // Assuming 5 is the index of the Number of Adults column
            BookingsTable.setValueAt(CField.getText(), row, 6); // Assuming 6 is the index of the Number of Children column
        }
    }

    private String getValueAt(JTable table, int row, int column) {
        Object value = table.getValueAt(row, column);
        return (value != null) ? value.toString() : "";
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }

    public JTable getRoomTable() {
        return RoomTable;
    }

    public JTable getBookingsTable() {
        return BookingsTable;
    }

    public void loadCheckInsToTable() {
        DefaultTableModel modelCIT = bookingstableCreator.loadCheckInsFromCSV(bookingsfilePath);
        modelCIT.addColumn("Check In"); // Add a new column for the button
        CITable.setModel(modelCIT);

        // Add the button to the last column in CITable
        TableColumn columnCITable = CITable.getColumnModel().getColumn(CITable.getColumnCount() - 1);
        columnCITable.setCellEditor(new ButtonEditor(new JCheckBox(), bookingstableCreator, bookingsfilePath, "CITable"));
        columnCITable.setCellRenderer(new ButtonRenderer());
    }

    public void loadCheckOutsToTable() {
        DefaultTableModel modelCOT = bookingstableCreator.loadCheckOutsFromCSV(bookingsfilePath);
        modelCOT.addColumn("Check Out");
        COTable.setModel(modelCOT);

        // Add the button to the last column in COTable
        TableColumn columnCOTable = COTable.getColumnModel().getColumn(COTable.getColumnCount() - 1);
        columnCOTable.setCellEditor(new ButtonEditor(new JCheckBox(), bookingstableCreator, bookingsfilePath, "COTable"));
        columnCOTable.setCellRenderer(new ButtonRenderer());
    }

    private LocalDate getDateAt(JTable table, int row, int column) {
        Object value = table.getValueAt(row, column);
        return (value != null) ? LocalDate.parse(value.toString()) : null;
    }
}