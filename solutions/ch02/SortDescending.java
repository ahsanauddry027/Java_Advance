import java.util.Arrays;
import java.util.Collections;

/** Exercise 2.6: two ways to sort an int[] in descending order. */
public class SortDescending {

    // region solve
    /** Boxes every value: simple, but uses far more memory and is slower. */
    static int[] descendingBoxed(int[] values) {
        Integer[] boxed = new Integer[values.length];
        for (int i = 0; i < values.length; i++) {
            boxed[i] = values[i];
        }
        Arrays.sort(boxed, Collections.reverseOrder());
        int[] result = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = boxed[i];
        }
        return result;
    }

    /** Sorts the primitives ascending, then reverses them in place. */
    static void sortDescendingInPlace(int[] values) {
        Arrays.sort(values);
        for (int i = 0, j = values.length - 1; i < j; i++, j--) {
            int temp = values[i];
            values[i] = values[j];
            values[j] = temp;
        }
    }
    // endregion solve

    public static void main(String[] args) {
        int[] values = {3, -1, 7, 7, 0};
        System.out.println(Arrays.toString(descendingBoxed(values)));
        sortDescendingInPlace(values);
        System.out.println(Arrays.toString(values));
    }
}
