import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/** Exercise 10.10: the first negative number in every window of k values (0 if none). */
public class FirstNegativeInWindow {

    // region solve
    static int[] firstNegatives(int[] values, int k) {
        int[] result = new int[values.length - k + 1];
        Deque<Integer> negatives = new ArrayDeque<>();  // indices of negatives, in order
        for (int i = 0; i < values.length; i++) {
            if (values[i] < 0) {
                negatives.offerLast(i);
            }
            if (!negatives.isEmpty() && negatives.peekFirst() <= i - k) {
                negatives.pollFirst();                  // it has left the window
            }
            if (i >= k - 1) {
                int first = negatives.isEmpty() ? 0 : values[negatives.peekFirst()];
                result[i - k + 1] = first;
            }
        }
        return result;
    }
    // endregion solve

    public static void main(String[] args) {
        int[] values = {12, -1, -7, 8, -15, 30, 16, 28};
        System.out.println(Arrays.toString(firstNegatives(values, 3)));
    }
}
