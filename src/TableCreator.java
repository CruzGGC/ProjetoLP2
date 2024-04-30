import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TableCreator {

    public void writeTableDataToCSV(JTable table, String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            FileWriter csvWriter = new FileWriter(filePath);

            // Write column names
            for (int i = 0; i < table.getColumnCount(); i++) {
                csvWriter.append(table.getColumnName(i));
                if (i != table.getColumnCount() - 1) {
                    csvWriter.append(",");
                }
            }
            csvWriter.append("\n");

            // Write rows
            for (int i = 0; i < table.getRowCount(); i++) {
                for (int j = 0; j < table.getColumnCount(); j++) {
                    Object cellValue = table.getValueAt(i, j);
                    if (cellValue != null) {
                        csvWriter.append(cellValue.toString());
                    }
                    if (j != table.getColumnCount() - 1) {
                        csvWriter.append(",");
                    }
                }
                csvWriter.append("\n");
            }

            csvWriter.flush();
            csvWriter.close();
            System.out.println("Writing table data to CSV file: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTableDataFromCSV(JTable table, String filePath) {
        System.out.println("Loading table data from CSV file: " + filePath);
        Vector<String> columnNames = new Vector<>();
        Vector<Vector<Object>> data = new Vector<>();

        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
            String row;

            // Read column names
            if ((row = csvReader.readLine()) != null) {
                String[] dataFields = row.split(",");
                columnNames.addAll(Arrays.asList(dataFields));
            }

            // Read rows
            while ((row = csvReader.readLine()) != null) {
                String[] dataFields = row.split(",");
                Vector<Object> vector = new Vector<>(Arrays.asList(dataFields));
                data.add(vector);
            }

            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // This causes all cells to be not editable
            }
        };
        table.setModel(model);
    }

    public List<Room> loadRoomsFromCSV(String filePath) {
        List<Room> rooms = new ArrayList<>();
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
            String row;
            csvReader.readLine(); // Skip the header
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                int id = Integer.parseInt(data[0]);
                int adultsCapacity = Integer.parseInt(data[1]);
                int childrenCapacity = Integer.parseInt(data[2]);
                double price = Double.parseDouble(data[3]);
                rooms.add(new Room(id, adultsCapacity, childrenCapacity, price));
            }
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public List<Booking> loadBookingsFromCSV(String filePath) {
        List<Booking> bookings = new ArrayList<>();
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
            String row;
            csvReader.readLine(); // Skip the header
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                if (data.length > 0) {
                    String guestFirstName = !data[0].isEmpty() ? data[0] : "N/A";
                    String guestLastName = data.length > 1 && !data[1].isEmpty() ? data[1] : "N/A";
                    int roomId = data.length > 2 && !data[2].isEmpty() ? Integer.parseInt(data[2]) : 0;
                    LocalDate checkInDate = data.length > 3 && !data[3].isEmpty() ? LocalDate.parse(data[3]) : null;
                    LocalDate checkOutDate = data.length > 4 && !data[4].isEmpty() ? LocalDate.parse(data[4]) : null;
                    int statusId = data.length > 5 && !data[5].isEmpty() ? Integer.parseInt(data[5]) : 0;

                    bookings.add(new Booking(guestFirstName, guestLastName, roomId, checkInDate, checkOutDate, statusId));
                }
            }
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookings;
    }
}
