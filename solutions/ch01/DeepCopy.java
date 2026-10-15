import java.util.Arrays;

/** Exercise 1.4: deep copy of a 2D int array that may be jagged or have null rows. */
public class DeepCopy {

    // region solve
    static int[][] deepCopy(int[][] source) {
        if (source == null) {
            return null;
        }
        int[][] copy = new int[source.length][];
        for (int r = 0; r < source.length; r++) {
            copy[r] = (source[r] == null) ? null : source[r].clone();
        }
        return copy;
    }
    // endregion solve

    public static void main(String[] args) {
        int[][] original = {{1, 2, 3}, null, {4}};
        int[][] copy = deepCopy(original);
        copy[0][0] = 100;
        copy[2] = new int[] {9, 9};
        System.out.println("original " + Arrays.deepToString(original));
        System.out.println("copy     " + Arrays.deepToString(copy));
    }
}
