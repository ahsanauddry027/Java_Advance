import java.math.BigDecimal;
import java.math.RoundingMode;

/** Exercise 2.10: exact money arithmetic with BigDecimal, compared with double. */
public class PriceTotal {

    // region solve
    static BigDecimal total(String[] prices, int[] quantities) {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < prices.length; i++) {
            BigDecimal quantity = BigDecimal.valueOf(quantities[i]);
            BigDecimal line = new BigDecimal(prices[i]).multiply(quantity);
            total = total.add(line);                  // BigDecimal is immutable: reassign
        }
        return total.setScale(2, RoundingMode.HALF_EVEN);
    }
    // endregion solve

    public static void main(String[] args) {
        String[] prices = {"19.99", "5.01", "0.10"};
        int[] quantities = {3, 2, 1};
        double asDouble = 0;
        for (int i = 0; i < prices.length; i++) {
            asDouble += Double.parseDouble(prices[i]) * quantities[i];
        }
        System.out.println("BigDecimal total: " + total(prices, quantities));
        System.out.println("double total:     " + asDouble);
        BigDecimal a = new BigDecimal("70.09");
        BigDecimal b = new BigDecimal("70.090");
        System.out.println("equals: " + a.equals(b) + ", compareTo: " + a.compareTo(b));
    }
}
