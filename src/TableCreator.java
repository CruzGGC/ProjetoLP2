import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.io.FileWriter;
import java.io.IOException;

/**TableCreator calss is responsible for handling operastions such as loading, writing table data
 * to the CSV and retrieving specific data related to the JTable
   */
public class TableCreator {

    /**
     * Writes the data from the JTable into the CSV file.
     * @param table is the JTable to get the data from.
     * @param filePath is the path of the CSV file to write the data to.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public void writeTableDataToCSV(JTable table, String filePath) {
        try {
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

    /**
     * Loads the data from the CSV file into the JTable.
     * @param table is the JTable to load the data into.
     * @param filePath is the path of the CSV file to load the data from.
     */
    @SuppressWarnings("CallToPrintStackTrace")
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

    /**
     * Loads Room data from the CSV file into the list of Room objects.
     * @param filePath is the path of the CSV file to get the data from.
     * @return A list of Room objects.
     */
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

    /**
     * Loads Booking data from the CSV file into the list of Booking objects.
     * @param filePath is the path of the CSV file to get the data from.
     * @return A list of Booking objects.
     */
    public List<Booking> loadBookingsFromCSV(String filePath) {
        List<Booking> bookings = new ArrayList<>();
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
            String row;
            csvReader.readLine(); // Skip the header
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                if (data.length > 1 && !data[0].isEmpty() && !data[1].isEmpty()) { // Check if the row is not completely empty
                    int roomId = data.length > 2 && !data[2].isEmpty() ? Integer.parseInt(data[2]) : 0;
                    LocalDate checkInDate = data.length > 3 && !data[3].isEmpty() ? LocalDate.parse(data[3]) : null;
                    LocalDate checkOutDate = data.length > 4 && !data[4].isEmpty() ? LocalDate.parse(data[4]) : null;
                    int statusId = data.length > 7 && !data[7].isEmpty() ? Integer.parseInt(data[7]) : 0;

                    bookings.add(new Booking(roomId, checkInDate, checkOutDate, statusId));
                }
            }
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    /**
     * Retrieves the Room object by its ID from the CSV file.
     * @param roomId is the ID of the Room to retrieve.
     * @param filePath is the path of the CSV file to get the data from.
     * @return A Room object if found, null otherwise.
     */
    public Room getRoomById(int roomId, String filePath) {
        List<Room> rooms = loadRoomsFromCSV(filePath);
        for (Room room : rooms) {
            if (room.id() == roomId) {
                return room;
            }
        }
        return null;
    }

    /**
     * Loads check-in data from the CSV file into the DefaultTableModel.
     * @param filePath is the path of the CSV file to get the data from.
     * @return A DefaultTableModel containing the check-in data.
     */
    public DefaultTableModel loadCheckInsFromCSV(String filePath) {
        List<String[]> filteredData = new ArrayList<>();
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
            String row;
            csvReader.readLine(); // Skip the header
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                if (data.length > 7 && data[7].equals("1")) { // Check if the Status is 1
                    // Create a new array with only the values for Guest First Name, Guest Last Name, Room, and Check-In
                    String[] filteredRow = {data[0], data[1], data[2], data[3]};
                    filteredData.add(filteredRow);
                }
            }
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Creates the DefaultTableModel with the filtered data and the specified column names

        return new DefaultTableModel(
                filteredData.toArray(new String[0][]),
                new String[] {"Guest First Name", "Guest Last Name", "Room", "Check-In"}
        );
    }

    /**
     * Loads check-out data from the CSV file into the DefaultTableModel.
     * @param filePath is the path of the CSV file to get the data from.
     * @return A DefaultTableModel containing the check-out data.
     */
    public DefaultTableModel loadCheckOutsFromCSV(String filePath) {
        List<String[]> filteredData = new ArrayList<>();
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
            String row;
            csvReader.readLine(); // Skip the header
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                if (data.length > 7 && data[7].equals("2")) { // Check if the Status is 2
                    // Create a new array with only the values for Guest First Name, Guest Last Name, Room, and Check-Out
                    String[] filteredRow = {data[0], data[1], data[2], data[4]};
                    filteredData.add(filteredRow);
                }
            }
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Creates the DefaultTableModel with the filtered data and the specified column names

        return new DefaultTableModel(
                filteredData.toArray(new String[0][]),
                new String[] {"Guest First Name", "Guest Last Name", "Room", "Check-Out"}
        );
    }
}
