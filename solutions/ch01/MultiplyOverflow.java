/** Exercise 1.12: detect multiplication overflow without Math.multiplyExact. */
public class MultiplyOverflow {

    // region solve
    /** For int: compute the exact product in a long and see whether it fits in an int. */
    static boolean multiplyOverflows(int a, int b) {
        long exact = (long) a * b;            // any int * int fits in a long
        return exact < Integer.MIN_VALUE || exact > Integer.MAX_VALUE;
    }

    /** For long there is no wider type, so check by dividing the result back. */
    static boolean multiplyOverflows(long a, long b) {
        if (a == 0 || b == 0) {
            return false;
        }
        if ((a == -1 && b == Long.MIN_VALUE) || (b == -1 && a == Long.MIN_VALUE)) {
            return true;                            // -MIN_VALUE does not exist in long
        }
        long product = a * b;                       // may have wrapped around
        return product / b != a;                    // wrapped results fail this test
    }
    // endregion solve

    public static void main(String[] args) {
        System.out.println(multiplyOverflows(46_340, 46_340) + " "
                + multiplyOverflows(46_341, 46_341));
        System.out.println(multiplyOverflows(3_037_000_499L, 3_037_000_499L) + " "
                + multiplyOverflows(3_037_000_500L, 3_037_000_500L));
        System.out.println(multiplyOverflows(-1L, Long.MIN_VALUE));
    }
}
