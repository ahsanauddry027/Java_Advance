import java.util.Arrays;

/** == compares primitive values but object references; equals compares content. */
public class EqualityDemo {

    public static void main(String[] args) {
        // region strings
        String literal1 = "java";
        String literal2 = "java";            // the same pooled object as literal1
        String created = new String("java"); // a new object with the same characters
        String constant = "ja" + "va";       // a compile-time constant: also pooled
        String part = "ja";
        String computed = part + "va";       // built while the program runs: a new object
        System.out.println("literal1 == literal2         " + (literal1 == literal2));
        System.out.println("literal1 == created          " + (literal1 == created));
        System.out.println("literal1.equals(created)     " + literal1.equals(created));
        System.out.println("literal1 == constant         " + (literal1 == constant));
        System.out.println("literal1 == computed         " + (literal1 == computed));
        String interned = computed.intern();  // the pooled copy of the same text
        System.out.println("literal1 == computed.intern() " + (literal1 == interned));
        // endregion strings

        // region boxes
        Integer small1 = 127;
        Integer small2 = 127;
        Integer large1 = 1000;
        Integer large2 = 1000;
        System.out.println("Integer 127 == 127           " + (small1 == small2));
        System.out.println("Integer 1000 == 1000         " + (large1 == large2));
        System.out.println("large1.equals(large2)        " + large1.equals(large2));
        // endregion boxes

        // region arrays
        int[] x = {1, 2, 3};
        int[] y = {1, 2, 3};
        System.out.println("x == y                       " + (x == y));
        System.out.println("x.equals(y)                  " + x.equals(y));
        System.out.println("Arrays.equals(x, y)          " + Arrays.equals(x, y));
        // endregion arrays
    }
}
