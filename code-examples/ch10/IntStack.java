import java.util.Arrays;
import java.util.NoSuchElementException;

/** A last-in, first-out stack of int values stored in a growable array. */
public class IntStack {

    private int[] items = new int[2];  // items[0..size-1] hold the stack
    private int size;                  // how many values are on the stack

    public void push(int value) {
        if (size == items.length) {
            items = Arrays.copyOf(items, items.length * 2);  // full: double the array
        }
        items[size] = value;          // the top is always items[size - 1]
        size++;
    }

    public int pop() {
        if (size == 0) {
            throw new NoSuchElementException("pop from an empty stack");
        }
        size--;
        return items[size];
    }

    public int peek() {
        if (size == 0) {
            throw new NoSuchElementException("peek at an empty stack");
        }
        return items[size - 1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    int capacity() {  // package-private: only for the demonstration below
        return items.length;
    }

    public static void main(String[] args) {
        IntStack stack = new IntStack();
        for (int value = 10; value <= 50; value += 10) {
            stack.push(value);
            System.out.printf("push %d  -> size %d, capacity %d%n",
                    value, stack.size(), stack.capacity());
        }
        System.out.println("peek    -> " + stack.peek());
        StringBuilder popped = new StringBuilder();
        while (!stack.isEmpty()) {
            popped.append(stack.pop()).append(' ');
        }
        System.out.println("popped  -> " + popped.toString().trim());
        try {
            stack.pop();
        } catch (NoSuchElementException e) {
            System.out.println("pop on empty stack -> " + e.getMessage());
        }
    }
}
