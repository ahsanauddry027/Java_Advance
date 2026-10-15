import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/** Static nested, inner, anonymous and local classes, each in its typical role. */
public class NestedClasses {

    // region static-nested
    /** A list node needs no access to the outer object: make it static. */
    static final class Node {
        final int value;
        Node next;

        Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }
    }
    // endregion static-nested

    // region inner
    /** A tiny integer bag whose iterator is an INNER class: it reads the bag's fields. */
    static final class IntBag implements Iterable<Integer> {
        private final int[] data;

        IntBag(int... data) {
            this.data = data.clone();
        }

        @Override
        public Iterator<Integer> iterator() {
            return new BagIterator();                 // bound to THIS bag
        }

        private final class BagIterator implements Iterator<Integer> {
            private int index;                        // the next position to return

            @Override
            public boolean hasNext() {
                return index < data.length;           // reads the outer object's field
            }

            @Override
            public Integer next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return data[index++];
            }
        }
    }
    // endregion inner

    public static void main(String[] args) {
        Node list = new Node(1, new Node(2, new Node(3, null)));
        int sum = 0;
        for (Node n = list; n != null; n = n.next) {
            sum += n.value;
        }
        System.out.println("list sum: " + sum);

        int total = 0;
        for (int value : new IntBag(4, 5, 6)) {    // works because IntBag is Iterable
            total += value;
        }
        System.out.println("bag total: " + total);

        // region anonymous
        String[] words = {"kiwi", "fig", "banana"};
        Arrays.sort(words, new Comparator<String>() {   // anonymous class (old style)
            @Override
            public int compare(String a, String b) {
                return Integer.compare(a.length(), b.length());
            }
        });
        System.out.println("anonymous: " + Arrays.toString(words));
        Arrays.sort(words, (a, b) -> Integer.compare(b.length(), a.length()));
        System.out.println("lambda, longest first: " + Arrays.toString(words));
        // endregion anonymous

        // region local
        class Counter {                            // a local class: visible only here
            int count;

            void see(String w) {
                if (w.length() > 3) {
                    count++;
                }
            }
        }
        Counter longWords = new Counter();
        for (String w : words) {
            longWords.see(w);
        }
        System.out.println("words longer than 3: " + longWords.count);
        // endregion local
    }
}
