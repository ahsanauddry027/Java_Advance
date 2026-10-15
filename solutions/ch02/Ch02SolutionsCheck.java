import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/** Checks the Chapter 2 solutions against simple reference versions. */
public class Ch02SolutionsCheck {

    private static final Random RANDOM = new Random(202);

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void main(String[] args) {
        for (int trial = 0; trial < 2_000; trial++) {
            // 2.3: length, then alphabetical
            String[] words = new String[RANDOM.nextInt(8)];
            for (int i = 0; i < words.length; i++) {
                words[i] = randomWord();
            }
            String[] sorted = words.clone();
            SortByLength.sortByLength(sorted);
            for (int i = 0; i + 1 < sorted.length; i++) {
                String a = sorted[i];
                String b = sorted[i + 1];
                require(a.length() < b.length()
                        || (a.length() == b.length() && a.compareTo(b) <= 0), "order");
            }

            // 2.4 and 2.6: integer orders
            int[] values = RANDOM.ints(RANDOM.nextInt(10)).toArray();
            Integer[] boxed = Arrays.stream(values).boxed().toArray(Integer[]::new);
            Arrays.sort(boxed, ComparatorBug.FIXED);
            int[] expected = values.clone();
            Arrays.sort(expected);
            for (int i = 0; i < values.length; i++) {
                require(boxed[i] == expected[i], "FIXED comparator");
            }
            int[] descending = values.clone();
            SortDescending.sortDescendingInPlace(descending);
            int[] viaBoxes = SortDescending.descendingBoxed(values);
            for (int i = 0; i < values.length; i++) {
                require(descending[i] == expected[values.length - 1 - i], "descending");
                require(viaBoxes[i] == expected[values.length - 1 - i], "boxed");
            }

            // 2.5: start ascending, end descending
            int[][] intervals = new int[RANDOM.nextInt(8)][];
            for (int i = 0; i < intervals.length; i++) {
                int start = RANDOM.nextInt(5);
                intervals[i] = new int[] {start, start + RANDOM.nextInt(5)};
            }
            SortIntervals.sortIntervals(intervals);
            for (int i = 0; i + 1 < intervals.length; i++) {
                int[] a = intervals[i];
                int[] b = intervals[i + 1];
                require(a[0] < b[0] || (a[0] == b[0] && a[1] >= b[1]), "interval order");
            }

            // 2.7: grouping
            Map<Character, List<String>> groups = GroupByFirstLetter.group(words);
            Map<Character, List<String>> manual = new TreeMap<>();
            for (String w : words) {
                if (!w.isEmpty()) {
                    manual.computeIfAbsent(w.charAt(0), c -> new ArrayList<>()).add(w);
                }
            }
            require(groups.equals(manual), "grouping");

            // 2.12: parse a random line
            StringBuilder line = new StringBuilder();
            List<Integer> numbers = new ArrayList<>();
            for (int i = RANDOM.nextInt(6); i > 0; i--) {
                int v = RANDOM.nextInt(2001) - 1000;
                numbers.add(v);
                line.append(" ".repeat(RANDOM.nextInt(3))).append(v).append(' ');
            }
            int[] parsed = LineParser.parseInts(line.toString());
            require(parsed.length == numbers.size(), "parse count");
            for (int i = 0; i < parsed.length; i++) {
                require(parsed[i] == numbers.get(i), "parse value");
            }
        }

        // 2.8: frequencies
        require(WordFrequencies.frequencies("b a b c a b").equals(
                List.of("b=3", "a=2", "c=1")), "frequencies");

        // 2.9: well-known values
        require(BigDigits.digitSumOfFactorial(100) == 648, "digit sum of 100!");
        require(BigDigits.digitsOfPowerOfTwo(1000) == 302, "digits of 2^1000");

        // 2.10: exact total
        BigDecimal total = PriceTotal.total(new String[] {"19.99", "5.01", "0.10"},
                new int[] {3, 2, 1});
        require(total.compareTo(new BigDecimal("70.09")) == 0, "price total");
        System.out.println("Ch02SolutionsCheck: all checks passed");
    }

    private static String randomWord() {
        StringBuilder sb = new StringBuilder();
        for (int i = RANDOM.nextInt(5); i > 0; i--) {
            sb.append((char) ('a' + RANDOM.nextInt(3)));
        }
        return sb.toString();
    }
}
