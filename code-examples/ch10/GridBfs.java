import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/** Fewest steps from the top-left to the bottom-right cell, moving in 4 directions. */
public class GridBfs {

    private static final int[] ROW_STEP = {-1, 1, 0, 0};
    private static final int[] COL_STEP = {0, 0, -1, 1};

    // region bfs
    static int shortestPath(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        if (grid[0][0] == '#' || grid[rows - 1][cols - 1] == '#') {
            return -1;
        }
        int[][] distance = new int[rows][cols];
        for (int[] row : distance) {
            Arrays.fill(row, -1);                      // -1 means "not reached yet"
        }
        Deque<int[]> queue = new ArrayDeque<>();       // cells as {row, col}
        distance[0][0] = 0;
        queue.offer(new int[] {0, 0});
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int row = cell[0];
            int col = cell[1];
            if (row == rows - 1 && col == cols - 1) {
                return distance[row][col];             // first arrival is the shortest
            }
            for (int d = 0; d < 4; d++) {
                int nextRow = row + ROW_STEP[d];
                int nextCol = col + COL_STEP[d];
                boolean inside = nextRow >= 0 && nextRow < rows
                        && nextCol >= 0 && nextCol < cols;
                if (inside && grid[nextRow][nextCol] == '.'
                        && distance[nextRow][nextCol] == -1) {
                    distance[nextRow][nextCol] = distance[row][col] + 1;  // mark now
                    queue.offer(new int[] {nextRow, nextCol});
                }
            }
        }
        return -1;                                     // the target is unreachable
    }
    // endregion bfs

    public static void main(String[] args) {
        String[] rows = {
            "..#.",
            ".#..",
            "....",
            "#.#.",
        };
        char[][] grid = new char[rows.length][];
        for (int r = 0; r < rows.length; r++) {
            grid[r] = rows[r].toCharArray();
        }
        System.out.println("shortest path length: " + shortestPath(grid));
    }
}
