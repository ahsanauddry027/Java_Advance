import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Queue;

/** A LIFO stack built from one FIFO queue: push is O(n), pop and peek are O(1). */
public class StackWithQueue<T> {

    // region stack
    private final Queue<T> queue = new ArrayDeque<>();  // queue front = stack top

    public void push(T value) {
        queue.offer(value);
        for (int i = 1; i < queue.size(); i++) {
            queue.offer(queue.poll());  // rotate the older elements behind the new one
        }
    }

    public T pop() {
        if (queue.isEmpty()) {
            throw new NoSuchElementException("pop from an empty stack");
        }
        return queue.poll();
    }

    public T peek() {
        if (queue.isEmpty()) {
            throw new NoSuchElementException("peek at an empty stack");
        }
        return queue.peek();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
    // endregion stack

    public static void main(String[] args) {
        StackWithQueue<Integer> stack = new StackWithQueue<>();
        for (int value = 1; value <= 4; value++) {
            stack.push(value);
        }
        StringBuilder order = new StringBuilder();
        while (!stack.isEmpty()) {
            order.append(stack.pop()).append(' ');
        }
        System.out.println("pop order: " + order.toString().trim());
    }
}
