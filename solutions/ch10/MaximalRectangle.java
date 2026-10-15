/**
 * Exercise 10.14: area of the largest rectangle containing only '1' cells in a binary
 * matrix, by treating each row as the base of a histogram.
 */
public class MaximalRectangle {

    // region solve
    static long maximalRectangle(char[][] matrix) {
        if (matrix.length == 0) {
            return 0;
        }
        int cols = matrix[0].length;
        int[] heights = new int[cols];  // consecutive '1's ending at the current row
        long best = 0;
        for (char[] row : matrix) {
            for (int c = 0; c < cols; c++) {
                heights[c] = (row[c] == '1') ? heights[c] + 1 : 0;
            }
            best = Math.max(best, largestRectangle(heights));
        }
        return best;
    }

    /** Same algorithm as LargestRectangle in code-examples/ch10. */
    static long largestRectangle(int[] heights) {
        int n = heights.length;
        int[] stack = new int[n + 1];
        int top = 0;
        long best = 0;
        for (int i = 0; i <= n; i++) {
            int current = (i == n) ? 0 : heights[i];
            while (top > 0 && heights[stack[top - 1]] >= current) {
                int height = heights[stack[--top]];
                int leftLimit = (top == 0) ? -1 : stack[top - 1];
                best = Math.max(best, (long) height * (i - leftLimit - 1));
            }
            stack[top++] = i;
        }
        return best;
    }
    // endregion solve

    public static void main(String[] args) {
        String[] rows = {"10100", "10111", "11111", "10010"};
        char[][] matrix = new char[rows.length][];
        for (int r = 0; r < rows.length; r++) {
            matrix[r] = rows[r].toCharArray();
        }
        System.out.println(maximalRectangle(matrix));
    }
}
