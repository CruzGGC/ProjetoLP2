import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        try {
            // Set the look and feel to Nimbus
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.print("Erro!");
        }
        SwingUtilities.invokeLater(Main::createUI);
    }
    private static void createUI() {
        MainUI ui = new MainUI();
        JPanel root = ui.getMainPanel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(root);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Load table data from CSV file, RoomTable
        TableCreator roomtableCreator = new TableCreator();
        String roomfilePath = "src/Tables/RT.csv";
        roomtableCreator.loadTableDataFromCSV(ui.getRoomTable(), roomfilePath);

        // Load table data from CSV file, BookingsTable

        TableCreator bookingstableCreator = new TableCreator();
        String bookingsfilePath = "src/Tables/BT.csv";
        bookingstableCreator.loadTableDataFromCSV(ui.getBookingsTable(), bookingsfilePath);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                roomtableCreator.writeTableDataToCSV(ui.getRoomTable(), roomfilePath);
                bookingstableCreator.writeTableDataToCSV(ui.getBookingsTable(), bookingsfilePath);
            }
        });
    }
}