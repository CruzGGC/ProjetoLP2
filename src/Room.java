/**
 * A record class that represents a Room.
 * A Room has an id, adultsCapacity, childrenCapacity, and price.
 *
 * @param id The unique identifier for the room.
 * @param adultsCapacity The maximum number of adults that the room can accommodate.
 * @param childrenCapacity The maximum number of children that the room can accommodate.
 * @param price The price per night for the room.
 */
public record Room(int id, int adultsCapacity, int childrenCapacity, double price) {
}