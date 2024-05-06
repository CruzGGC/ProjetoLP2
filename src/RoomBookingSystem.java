import java.util.Comparator;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class RoomBookingSystem {

    /**
     * Searches for available rooms based on the given criteria.
     * @param rooms is the list of rooms to search from.
     * @param bookings is the list of bookings to check for availability.
     * @param numberOfAdults is the number of adults to accommodate.
     * @param numberOfChildren is the number of children to accommodate.
     * @param checkInDate is the check-in date.
     * @param checkOutDate is the check-out date.
     * @param canceledStatus is the status ID for canceled bookings.
     * @return a list of available rooms.
     */
    public List<Room> searchAvailableRooms(List<Room> rooms, List<Booking> bookings, int numberOfAdults, int numberOfChildren, LocalDate checkInDate, LocalDate checkOutDate, int canceledStatus) {
        return rooms.stream()
                .filter(room -> room.adultsCapacity() >= numberOfAdults && (room.adultsCapacity() + room.childrenCapacity()) >= (numberOfAdults + numberOfChildren))
                .filter(room -> bookings.stream().noneMatch(booking -> booking.roomId() == room.id() &&
                                        ((checkInDate.isBefore(booking.checkOutDate()) &&
                                        checkOutDate.isAfter(booking.checkInDate())) ||
                                        (checkInDate.isEqual(booking.checkInDate()) ||
                                        checkOutDate.isEqual(booking.checkOutDate()))) &&
                                        booking.statusId() != canceledStatus))
                .sorted(Comparator.comparing(Room::price))
                .collect(Collectors.toList());
    }
}