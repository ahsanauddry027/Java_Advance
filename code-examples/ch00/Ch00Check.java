import java.util.Arrays;
import java.util.Random;

/** Self-checks for the front-matter examples. Exits with an error if any check fails. */
public class Ch00Check {

    public static void main(String[] args) {
        Random random = new Random(42);
        for (int trial = 0; trial < 2_000; trial++) {
            int[] original = random.ints(random.nextInt(10), -50, 50).toArray();
            int[] expected = new int[original.length];
            for (int i = 0; i < original.length; i++) {
                expected[i] = original[original.length - 1 - i];
            }
            int[] actual = original.clone();
            ReverseInPlace.reverse(actual);
            if (!Arrays.equals(expected, actual)) {
                String input = Arrays.toString(original);
                throw new AssertionError("reverse failed for " + input);
            }
        }
        System.out.println("Ch00Check: all checks passed");
    }
}
