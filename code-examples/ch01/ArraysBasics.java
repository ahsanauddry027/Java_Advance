import java.util.Arrays;

/** Creating, copying and printing arrays, including the traps with 2D arrays. */
public class ArraysBasics {

    public static void main(String[] args) {
        // region create
        int[] scores = new int[4];            // every slot starts at 0
        String[] names = new String[2];       // every slot starts at null
        boolean[] seen = new boolean[3];      // every slot starts at false
        int[] primes = {2, 3, 5, 7};          // an array initializer
        int[][] grid = new int[2][3];         // 2 rows, each with 3 columns
        int[][] triangle = new int[3][];      // 3 rows, not created yet (all null)
        for (int r = 0; r < triangle.length; r++) {
            triangle[r] = new int[r + 1];     // a "jagged" array: row r has r + 1 slots
        }
        // endregion create
        System.out.println(Arrays.toString(scores) + " " + Arrays.toString(names)
                + " " + Arrays.toString(seen) + " " + Arrays.toString(primes));
        System.out.println(Arrays.deepToString(grid) + " "
                + Arrays.deepToString(triangle));

        // region copy
        int[] whole = primes.clone();                  // same length, same values
        int[] longer = Arrays.copyOf(primes, 6);       // extra slots are 0
        int[] middle = Arrays.copyOfRange(primes, 1, 3);  // indices 1 and 2 (3 excluded)
        int[] shifted = new int[5];
        System.arraycopy(primes, 0, shifted, 1, 4);    // from, start, to, start, count
        // endregion copy
        System.out.println(Arrays.toString(whole) + " " + Arrays.toString(longer) + " "
                + Arrays.toString(middle) + " " + Arrays.toString(shifted));

        // region shallow
        int[][] original = {{1, 2}, {3, 4}};
        int[][] shallow = original.clone();            // copies the row REFERENCES
        shallow[0][0] = 99;                            // ...so this changes original too
        int[][] deep = new int[original.length][];
        for (int r = 0; r < original.length; r++) {
            deep[r] = original[r].clone();             // copy every row
        }
        deep[1][1] = -1;                               // original is not affected
        // endregion shallow
        System.out.println("original " + Arrays.deepToString(original)
                + ", deep " + Arrays.deepToString(deep));

        // region fill-trap
        int[] oneRow = new int[3];
        int[][] rows = new int[3][];
        Arrays.fill(rows, oneRow);                     // three references to ONE array
        rows[0][0] = 7;
        // endregion fill-trap
        System.out.println("fill trap " + Arrays.deepToString(rows));
    }
}
