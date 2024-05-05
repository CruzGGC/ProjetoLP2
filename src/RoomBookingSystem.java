import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RoomBookingSystem {

    public List<Room> searchAvailableRooms(List<Room> rooms, List<Booking> bookings, int numberOfAdults, int numberOfChildren, LocalDate checkInDate, LocalDate checkOutDate, int canceledStatus) {
        for (Booking booking : bookings) {
            System.out.println("Guest First Name: " + booking.getGuestFirstName() + ", Guest Last Name: " + booking.getGuestLastName() + ", Room ID: " + booking.getRoomId() + ", Check-In Date: " + booking.getCheckInDate() + ", Check-Out Date: " + booking.getCheckOutDate() + ", Number of Adults: " + booking.getNumberOfAdults() + ", Number of Children: " + booking.getNumberOfChildren() + ", Status ID: " + booking.getStatusId());
        }
        //filter by room capacity
        List<Room> availableRooms = rooms.stream()
                .filter(room -> room.getAdultsCapacity() >= numberOfAdults && (room.getAdultsCapacity() + room.getChildrenCapacity()) >= (numberOfAdults + numberOfChildren)).collect(Collectors.toList());
        List<Room> copy = new ArrayList<>(availableRooms);
        for (Room room : copy) {
            for (Booking booking : bookings) {
                if (booking.getRoomId() == room.getId()) {
                        /*
                        ------|---------|------ <- booking
                        ----|---|-------------- <- case 1
                        ----------|---|-------- <- case 4 (completely eaten by the booking)
                        --------------|----|--- <- case 3
                        ----|-------------|---- <- case 4 (completely eats the booking)
                        ------|---------|------ <- case 5 (exactly the same)
                        */
                        //case 1/2 - our checkout is in the middle of the booking / which fixes the case 4
                        if (checkOutDate.compareTo(booking.getCheckInDate()) > 0 && checkOutDate.compareTo(booking.getCheckOutDate()) <= 0){
                                // System.out.println("condition 1/2");
                                availableRooms.remove(room);
                        }
                        //case 3 - our checkin is in the middle of the booking
                        else if (checkInDate.compareTo(booking.getCheckInDate()) >= 0 && checkInDate.compareTo(booking.getCheckOutDate()) < 0){
                                // System.out.println("condition 3");
                                availableRooms.remove(room);
                        }
                        //case 4/5 - our booking completely eats the existing booking / exactly the same
                        else if (checkInDate.compareTo(booking.getCheckInDate()) <= 0 && checkOutDate.compareTo(booking.getCheckOutDate()) >= 0){
                                // System.out.println("condition 4/5");
                                availableRooms.remove(room);
                        }
                }
            }
        }
        return availableRooms.stream().sorted(Comparator.comparing(Room::getPrice)).collect(Collectors.toList());

        // return availableRooms;
        // return rooms.stream()
        //     .filter(room -> room.getAdultsCapacity() >= numberOfAdults && (room.getAdultsCapacity() + room.getChildrenCapacity()) >= (numberOfAdults + numberOfChildren))
        //     .filter(room -> bookings.stream().noneMatch(booking -> booking.getRoomId() == room.getId() &&
        //                                                   (checkInDate.compareTo(booking.getCheckInDate()) >= 0 &&
        //                                                    checkInDate.compareTo(booking.getCheckOutDate()) <= 0) ||
        //                                                   (checkOutDate.compareTo(booking.getCheckInDate()) >= 1 &&
        //                                                    checkOutDate.compareTo(booking.getCheckOutDate()) <= 0) &&
        //                                                   booking.getStatusId() != canceledStatus))

        //     .sorted(Comparator.comparing(Room::getPrice))
        //     .collect(Collectors.toList());
    }
}