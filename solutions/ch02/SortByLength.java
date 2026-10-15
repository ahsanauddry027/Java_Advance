import java.util.Arrays;
import java.util.Comparator;

/** Exercise 2.3: sort words by length, and alphabetically among words of equal length. */
public class SortByLength {

    // region solve
    static void sortByLength(String[] words) {
        Arrays.sort(words, Comparator.comparingInt(String::length)
                .thenComparing(Comparator.naturalOrder()));
    }
    // endregion solve

    public static void main(String[] args) {
        String[] words = {"pear", "fig", "banana", "kiwi", "apple", "date", "plum"};
        sortByLength(words);
        System.out.println(Arrays.toString(words));
    }
}
