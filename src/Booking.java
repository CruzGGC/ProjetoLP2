import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Date;

public class Booking {
    private String guestFirstName;
    private String guestLastName;
    private int roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int statusId;

    public Booking(String guestFirstName, String guestLastName, int roomId, LocalDate checkInDate, LocalDate checkOutDate, int statusId) {
        this.guestFirstName = guestFirstName;
        this.guestLastName = guestLastName;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.statusId = statusId;
    }

    public String getGuestFirstName() {
        return guestFirstName;
    }

    public void setGuestFirstName(String guestFirstName) {
        this.guestFirstName = guestFirstName;
    }

    public String getGuestLastName() {
        return guestLastName;
    }

    public void setGuestLastName(String guestLastName) {
        this.guestLastName = guestLastName;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
}