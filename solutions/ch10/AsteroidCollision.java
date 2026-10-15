import java.util.Arrays;

/**
 * Exercise 10.9: asteroids move along a line; a positive value moves right, a negative
 * value moves left, and the absolute value is the size. When two meet, the smaller one
 * explodes; equal sizes both explode. Return the asteroids that survive.
 */
public class AsteroidCollision {

    // region solve
    static int[] survivors(int[] asteroids) {
        int[] stack = new int[asteroids.length];  // survivors so far, left to right
        int top = 0;
        for (int asteroid : asteroids) {
            boolean alive = true;
            while (alive && asteroid < 0 && top > 0 && stack[top - 1] > 0) {
                int right = stack[top - 1];           // moving right, meets this one
                if (right < -asteroid) {
                    top--;                            // the right-mover explodes
                } else if (right == -asteroid) {
                    top--;                            // both explode
                    alive = false;
                } else {
                    alive = false;                    // the left-mover explodes
                }
            }
            if (alive) {
                stack[top++] = asteroid;
            }
        }
        return Arrays.copyOf(stack, top);
    }
    // endregion solve

    public static void main(String[] args) {
        int[][] samples = {{5, 10, -5}, {8, -8}, {10, 2, -5}, {-2, -1, 1, 2}};
        for (int[] sample : samples) {
            System.out.printf("%-16s -> %s%n", Arrays.toString(sample),
                    Arrays.toString(survivors(sample)));
        }
    }
}
