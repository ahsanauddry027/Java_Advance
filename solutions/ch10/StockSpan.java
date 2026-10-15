import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Exercise 10.6: the span of today's price is the number of consecutive days, ending
 * today, on which the price was less than or equal to today's price.
 */
public class StockSpan {

    // region solve
    private final Deque<int[]> history = new ArrayDeque<>();  // {price, span} pairs

    public int next(int price) {
        int span = 1;
        while (!history.isEmpty() && history.peek()[0] <= price) {
            span += history.pop()[1];   // absorb the days that the popped entry covered
        }
        history.push(new int[] {price, span});
        return span;
    }
    // endregion solve

    public static void main(String[] args) {
        StockSpan spanner = new StockSpan();
        int[] prices = {100, 80, 60, 70, 60, 75, 85};
        StringBuilder spans = new StringBuilder();
        for (int price : prices) {
            spans.append(spanner.next(price)).append(' ');
        }
        System.out.println(spans.toString().trim());
    }
}
