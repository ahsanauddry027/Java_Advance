/** Exercise 1.5: summing ints without silent overflow. */
public class SafeSum {

    // region solve
    /** Correct for any int array: the total of up to 2^31 ints always fits in a long. */
    static long sumAsLong(int[] values) {
        long total = 0;
        for (int value : values) {
            total += value;                  // value is widened to long before adding
        }
        return total;
    }

    /** Returns an int sum, but throws ArithmeticException instead of overflowing. */
    static int sumExact(int[] values) {
        int total = 0;
        for (int value : values) {
            total = Math.addExact(total, value);
        }
        return total;
    }
    // endregion solve

    public static void main(String[] args) {
        int[] big = {2_000_000_000, 2_000_000_000, -1_000_000_000};
        System.out.println("long sum: " + sumAsLong(big));
        try {
            System.out.println("exact sum: " + sumExact(big));
        } catch (ArithmeticException e) {
            System.out.println("exact sum: " + e);
        }
    }
}
