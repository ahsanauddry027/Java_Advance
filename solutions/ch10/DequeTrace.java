import java.util.ArrayDeque;
import java.util.Deque;

/** Exercise 10.1: runs the trace from the exercise so you can check your prediction. */
public class DequeTrace {

    public static void main(String[] args) {
        Deque<Integer> d = new ArrayDeque<>();
        d.push(1);
        d.offer(2);
        d.push(3);
        d.offerFirst(4);
        d.addLast(5);
        System.out.println(d);
        System.out.println(d.pop() + " " + d.pollLast() + " " + d.peek());
    }
}
