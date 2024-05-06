import java.time.LocalDate;

/**
 * A record class that represents the Booking.
 * Booking  has a roomId, checkInDate, checkOutDate, and statusId.
 *
 * @param roomId The unique identifier for the room associated with the booking.
 * @param checkInDate The check-in date for the booking.
 * @param checkOutDate The check-out date for the booking.
 * @param statusId The status of the booking. This could be an identifier for statuses like 'booked', 'checked-in', 'checked-out', etc.
 */
public record Booking(int roomId, LocalDate checkInDate, LocalDate checkOutDate, int statusId) {
}