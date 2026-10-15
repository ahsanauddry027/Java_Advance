/** Exercise 1.10: n! modulo 1,000,000,007 for n up to 10^6. */
public class FactorialMod {

    static final int MOD = 1_000_000_007;

    // region solve
    static int factorialMod(int n) {
        long result = 1;                       // long: result * i can reach about 10^15
        for (int i = 2; i <= n; i++) {
            result = result * i % MOD;         // reduce after EVERY multiplication
        }
        return (int) result;                   // < MOD, so it fits in an int
    }
    // endregion solve

    // region buggy
    static int factorialModBuggy(int n) {
        int result = 1;
        for (int i = 2; i <= n; i++) {
            result = result * i % MOD;         // int * int overflows before the %
        }
        return result;
    }
    // endregion buggy

    public static void main(String[] args) {
        for (int n : new int[] {5, 13, 20, 1_000_000}) {
            System.out.printf("n = %,9d: correct %,13d   buggy %,14d%n",
                    n, factorialMod(n), factorialModBuggy(n));
        }
    }
}
