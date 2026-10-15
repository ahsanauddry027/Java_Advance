import java.math.BigInteger;

/** Exercise 2.9: questions about numbers far beyond the range of long. */
public class BigDigits {

    // region solve
    static int digitSumOfFactorial(int n) {
        BigInteger factorial = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            factorial = factorial.multiply(BigInteger.valueOf(i));
        }
        int sum = 0;
        for (char digit : factorial.toString().toCharArray()) {
            sum += digit - '0';
        }
        return sum;
    }

    static int digitsOfPowerOfTwo(int exponent) {
        return BigInteger.TWO.pow(exponent).toString().length();
    }
    // endregion solve

    public static void main(String[] args) {
        System.out.println("digit sum of 100! = " + digitSumOfFactorial(100));
        System.out.println("digits in 2^1000  = " + digitsOfPowerOfTwo(1000));
    }
}
