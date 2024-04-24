import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.Arrays;
import java.util.Vector;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RoomTableCreator {

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
                    csvWriter.append(table.getValueAt(i, j).toString());
                    if (j != table.getColumnCount() - 1) {
                        csvWriter.append(",");
                    }
                }
                csvWriter.append("\n");
            }

            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTableDataFromCSV(JTable table, String filePath) {
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

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table.setModel(model);
    }

}
