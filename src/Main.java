import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        setLookAndFeel();
        SwingUtilities.invokeLater(Main::createUI);
    }

    private static void setLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.print("Erro!");
        }
    }

    private static void createUI() {
        MainUI ui = new MainUI();
        JFrame frame = setupFrame(ui.getMainPanel());
        TableCreator roomTableCreator = loadTableData(ui.getRoomTable(), "src/Tables/RT.csv");
        TableCreator bookingsTableCreator = loadTableData(ui.getBookingsTable(), "src/Tables/BT.csv");

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                roomTableCreator.writeTableDataToCSV(ui.getRoomTable(), "src/Tables/RT.csv");
                bookingsTableCreator.writeTableDataToCSV(ui.getBookingsTable(), "src/Tables/BT.csv");
            }
        });
    }

    private static JFrame setupFrame(JPanel root) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(root);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }

    private static TableCreator loadTableData(JTable table, String filePath) {
        TableCreator tableCreator = new TableCreator();
        tableCreator.loadTableDataFromCSV(table, filePath);
        return tableCreator;
    }
}