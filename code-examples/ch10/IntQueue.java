import java.util.NoSuchElementException;

/** A first-in, first-out queue of int values stored in a circular, growable array. */
public class IntQueue {

    // region queue
    private int[] items = new int[4];
    private int head;  // index of the front value
    private int size;  // how many values are in the queue

    public void enqueue(int value) {
        if (size == items.length) {
            grow();
        }
        int tail = (head + size) % items.length;  // first free slot after the back
        items[tail] = value;
        size++;
    }

    public int dequeue() {
        if (size == 0) {
            throw new NoSuchElementException("dequeue from an empty queue");
        }
        int value = items[head];
        head = (head + 1) % items.length;  // step forward, wrapping to 0 at the end
        size--;
        return value;
    }

    public int peek() {
        if (size == 0) {
            throw new NoSuchElementException("peek at an empty queue");
        }
        return items[head];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void grow() {
        int[] bigger = new int[items.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = items[(head + i) % items.length];  // copy in queue order
        }
        items = bigger;
        head = 0;
    }
    // endregion queue

    String layout() {  // package-private: shows the raw array for the demonstration
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < items.length; i++) {
            boolean used = (i - head + items.length) % items.length < size;
            sb.append(used ? String.valueOf(items[i]) : "_");
            sb.append(i + 1 < items.length ? " " : "]");
        }
        return sb + " head=" + head + " size=" + size;
    }

    public static void main(String[] args) {
        IntQueue queue = new IntQueue();
        for (int value = 1; value <= 4; value++) {
            queue.enqueue(value);
        }
        System.out.println("after enqueue 1..4   " + queue.layout());
        int first = queue.dequeue();
        int second = queue.dequeue();
        System.out.println("dequeue -> " + first + ", dequeue -> " + second);
        System.out.println("after two dequeues   " + queue.layout());
        queue.enqueue(5);
        queue.enqueue(6);
        System.out.println("after enqueue 5, 6   " + queue.layout());
        queue.enqueue(7);
        System.out.println("after enqueue 7      " + queue.layout());
        StringBuilder order = new StringBuilder();
        while (!queue.isEmpty()) {
            order.append(queue.dequeue()).append(' ');
        }
        System.out.println("dequeue order: " + order.toString().trim());
    }
}
