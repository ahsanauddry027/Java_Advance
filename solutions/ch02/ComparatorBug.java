import java.util.Arrays;
import java.util.Comparator;

/** Exercise 2.4: the subtraction comparator breaks when the difference overflows. */
public class ComparatorBug {

    // region solve
    static final Comparator<Integer> BUGGY = (a, b) -> a - b;
    static final Comparator<Integer> FIXED = Integer::compare;
    // endregion solve

    public static void main(String[] args) {
        Integer low = -2_000_000_000;
        Integer high = 2_000_000_000;
        System.out.println("BUGGY.compare(low, high) = " + BUGGY.compare(low, high)
                + "  (positive: claims low > high)");
        System.out.println("FIXED.compare(low, high) = " + FIXED.compare(low, high));
        Integer[] values = {high, low};
        Integer[] sortedBuggy = values.clone();
        Arrays.sort(sortedBuggy, BUGGY);
        Integer[] sortedFixed = values.clone();
        Arrays.sort(sortedFixed, FIXED);
        System.out.println("buggy sort: " + Arrays.toString(sortedBuggy));
        System.out.println("fixed sort: " + Arrays.toString(sortedFixed));
    }
}
