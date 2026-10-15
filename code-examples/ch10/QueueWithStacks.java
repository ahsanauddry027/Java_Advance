import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

/** A FIFO queue built from two LIFO stacks; each operation is amortized O(1). */
public class QueueWithStacks<T> {

    // region queue
    private final Deque<T> inbox = new ArrayDeque<>();   // receives new elements
    private final Deque<T> outbox = new ArrayDeque<>();  // serves the oldest elements

    public void enqueue(T value) {
        inbox.push(value);
    }

    public T dequeue() {
        refillOutboxIfEmpty();
        if (outbox.isEmpty()) {
            throw new NoSuchElementException("dequeue from an empty queue");
        }
        return outbox.pop();
    }

    public T peek() {
        refillOutboxIfEmpty();
        if (outbox.isEmpty()) {
            throw new NoSuchElementException("peek at an empty queue");
        }
        return outbox.peek();
    }

    public boolean isEmpty() {
        return inbox.isEmpty() && outbox.isEmpty();
    }

    private void refillOutboxIfEmpty() {
        if (outbox.isEmpty()) {
            while (!inbox.isEmpty()) {
                outbox.push(inbox.pop());  // reverses the order: oldest ends on top
            }
        }
    }
    // endregion queue

    public static void main(String[] args) {
        QueueWithStacks<String> queue = new QueueWithStacks<>();
        queue.enqueue("a");
        queue.enqueue("b");
        queue.enqueue("c");
        System.out.println("dequeue -> " + queue.dequeue());
        queue.enqueue("d");
        StringBuilder rest = new StringBuilder();
        while (!queue.isEmpty()) {
            rest.append(queue.dequeue()).append(' ');
        }
        System.out.println("then    -> " + rest.toString().trim());
    }
}
