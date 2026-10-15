import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

/** Exercise 10.4: a stack that also reports its minimum in O(1). */
public class MinStack {

    // region solve
    /** Each entry holds a value and the stack's minimum at the moment it was pushed. */
    private final Deque<int[]> entries = new ArrayDeque<>();

    public void push(int value) {
        int min = entries.isEmpty() ? value : Math.min(value, entries.peek()[1]);
        entries.push(new int[] {value, min});
    }

    public int pop() {
        requireNotEmpty();
        return entries.pop()[0];
    }

    public int top() {
        requireNotEmpty();
        return entries.peek()[0];
    }

    public int getMin() {
        requireNotEmpty();
        return entries.peek()[1];
    }

    private void requireNotEmpty() {
        if (entries.isEmpty()) {
            throw new NoSuchElementException("empty stack");
        }
    }
    // endregion solve

    public static void main(String[] args) {
        MinStack stack = new MinStack();
        stack.push(5);
        stack.push(3);
        stack.push(7);
        stack.push(3);
        System.out.println("min " + stack.getMin());
        stack.pop();
        System.out.println("after pop, min " + stack.getMin());
        stack.pop();
        stack.pop();
        System.out.println("after two more pops, min " + stack.getMin());
    }
}
