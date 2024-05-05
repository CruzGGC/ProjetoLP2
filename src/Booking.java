import java.time.LocalDate;

public record Booking(int roomId, LocalDate checkInDate, LocalDate checkOutDate, int statusId) {
}