/**
 * Exercise 10.12: the sum of min(subarray) over every contiguous subarray, modulo
 * 1_000_000_007.
 */
public class SumOfSubarrayMinimums {

    static final long MOD = 1_000_000_007L;

    // region solve
    static int sumOfMinimums(int[] values) {
        int n = values.length;
        int[] left = new int[n];    // index of previous value that is STRICTLY smaller
        int[] right = new int[n];   // index of next value that is smaller OR EQUAL
        int[] stack = new int[n];
        int top = 0;
        for (int i = 0; i < n; i++) {
            while (top > 0 && values[stack[top - 1]] >= values[i]) {
                top--;
            }
            left[i] = (top == 0) ? -1 : stack[top - 1];
            stack[top++] = i;
        }
        top = 0;
        for (int i = n - 1; i >= 0; i--) {
            while (top > 0 && values[stack[top - 1]] > values[i]) {
                top--;
            }
            right[i] = (top == 0) ? n : stack[top - 1];
            stack[top++] = i;
        }
        long total = 0;
        for (int i = 0; i < n; i++) {
            // values[i] is the minimum of exactly this many subarrays:
            long subarrays = (long) (i - left[i]) * (right[i] - i);
            total = (total + values[i] % MOD * (subarrays % MOD)) % MOD;
        }
        return (int) total;
    }
    // endregion solve

    public static void main(String[] args) {
        System.out.println(sumOfMinimums(new int[] {3, 1, 2, 4}));
        System.out.println(sumOfMinimums(new int[] {11, 81, 94, 43, 3}));
        System.out.println(sumOfMinimums(new int[] {2, 2, 2}));
    }
}
