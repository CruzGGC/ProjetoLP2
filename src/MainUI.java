import javax.swing.*;

public class MainUI {
    private JPanel MainPanel;
    private JButton homeButton;
    private JButton bookingsButton;
    private JButton roomsButton;
    private JPanel Card;
    private JPanel HomePanel;
    private JPanel Bookings;
    private JPanel Rooms;

    public MainUI() {
        homeButton.addActionListener(e -> {
            Card.removeAll();
            Card.add(HomePanel);
            Card.revalidate();
        });
        bookingsButton.addActionListener(e -> {
            Card.removeAll();
            Card.add(Bookings);
            Card.revalidate();
        });
        roomsButton.addActionListener(e -> {
            Card.removeAll();
            Card.add(Rooms);
            Card.revalidate();
        });
    }
    public JPanel getMainPanel() {
        return MainPanel;
    }
}
