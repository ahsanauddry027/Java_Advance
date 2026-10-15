import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Exercise 2.8: word frequencies, most frequent first, ties in alphabetical order. */
public class WordFrequencies {

    // region solve
    static List<String> frequencies(String text) {
        Map<String, Integer> counts = new HashMap<>();
        for (String word : text.toLowerCase().split("\\W+")) {  // split on non-letters
            if (!word.isEmpty()) {
                counts.merge(word, 1, Integer::sum);
            }
        }
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(counts.entrySet());
        entries.sort(Map.Entry.<String, Integer>comparingByValue().reversed()
                .thenComparing(Map.Entry.comparingByKey()));
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : entries) {
            result.add(entry.getKey() + "=" + entry.getValue());
        }
        return result;
    }
    // endregion solve

    public static void main(String[] args) {
        System.out.println(frequencies("The cat saw the dog. The dog saw a cat!"));
    }
}
