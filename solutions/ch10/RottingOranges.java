import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Exercise 10.13: in a grid, 0 is empty, 1 is a fresh orange and 2 is a rotten orange.
 * Every minute, rotten oranges rot their fresh neighbours (up, down, left, right).
 * Return the minutes until no fresh orange is left, or -1 if that never happens.
 */
public class RottingOranges {

    // region solve
    static int minutesToRot(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int[][] state = new int[rows][];
        Deque<int[]> queue = new ArrayDeque<>();
        int fresh = 0;
        for (int r = 0; r < rows; r++) {
            state[r] = grid[r].clone();             // do not modify the caller's grid
            for (int c = 0; c < cols; c++) {
                if (state[r][c] == 2) {
                    queue.offer(new int[] {r, c});  // every rotten orange starts at once
                } else if (state[r][c] == 1) {
                    fresh++;
                }
            }
        }
        int[][] steps = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int minutes = 0;
        while (!queue.isEmpty() && fresh > 0) {
            int levelSize = queue.size();          // all oranges that rotted last minute
            for (int i = 0; i < levelSize; i++) {
                int[] cell = queue.poll();
                for (int[] step : steps) {
                    int r = cell[0] + step[0];
                    int c = cell[1] + step[1];
                    if (r >= 0 && r < rows && c >= 0 && c < cols && state[r][c] == 1) {
                        state[r][c] = 2;
                        fresh--;
                        queue.offer(new int[] {r, c});
                    }
                }
            }
            minutes++;
        }
        return fresh == 0 ? minutes : -1;
    }
    // endregion solve

    public static void main(String[] args) {
        int[][] reachable = {{2, 1, 1}, {1, 1, 0}, {0, 1, 1}};
        int[][] blocked = {{2, 1, 1}, {0, 1, 1}, {1, 0, 1}};
        int[][] nothingFresh = {{0, 2}};
        System.out.println(minutesToRot(reachable));
        System.out.println(minutesToRot(blocked));
        System.out.println(minutesToRot(nothingFresh));
    }
}
