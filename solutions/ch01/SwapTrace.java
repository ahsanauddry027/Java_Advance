import java.util.Arrays;

/** Exercise 1.8: runs the code from the exercise so you can check your trace. */
public class SwapTrace {

    static void swapValues(int x, int y) {
        int temp = x;
        x = y;
        y = temp;
    }

    static void swapFirstTwo(int[] array) {
        int temp = array[0];
        array[0] = array[1];
        array[1] = temp;
    }

    static void replace(int[] array) {
        array = new int[] {7, 7};
        array[0] = 8;
    }

    public static void main(String[] args) {
        int a = 1;
        int b = 2;
        int[] pair = {1, 2};
        swapValues(a, b);
        swapFirstTwo(pair);
        replace(pair);
        System.out.println(a + " " + b + " " + Arrays.toString(pair));
    }
}
