import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

public class MainUI {

    private final TableCreator roomtableCreator = new TableCreator();
    private final String roomfilePath = "src/Tables/RT.csv";

    private final TableCreator bookingstableCreator = new TableCreator();
    private final String bookingsfilePath = "src/Tables/BT.csv";

    private JPanel MainPanel;
    private JButton homeButton;
    private JButton bookingsButton;
    private JButton roomsButton;
    private JPanel Card;
    private JPanel HomePanel;
    private JPanel Bookings;
    private JPanel Rooms;
    private JPanel RoomsCard;
    private JPanel RoomTableJP;
    private JPanel RoomDetail;
    private JTable RoomTable;
    private JButton RoomsaveButton;
    private JPanel RoomDetailList;
    private JPanel SaveCancel;
    private JButton RoomcancelButton;
    private JTextField RNField;
    private JTextField ACField;
    private JTextField CCField;
    private JTextField PField;
    private JScrollPane TBScrollPane;
    private JTable BookingsTable;
    private JPanel BookingsTableJP;
    private JPanel BookingsDetail;
    private JPanel BookingsDetailList;
    private JScrollPane BTScroll;
    private JTextField FNField;
    private JTextField LNField;
    private JTextField AField;
    private JButton BookingsGARButton;
    private JButton BookingsBRButton;
    private JPanel Savecancel;
    private JPanel BookingsCard;
    private JTextField CField;
    private JLabel BookingsRoomStatus;
    private JButton BookingsCancelButton;
    private DatePicker CIField;
    private DatePicker COField;
    private JPanel HomeTableJP;
    private JScrollPane HTScrool;
    private JTable HomeTable;
    private JPanel HomeDetail;
    private JPanel HomeDetailList;
    private JTextField LNfield;
    private JTextField FNfield;
    private JTextField RField;
    private JTextField COfield;
    private JPanel HomeCard;
    private JPanel HomeDetailList2;
    private JTextField LN2Field;
    private JTextField FN2List;
    private JTextField R2Field;
    private JTextField CI2Field;

    public MainUI() {

        Filter filter = new Filter();
        filter.applyFilters(RNField, ACField, CCField, PField);

        homeButton.addActionListener(e -> {
            switchPanel(HomePanel);
            RoomTable.revalidate();
            RoomTable.repaint();
            BookingsTable.revalidate();
            BookingsTable.repaint();
        });
        bookingsButton.addActionListener(e -> {

            bookingstableCreator.loadTableDataFromCSV(BookingsTable, bookingsfilePath);
            switchPanel(Bookings);
            RoomTable.revalidate();
            RoomTable.repaint();
            BookingsTable.revalidate();
            BookingsTable.repaint();
        });
        roomsButton.addActionListener(e -> {
            switchPanel(Rooms);
            RoomTable.revalidate();
            RoomTable.repaint();
            BookingsTable.revalidate();
            BookingsTable.repaint();
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
                    switchPanel(BookingsDetail);
                }
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
                int canceledStatus = 0; // Assuming 0 is the status for canceled bookings

                // Use the searchAvailableRooms method to find the available rooms
                List<Room> availableRooms = roomBookingSystem.searchAvailableRooms(rooms, bookings, numberOfAdults, numberOfChildren, checkInDate, checkOutDate, canceledStatus);

                // Display the result in the UI
                if (!availableRooms.isEmpty()) {
                    Room room = availableRooms.get(0); // Get the first available room
                    BookingsRoomStatus.setText("Room " + room.getId() + " is available. Price: " + room.getPrice());

                    // Update the Room column of the selected row in the BookingsTable with the room details
                    int selectedRow = BookingsTable.getSelectedRow();
                    if (selectedRow >= 0) { // Check if a row is actually selected
                        DefaultTableModel model = (DefaultTableModel) BookingsTable.getModel();
                        model.setValueAt(room.getId(), selectedRow, 2); // 2 is the index of the Room column

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
                        bookingstableCreator.writeTableDataToCSV(BookingsTable, bookingsfilePath);
                        switchPanel(BookingsTableJP);
                    }
                } else {
                    // If no row has been selected, show a message dialog
                    JOptionPane.showMessageDialog(MainPanel, "Select a booking first!", "Error", JOptionPane.ERROR_MESSAGE);
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
    }

    private void updateBookingsTableFromFields() {
        int row = BookingsTable.getSelectedRow();
        System.out.println("Updating BookingsTable at row: " + row);
        if (row >= 0) { // Check if a row is actually selected
            BookingsTable.setValueAt(FNField.getText(), row, 0);
            BookingsTable.setValueAt(LNField.getText(), row, 1);
            BookingsTable.setValueAt(CIField.getDate(), row, 3);
            BookingsTable.setValueAt(COField.getDate(), row, 4);
            BookingsTable.setValueAt(AField.getText(), row, 5);
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

    private LocalDate getDateAt(JTable table, int row, int column) {
        Object value = table.getValueAt(row, column);
        return (value != null) ? LocalDate.parse(value.toString()) : null;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}