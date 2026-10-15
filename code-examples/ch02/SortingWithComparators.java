import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** Sorting in ascending, descending and custom orders, and the comparator to avoid. */
public class SortingWithComparators {

    // region record
    record Student(String name, int score) { }  // a tiny immutable data class (Chapter 3)
    // endregion record

    public static void main(String[] args) {
        // region descending
        Integer[] boxed = {5, 1, 4, 2, 3};
        Arrays.sort(boxed, Collections.reverseOrder());        // only for object arrays
        System.out.println("boxed desc   " + Arrays.toString(boxed));

        int[] primitive = {5, 1, 4, 2, 3};
        Arrays.sort(primitive);                                // ascending...
        for (int i = 0, j = primitive.length - 1; i < j; i++, j--) {
            int temp = primitive[i];                           // ...then reverse in place
            primitive[i] = primitive[j];
            primitive[j] = temp;
        }
        System.out.println("int[] desc   " + Arrays.toString(primitive));
        // endregion descending

        // region intervals
        int[][] intervals = {{5, 8}, {1, 9}, {1, 3}, {2, 6}};
        Arrays.sort(intervals, Comparator.comparingInt((int[] a) -> a[0])
                .thenComparingInt(a -> a[1]));                 // by start, then by end
        System.out.println("intervals    " + Arrays.deepToString(intervals));
        // endregion intervals

        // region objects
        List<Student> students = new ArrayList<>(List.of(
                new Student("Rafi", 82), new Student("Anika", 95),
                new Student("Tanvir", 82), new Student("Mim", 70)));
        students.sort(Comparator.comparingInt(Student::score).reversed()
                .thenComparing(Student::name));                // score desc, then name
        StringBuilder shown = new StringBuilder("students    ");
        for (Student s : students) {
            shown.append(' ').append(s.name()).append('=').append(s.score());
        }
        System.out.println(shown);
        // endregion objects

        // region overflow
        Integer[] extremes = {Integer.MIN_VALUE, 1, Integer.MAX_VALUE, 0};
        Integer[] wrong = extremes.clone();
        Arrays.sort(wrong, (a, b) -> a - b);                   // subtraction can overflow
        Integer[] right = extremes.clone();
        Arrays.sort(right, Integer::compare);
        System.out.println("a - b        " + Arrays.toString(wrong));
        System.out.println("compare      " + Arrays.toString(right));
        // endregion overflow
    }
}
