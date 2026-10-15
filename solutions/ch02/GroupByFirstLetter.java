import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** Exercise 2.7: group words by their first letter, letters in alphabetical order. */
public class GroupByFirstLetter {

    // region solve
    static Map<Character, List<String>> group(String[] words) {
        Map<Character, List<String>> groups = new TreeMap<>();  // keys kept sorted
        for (String word : words) {
            if (word.isEmpty()) {
                continue;                                       // no first letter
            }
            groups.computeIfAbsent(word.charAt(0), letter -> new ArrayList<>()).add(word);
        }
        return groups;
    }
    // endregion solve

    public static void main(String[] args) {
        String[] words = {"banana", "apple", "blueberry", "cherry", "avocado", "", "fig"};
        System.out.println(group(words));
    }
}
