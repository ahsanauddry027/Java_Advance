import java.util.Arrays;
import java.util.NoSuchElementException;

/** A generic array-based stack: how to work around "new T[]" being illegal. */
public class GenericStack<T> {

    // region stack
    private T[] items;
    private int size;

    @SuppressWarnings("unchecked")      // safe: the array never leaves this class
    public GenericStack() {
        items = (T[]) new Object[4];    // new T[4] does not compile (erasure)
    }

    public void push(T value) {
        if (size == items.length) {
            items = Arrays.copyOf(items, size * 2);
        }
        items[size++] = value;
    }

    public T pop() {
        if (size == 0) {
            throw new NoSuchElementException("pop from an empty stack");
        }
        T value = items[--size];
        items[size] = null;             // drop the reference: no memory leak
        return value;
    }

    public boolean isEmpty() {
        return size == 0;
    }
    // endregion stack

    public static void main(String[] args) {
        GenericStack<String> words = new GenericStack<>();
        for (String w : "one two three four five".split(" ")) {
            words.push(w);
        }
        StringBuilder out = new StringBuilder();
        while (!words.isEmpty()) {
            out.append(words.pop()).append(' ');
        }
        System.out.println(out.toString().trim());
    }
}
