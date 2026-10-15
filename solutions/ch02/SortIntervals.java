import java.util.Arrays;

/** Exercise 2.5: sort by start ascending; for equal starts, by end descending. */
public class SortIntervals {

    // region solve
    static void sortIntervals(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);   // smaller start first
            }
            return Integer.compare(b[1], a[1]);       // then larger end first
        });
    }
    // endregion solve

    public static void main(String[] args) {
        int[][] intervals = {{2, 4}, {1, 3}, {2, 9}, {1, 7}, {0, 1}};
        sortIntervals(intervals);
        System.out.println(Arrays.deepToString(intervals));
    }
}
