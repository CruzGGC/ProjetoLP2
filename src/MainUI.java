import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainUI {

    private final RoomTableCreator tableCreator = new RoomTableCreator();
    private final String filePath = "RoomTable/RT.csv";

    private JPanel MainPanel;
    private JButton homeButton;
    private JButton bookingsButton;
    private JButton roomsButton;
    private JPanel Card;
    private JPanel HomePanel;
    private JPanel Bookings;
    private JPanel Rooms;
    private JPanel RoomsCard;
    private JPanel Table;
    private JPanel RoomDetail;
    private JTable RoomTable;
    private JButton saveButton;
    private JPanel RoomDetailList;
    private JPanel SaveCancel;
    private JButton cancelButton;
    private JTextField RNField;
    private JTextField ACField;
    private JTextField CCField;
    private JTextField PField;
    private JScrollPane TBScrollPane;
    private JPanel BookingsCard;
    private JTable BookingsTable;
    private JPanel table;
    private JPanel BookingsDetailList;

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

        cancelButton.addActionListener(e -> switchPanel(Table));
        saveButton.addActionListener(e -> {
            updateRoomTableFromFields();
            tableCreator.writeTableDataToCSV(RoomTable, filePath);
            switchPanel(Table);
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
        RNField.setText(RoomTable.getValueAt(row, 0).toString());
        ACField.setText(RoomTable.getValueAt(row, 1).toString());
        CCField.setText(RoomTable.getValueAt(row, 2).toString());
        PField.setText(RoomTable.getValueAt(row, 3).toString());
    }

    private void updateRoomTableFromFields() {
        int row = RoomTable.getSelectedRow();
        RoomTable.setValueAt(Integer.parseInt(RNField.getText()), row, 0);
        RoomTable.setValueAt(Integer.parseInt(ACField.getText()), row, 1);
        RoomTable.setValueAt(Integer.parseInt(CCField.getText()), row, 2);
        RoomTable.setValueAt(Float.parseFloat(PField.getText()), row, 3);
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }

    public JTable getRoomTable() {
        return RoomTable;
    }
}