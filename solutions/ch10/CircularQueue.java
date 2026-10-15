/** Exercise 10.3: a fixed-capacity circular queue of ints. */
public class CircularQueue {

    // region solve
    private final int[] items;
    private int head;  // index of the front element
    private int size;

    public CircularQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        items = new int[capacity];
    }

    /** Adds value at the back; returns false if the queue is full. */
    public boolean enqueue(int value) {
        if (isFull()) {
            return false;
        }
        items[(head + size) % items.length] = value;
        size++;
        return true;
    }

    /** Removes the front value; returns false if the queue is empty. */
    public boolean dequeue() {
        if (isEmpty()) {
            return false;
        }
        head = (head + 1) % items.length;
        size--;
        return true;
    }

    public int front() {
        return isEmpty() ? -1 : items[head];
    }

    public int rear() {
        return isEmpty() ? -1 : items[(head + size - 1) % items.length];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == items.length;
    }
    // endregion solve

    public static void main(String[] args) {
        CircularQueue queue = new CircularQueue(3);
        System.out.println(queue.enqueue(1) + " " + queue.enqueue(2) + " "
                + queue.enqueue(3) + " " + queue.enqueue(4));   // the 4th does not fit
        System.out.println("rear " + queue.rear() + ", full " + queue.isFull());
        boolean removed = queue.dequeue();
        boolean added = queue.enqueue(4);
        System.out.println("dequeue " + removed + ", enqueue 4 " + added);
        System.out.println("front " + queue.front() + ", rear " + queue.rear());
    }
}
