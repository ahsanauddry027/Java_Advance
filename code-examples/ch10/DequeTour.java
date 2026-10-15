import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Stack;

/** How ArrayDeque behaves as a stack and as a queue, and how legacy Stack differs. */
public class DequeTour {

    public static void main(String[] args) {
        // region as-stack
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);                                 // 3 is now the top (the front)
        System.out.println("stack        " + stack);  // printed from the top down
        System.out.println("pop()        " + stack.pop());
        System.out.println("peek()       " + stack.peek());
        // endregion as-stack

        // region as-queue
        Deque<Integer> queue = new ArrayDeque<>();
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);                                // 3 is at the back
        System.out.println("queue        " + queue);  // printed from the front
        System.out.println("poll()       " + queue.poll());
        System.out.println("peek()       " + queue.peek());
        // endregion as-queue

        // region empty
        Deque<Integer> empty = new ArrayDeque<>();
        System.out.println("empty peek() " + empty.peek());   // null, no exception
        System.out.println("empty poll() " + empty.poll());   // null, no exception
        try {
            empty.pop();                                         // throws
        } catch (NoSuchElementException e) {
            System.out.println("empty pop()  NoSuchElementException");
        }
        try {
            empty.push(null);                                    // nulls are not allowed
        } catch (NullPointerException e) {
            System.out.println("push(null)   NullPointerException");
        }
        // endregion empty

        // region legacy
        Stack<Integer> legacy = new Stack<>();
        legacy.push(1);
        legacy.push(2);
        legacy.push(3);
        System.out.println("legacy Stack " + legacy);  // printed from the BOTTOM up
        legacy.add(0, 99);                             // compiles: Stack is a Vector
        System.out.println("add(0, 99)   " + legacy);
        // endregion legacy
    }
}
