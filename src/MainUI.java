import javax.swing.*;

public class MainUI {
    private JPanel MainPanel;
    private JButton homeButton;
    private JButton bookingsButton;
    private JButton roomsButton;
    private JPanel Card;
    private JPanel HomePanel;
    private JPanel Bookins;
    private JPanel Rooms;

    public MainUI() {
        homeButton.addActionListener(e -> {
            Card.removeAll();
            Card.add(HomePanel);
        });
        bookingsButton.addActionListener(e -> {
            Card.removeAll();
            Card.add(Bookins);
        });
        roomsButton.addActionListener(e -> {
            Card.removeAll();
            Card.add(Rooms);
        });
    }
    public JPanel getMainPanel() {
        return MainPanel;
    }
}
