import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

/** The lambda and method-reference forms that appear in everyday DSA code. */
public class LambdaBasics {

    public static void main(String[] args) {
        // region forms
        Comparator<String> byLength = (String a, String b) -> Integer.compare(a.length(),
                b.length());                                      // full form
        Comparator<String> byLengthShort =
                (a, b) -> Integer.compare(a.length(), b.length());   // types inferred
        Runnable hello = () -> System.out.println("hello from a Runnable");
        BinaryOperator<Integer> add = (x, y) -> {             // a block body needs return
            int sum = x + y;
            return sum;
        };
        hello.run();
        System.out.println(byLength.compare("fig", "pear") + " "
                + byLengthShort.compare("pear", "fig") + " " + add.apply(2, 3));
        // endregion forms

        // region refs
        Comparator<Integer> natural = Integer::compare;        // static method
        Comparator<String> byLengthRef = Comparator.comparingInt(String::length);
        Supplier<List<String>> newList = ArrayList::new;       // constructor
        List<String> words = newList.get();
        words.add("banana");
        words.add("fig");
        words.add("apple");
        words.sort(byLengthRef);
        System.out.println(natural.compare(7, 3) + " " + words);
        // endregion refs

        // region maps
        String text = "to be or not to be is the question";
        Map<String, Integer> counts = new HashMap<>();
        for (String word : text.split(" ")) {
            counts.merge(word, 1, Integer::sum);               // add 1, or start at 1
        }
        Map<Integer, List<String>> byLen = new TreeMap<>();
        for (String word : text.split(" ")) {
            byLen.computeIfAbsent(word.length(), k -> new ArrayList<>()).add(word);
        }
        System.out.println(new TreeMap<>(counts));
        System.out.println(byLen);
        // endregion maps

        // region capture
        int limit = 3;                                         // effectively final
        Runnable show = () -> System.out.println("limit inside the lambda: " + limit);
        show.run();
        // limit = 4;   // would make the lambda above a compile error
        // endregion capture
    }
}
