/** Arithmetic results that surprise people: overflow, division, floating point, chars. */
public class NumericTraps {

    public static void main(String[] args) {
        // region overflow
        int big = Integer.MAX_VALUE;                      // 2147483647
        System.out.println("MAX_VALUE + 1    = " + (big + 1));
        long widened = big + 1L;                          // 1L is a long: the sum is long
        System.out.println("MAX_VALUE + 1L   = " + widened);
        try {
            Math.addExact(big, 1);                        // checks for overflow
        } catch (ArithmeticException e) {
            System.out.println("Math.addExact    -> " + e);
        }
        // endregion overflow

        // region widening
        int a = 100_000;
        int b = 100_000;
        long wrong = a * b;          // int * int overflows BEFORE the result becomes long
        long right = (long) a * b;   // a becomes long first, so the product is long
        System.out.println("long wrong = a * b        = " + wrong);
        System.out.println("long right = (long) a * b = " + right);
        // endregion widening

        // region division
        System.out.println(" 7 / 2 = " + (7 / 2) + ",   7 % 2 = " + (7 % 2));
        System.out.println("-7 / 2 = " + (-7 / 2) + ",  -7 % 2 = " + (-7 % 2));
        System.out.println("Math.floorDiv(-7, 2) = " + Math.floorDiv(-7, 2)
                + ", Math.floorMod(-7, 2) = " + Math.floorMod(-7, 2));
        System.out.println("7 / 2.0 = " + (7 / 2.0));
        // endregion division

        // region floating
        double sum = 0.1 + 0.2;
        System.out.println("0.1 + 0.2 = " + sum + ", == 0.3? " + (sum == 0.3));
        System.out.println("within 1e-9 of 0.3? " + (Math.abs(sum - 0.3) < 1e-9));
        System.out.println("(int) 3.99 = " + (int) 3.99
                + ", (int) -3.99 = " + (int) -3.99);
        System.out.println("Math.round(2.5) = " + Math.round(2.5)
                + ", Math.round(-2.5) = " + Math.round(-2.5));
        System.out.println("(int) 1e10 = " + (int) 1e10);
        System.out.println("1.0 / 0 = " + (1.0 / 0) + ", 0.0 / 0 = " + (0.0 / 0));
        // endregion floating

        // region chars
        char letter = 'c';
        System.out.println("'c' - 'a' = " + (letter - 'a'));
        System.out.println("'a' + 1   = " + ('a' + 1));
        System.out.println("(char) ('a' + 1) = " + (char) ('a' + 1));
        System.out.println("'7' - '0' = " + ('7' - '0'));
        // endregion chars

        // region midpoint
        int low = 2_000_000_000;
        int high = 2_100_000_000;
        System.out.println("(low + high) / 2       = " + (low + high) / 2);
        System.out.println("low + (high - low) / 2 = " + (low + (high - low) / 2));
        // endregion midpoint
    }
}
