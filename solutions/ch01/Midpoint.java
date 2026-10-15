/** Exercise 1.2: three ways to compute the middle index between low and high. */
public class Midpoint {

    // region solve
    static int midOverflowing(int low, int high) {
        return (low + high) / 2;              // low + high can exceed Integer.MAX_VALUE
    }

    static int midSafe(int low, int high) {
        return low + (high - low) / 2;        // no overflow when 0 <= low <= high
    }

    static int midUnsignedShift(int low, int high) {
        return (low + high) >>> 1;            // treats the overflowed sum as unsigned
    }
    // endregion solve

    public static void main(String[] args) {
        int low = 1_500_000_000;
        int high = 2_000_000_000;
        System.out.println("overflowing:    " + midOverflowing(low, high));
        System.out.println("safe:           " + midSafe(low, high));
        System.out.println("unsigned shift: " + midUnsignedShift(low, high));
    }
}
