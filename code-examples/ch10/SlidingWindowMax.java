import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/** Maximum of every window of k consecutive values, using a monotonic deque. */
public class SlidingWindowMax {

    // region fast
    static int[] windowMaxima(int[] values, int k) {
        if (k <= 0 || k > values.length) {
            throw new IllegalArgumentException("need 1 <= k <= values.length");
        }
        int[] result = new int[values.length - k + 1];
        Deque<Integer> window = new ArrayDeque<>();  // indices, values decreasing
        for (int i = 0; i < values.length; i++) {
            if (!window.isEmpty() && window.peekFirst() <= i - k) {
                window.pollFirst();                    // front index left the window
            }
            while (!window.isEmpty() && values[window.peekLast()] <= values[i]) {
                window.pollLast();                     // can never be a maximum
            }
            window.offerLast(i);
            if (i >= k - 1) {
                result[i - k + 1] = values[window.peekFirst()];
            }
        }
        return result;
    }
    // endregion fast

    // region brute
    static int[] windowMaximaBrute(int[] values, int k) {
        int[] result = new int[values.length - k + 1];
        for (int start = 0; start + k <= values.length; start++) {
            int max = values[start];
            for (int i = start + 1; i < start + k; i++) {
                max = Math.max(max, values[i]);
            }
            result[start] = max;
        }
        return result;
    }
    // endregion brute

    public static void main(String[] args) {
        int[] values = {1, 3, -1, -3, 5, 3, 6, 7};
        int k = 3;
        System.out.println("values " + Arrays.toString(values) + ", k = " + k);
        System.out.println("maxima " + Arrays.toString(windowMaxima(values, k)));
        System.out.println("brute  " + Arrays.toString(windowMaximaBrute(values, k)));
    }
}
