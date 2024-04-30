import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

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
    public static List<Room> readRoomsFromCSV(String filePath) {
        List<Room> rooms = new ArrayList<>();
        TableCreator tableCreator = new TableCreator();
        JTable table = new JTable();
        tableCreator.loadTableDataFromCSV(table, filePath);

        for (int i = 0; i < table.getRowCount(); i++) {
            int roomNumber = Integer.parseInt(table.getValueAt(i, 0).toString());
            int adultsCapacity = Integer.parseInt(table.getValueAt(i, 1).toString());
            int childrenCapacity = Integer.parseInt(table.getValueAt(i, 2).toString());
            double price = Double.parseDouble(table.getValueAt(i, 3).toString());

            Room room = new Room(roomNumber, adultsCapacity, childrenCapacity, price);
            rooms.add(room);
        }

        return rooms;
    }

    public static List<Booking> readBookingsFromCSV(String filePath) {
        List<Booking> bookings = new ArrayList<>();
        TableCreator tableCreator = new TableCreator();
        JTable table = new JTable();
        tableCreator.loadTableDataFromCSV(table, filePath);

        for (int i = 0; i < table.getRowCount(); i++) {
            // Check if the row is empty
            if (table.getValueAt(i, 0) == null || table.getValueAt(i, 0).toString().trim().isEmpty()) {
                continue; // Skip this iteration if the row is empty
            }

            String guestFirstName = table.getValueAt(i, 0).toString();
            String guestLastName = table.getValueAt(i, 1).toString();
            int roomId = Integer.parseInt(table.getValueAt(i, 2).toString());
            Date checkInDate = parseDate(table.getValueAt(i, 3).toString());
            Date checkOutDate = parseDate(table.getValueAt(i, 4).toString());
            int statusId = Integer.parseInt(table.getValueAt(i, 5).toString());

            Booking booking = new Booking(guestFirstName, guestLastName, roomId, checkInDate, checkOutDate, statusId);
            bookings.add(booking);
        }

        return bookings;
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void displayAvailableRooms(List<Room> rooms) {
        for (Room room : rooms) {
            System.out.println(room);
        }
    }
}
