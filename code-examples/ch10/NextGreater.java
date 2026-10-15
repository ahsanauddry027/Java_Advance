import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/** Next greater element: for each position, the first larger value to its right. */
public class NextGreater {

    // region brute
    static int[] nextGreaterBrute(int[] values) {
        int[] result = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = -1;
            for (int j = i + 1; j < values.length; j++) {
                if (values[j] > values[i]) {
                    result[i] = values[j];
                    break;
                }
            }
        }
        return result;
    }
    // endregion brute

    // region fast
    static int[] nextGreater(int[] values) {
        int[] result = new int[values.length];
        Arrays.fill(result, -1);                      // -1 means "no greater value"
        Deque<Integer> waiting = new ArrayDeque<>();  // indices still without an answer
        for (int i = 0; i < values.length; i++) {
            while (!waiting.isEmpty() && values[waiting.peek()] < values[i]) {
                result[waiting.pop()] = values[i];    // values[i] answers that index
            }
            waiting.push(i);
        }
        return result;                                // indices left waiting keep -1
    }
    // endregion fast

    // region smaller
    /** Index of the previous strictly smaller value for each position, or -1. */
    static int[] previousSmallerIndex(int[] values) {
        int[] result = new int[values.length];
        Deque<Integer> candidates = new ArrayDeque<>();  // values increase toward the top
        for (int i = 0; i < values.length; i++) {
            while (!candidates.isEmpty() && values[candidates.peek()] >= values[i]) {
                candidates.pop();                         // can never be an answer again
            }
            result[i] = candidates.isEmpty() ? -1 : candidates.peek();
            candidates.push(i);
        }
        return result;
    }
    // endregion smaller

    public static void main(String[] args) {
        int[] values = {2, 7, 3, 5, 4, 6, 8};
        System.out.println("values           " + Arrays.toString(values));
        System.out.println("next greater     " + Arrays.toString(nextGreater(values)));
        int[] brute = nextGreaterBrute(values);
        System.out.println("brute force      " + Arrays.toString(brute));
        int[] previous = previousSmallerIndex(values);
        System.out.println("prev smaller idx " + Arrays.toString(previous));
    }
}
