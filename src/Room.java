import java.util.List;

public class Room {
    private int roomNumber;
    private int adultsCapacity;
    private int childrenCapacity;
    private double price;

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getAdultsCapacity() {
        return adultsCapacity;
    }

    public void setAdultsCapacity(int adultsCapacity) {
        this.adultsCapacity = adultsCapacity;
    }

    public int getChildrenCapacity() {
        return childrenCapacity;
    }

    public void setChildrenCapacity(int childrenCapacity) {
        this.childrenCapacity = childrenCapacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Constructor
    public Room(int roomNumber, int adultsCapacity, int childrenCapacity, double price) {
        this.roomNumber = roomNumber;
        this.adultsCapacity = adultsCapacity;
        this.childrenCapacity = childrenCapacity;
        this.price = price;
    }

    // Getters and setters
    // ...

    public static void main(String[] args) {
        String filePath = "src/Tables/RT.csv";
        List<Room> rooms = TableCreator.readRoomsFromCSV(filePath);

        // Now you have a list of Room objects populated with data from the CSV file
        // You can use this list as needed
    }
}