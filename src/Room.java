public class Room {
    private int id;
    private int adultsCapacity;
    private int childrenCapacity;
    private double price;

    public Room(int id, int adultsCapacity, int childrenCapacity, double price) {
        this.id = id;
        this.adultsCapacity = adultsCapacity;
        this.childrenCapacity = childrenCapacity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public int getAdultsCapacity() {
        return adultsCapacity;
    }

    public int getChildrenCapacity() {
        return childrenCapacity;
    }

    public double getPrice() {
        return price;
    }
}