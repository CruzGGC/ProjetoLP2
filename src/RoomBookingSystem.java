import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class RoomBookingSystem {

    public List<Room> searchAvailableRooms(List<Room> rooms, List<Booking> bookings, int numberOfAdults, int numberOfChildren, Date checkInDate, Date checkOutDate, int canceledStatus) {
        return rooms.stream()
            .filter(Room -> Room.getAdultsCapacity() >= numberOfAdults && (Room.getAdultsCapacity() + Room.getChildrenCapacity()) >= (numberOfAdults + numberOfChildren))
            .filter(Room -> bookings.stream().noneMatch(Booking -> Booking.getRoomId() == Room.getRoomNumber() &&
                                                          (checkInDate.compareTo(Booking.getCheckInDate()) >= 0 &&
                                                           checkInDate.compareTo(Booking.getCheckOutDate()) <= 0) ||
                                                          (checkOutDate.compareTo(Booking.getCheckInDate()) >= 1 &&
                                                           checkOutDate.compareTo(Booking.getCheckOutDate()) <= 0) &&
                                                          Booking.getRoomId() != canceledStatus))

            .sorted(Comparator.comparing(Room::getPrice))
            .collect(Collectors.toList());
    }
}
