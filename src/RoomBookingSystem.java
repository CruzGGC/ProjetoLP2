import java.util.ArrayList;
import java.util.Comparator;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/*public class RoomBookingSystem {

    public List<Room> searchAvailableRooms(List<Room> rooms, List<Booking> bookings, int numberOfAdults, int numberOfChildren, LocalDate checkInDate, LocalDate checkOutDate, int canceledStatus) {
        return rooms.stream()
            .filter(room -> room.getAdultsCapacity() >= numberOfAdults && (room.getAdultsCapacity() + room.getChildrenCapacity()) >= (numberOfAdults + numberOfChildren))
            .filter(room -> bookings.stream().noneMatch(booking -> booking.getRoomId() == room.getId() &&
                                                          (checkInDate.compareTo(booking.getCheckInDate()) >= 0 &&
                                                           checkInDate.compareTo(booking.getCheckOutDate()) <= 0) ||
                                                          (checkOutDate.compareTo(booking.getCheckInDate()) >= 1 &&
                                                           checkOutDate.compareTo(booking.getCheckOutDate()) <= 0) &&
                                                          booking.getStatusId() != canceledStatus))

            .sorted(Comparator.comparing(Room::getPrice))
            .collect(Collectors.toList());
    }
}*/

public class RoomBookingSystem {
    public List<Room> searchAvailableRooms(List<Room> rooms, List<Booking> bookings, int numberOfAdults, int numberOfChildren, LocalDate checkInDate, LocalDate checkOutDate, int canceledStatus) {
        return rooms.stream()
                .filter(room -> room.getAdultsCapacity() >= numberOfAdults && (room.getAdultsCapacity() + room.getChildrenCapacity()) >= (numberOfAdults + numberOfChildren))
                .filter(room -> bookings.stream().noneMatch(booking -> booking.getRoomId() == room.getId() &&
                        ((checkInDate.isBefore(booking.getCheckOutDate()) && checkOutDate.isAfter(booking.getCheckInDate())) ||
                                (checkInDate.isEqual(booking.getCheckInDate()) || checkOutDate.isEqual(booking.getCheckOutDate())))))
                .sorted(Comparator.comparing(Room::getPrice))
                .collect(Collectors.toList());
    }
}