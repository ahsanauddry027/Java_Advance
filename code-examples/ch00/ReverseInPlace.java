import java.util.Arrays;

public class ReverseInPlace {

    // region reverse
    /** Reverses values in place using two pointers that move toward each other. */
    static void reverse(int[] values) {
        int left = 0;
        int right = values.length - 1;
        while (left < right) {
            int temp = values[left];
            values[left] = values[right];
            values[right] = temp;
            left++;
            right--;
        }
    }
    // endregion reverse

    public static void main(String[] args) {
        int[] numbers = {3, 8, 1, 9, 4};
        System.out.println("Before: " + Arrays.toString(numbers));
        reverse(numbers);
        System.out.println("After:  " + Arrays.toString(numbers));
    }
}
