import java.util.Date;
import java.util.List;

public class Booking {
    private String guestFirstName;
    private String guestLastName;
    private int room;
    private Date checkIn;
    private Date checkOut;
    private int status;

    public Date getCheckInDate() {
        return this.checkIn;
    }

    public void setCheckInDate(Date checkIn) {
        this.checkIn = checkIn;
    }

    public Date getCheckOutDate() {
        return this.checkOut;
    }

    public void setCheckOutDate(Date checkOut) {
        this.checkOut = checkOut;
    }

    public int getRoomId() {
        return this.room;
    }

    public void setRoomId(int room) {
        this.room = room;
    }

    public Booking(String guestFirstName, String guestLastName, int room, Date checkIn, Date checkOut, int status) {
        this.guestFirstName = guestFirstName;
        this.guestLastName = guestLastName;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
    }

    public static void main(String[] args) {
        String filePath = "src/Tables/BT.csv";
        List<Booking> bookings = TableCreator.readBookingsFromCSV(filePath);

        // Now you have a list of Booking objects populated with data from the CSV file
        // You can use this list as needed
        }
    }