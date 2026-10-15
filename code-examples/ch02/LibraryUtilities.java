import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** The Arrays and Collections helpers that save the most time in DSA code. */
public class LibraryUtilities {

    public static void main(String[] args) {
        // region arrays
        int[] values = {40, 10, 30, 20, 50};
        Arrays.sort(values);                                  // ascending, in place
        System.out.println("sorted       " + Arrays.toString(values));
        System.out.println("search 30    " + Arrays.binarySearch(values, 30));  // index
        System.out.println("search 35    " + Arrays.binarySearch(values, 35));  // -3 - 1
        int[] part = {9, 7, 5, 3, 1};
        Arrays.sort(part, 1, 4);                              // only indices 1..3
        System.out.println("range sort   " + Arrays.toString(part));
        int[] filled = new int[5];
        Arrays.fill(filled, -1);
        System.out.println("fill         " + Arrays.toString(filled));
        // endregion arrays

        // region aslist
        String[] words = {"pear", "apple", "fig"};
        List<String> view = Arrays.asList(words);             // fixed size, array-backed
        view.set(0, "plum");                                  // allowed: writes through
        System.out.println("asList view  " + view + ", array " + Arrays.toString(words));
        try {
            view.add("kiwi");                                 // not allowed: fixed size
        } catch (UnsupportedOperationException e) {
            System.out.println("asList add   UnsupportedOperationException");
        }
        List<String> copy = new ArrayList<>(Arrays.asList(words));  // growable copy
        copy.add("kiwi");
        System.out.println("copy         " + copy);
        // endregion aslist

        // region collections
        List<Integer> numbers = new ArrayList<>(List.of(5, 3, 8, 3, 1));
        Collections.sort(numbers);
        System.out.println("sort         " + numbers);
        Collections.reverse(numbers);
        System.out.println("reverse      " + numbers);
        System.out.println("max, min     " + Collections.max(numbers) + ", "
                + Collections.min(numbers));
        System.out.println("frequency 3  " + Collections.frequency(numbers, 3));
        Collections.swap(numbers, 0, numbers.size() - 1);
        System.out.println("swap ends    " + numbers);
        System.out.println("nCopies      " + Collections.nCopies(3, "ab"));
        List<Integer> fixed = List.of(1, 2, 3);               // immutable
        try {
            fixed.set(0, 9);
        } catch (UnsupportedOperationException e) {
            System.out.println("List.of set  UnsupportedOperationException");
        }
        // endregion collections
    }
}
