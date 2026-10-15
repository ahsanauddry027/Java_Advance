import java.util.ArrayList;
import java.util.List;

/** Generic methods, bounded type parameters, wildcards and erasure. */
public class GenericsDemo {

    // region max
    /** The largest element. T must be comparable to itself (or to a supertype). */
    static <T extends Comparable<? super T>> T max(List<? extends T> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("empty list");
        }
        T best = items.get(0);
        for (T item : items) {
            if (item.compareTo(best) > 0) {
                best = item;
            }
        }
        return best;
    }
    // endregion max

    // region pecs
    /** "Producer extends": we only READ numbers from the list. */
    static double sum(List<? extends Number> numbers) {
        double total = 0;
        for (Number n : numbers) {
            total += n.doubleValue();
        }
        return total;
    }

    /** "Consumer super": we only WRITE Integers into the list. */
    static void addOneToFive(List<? super Integer> target) {
        for (int i = 1; i <= 5; i++) {
            target.add(i);
        }
    }
    // endregion pecs

    public static void main(String[] args) {
        // region calls
        System.out.println(max(List.of(3, 9, 4)) + " " + max(List.of("pear", "apple")));

        List<Integer> ints = List.of(1, 2, 3);
        List<Double> doubles = List.of(0.5, 0.25);
        System.out.println(sum(ints) + " " + sum(doubles));  // both match the wildcard

        List<Number> numbers = new ArrayList<>();
        List<Object> objects = new ArrayList<>();
        addOneToFive(numbers);                     // Number is a supertype of Integer
        addOneToFive(objects);
        System.out.println(numbers + " " + objects);
        // endregion calls

        // region erasure
        List<String> strings = new ArrayList<>();
        List<Integer> integers = new ArrayList<>();
        boolean same = strings.getClass() == integers.getClass();
        System.out.println("same class at run time: " + same);
        // endregion erasure
    }
}
