import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/** Arbitrary-size integers and exact decimals, and the traps in using them. */
public class BigNumbers {

    public static void main(String[] args) {
        // region biginteger
        BigInteger factorial = BigInteger.ONE;
        for (int i = 2; i <= 30; i++) {
            factorial = factorial.multiply(BigInteger.valueOf(i));  // a NEW object
        }
        System.out.println("30! = " + factorial);
        BigInteger twoTo100 = BigInteger.TWO.pow(100);
        System.out.println("2^100 = " + twoTo100 + " (" + twoTo100.toString().length()
                + " digits)");
        BigInteger mod = BigInteger.valueOf(1_000_000_007);
        System.out.println("2^100 mod 1e9+7 = " + twoTo100.mod(mod)
                + ", via modPow: " + BigInteger.TWO.modPow(BigInteger.valueOf(100), mod));
        System.out.println("compareTo: " + factorial.compareTo(twoTo100)
                + ", equals parsed 30!: "
                + factorial.equals(new BigInteger("265252859812191058636308480000000")));
        // endregion biginteger

        // region bigdecimal
        BigDecimal a = new BigDecimal("0.1");      // from a String: exactly 0.1
        BigDecimal b = new BigDecimal("0.2");
        System.out.println("0.1 + 0.2 = " + a.add(b));
        System.out.println("new BigDecimal(0.1) = " + new BigDecimal(0.1));  // the double
        BigDecimal two = new BigDecimal("2.0");
        BigDecimal twoPointZeroZero = new BigDecimal("2.00");
        System.out.println("2.0 equals 2.00? " + two.equals(twoPointZeroZero)
                + ", compareTo: " + two.compareTo(twoPointZeroZero));
        BigDecimal three = new BigDecimal("3");
        BigDecimal third = BigDecimal.ONE.divide(three, 10, RoundingMode.HALF_UP);
        System.out.println("1/3 to 10 places = " + third);
        // endregion bigdecimal
    }
}
