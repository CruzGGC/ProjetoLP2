import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
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
    private JTextField RField;
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

    public MainUI() {

        Filter filter = new Filter();
        filter.applyFilters(RNField, ACField, CCField, PField);

        homeButton.addActionListener(e -> switchPanel(HomePanel));
        bookingsButton.addActionListener(e -> switchPanel(Bookings));
        roomsButton.addActionListener(e -> switchPanel(Rooms));

        RoomTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    setRoomDetailsFromSelectedRow();
                    switchPanel(RoomDetail);
                }
            }
        });

        RoomcancelButton.addActionListener(e -> switchPanel(RoomTableJP));
        RoomsaveButton.addActionListener(e -> {
            updateRoomTableFromFields();
            roomtableCreator.writeTableDataToCSV(RoomTable, roomfilePath);
            switchPanel(RoomTableJP);
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

        BookingsCancelButton.addActionListener(e -> switchPanel(BookingsTableJP));
        BookingsGARButton.addActionListener(e -> {
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
                BookingsRoomStatus.setText("Room " + room.getId() + " is available");

            } else {
                BookingsRoomStatus.setText("No rooms available");
            }
        });
        BookingsBRButton.addActionListener(e -> {
            updateBookingsTableFromFields();
            bookingstableCreator.writeTableDataToCSV(BookingsTable, bookingsfilePath);
            switchPanel(BookingsTableJP);
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
        //RField.setText(getValueAt(BookingsTable, row, 2));
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

}