/** Largest rectangle that fits under a histogram, using a monotonic stack of indices. */
public class LargestRectangle {

    // region largest
    static long largestRectangle(int[] heights) {
        int n = heights.length;
        int[] stack = new int[n + 1];  // indices; their heights increase toward the top
        int top = 0;                   // number of indices on the stack
        long best = 0;
        for (int i = 0; i <= n; i++) {
            int current = (i == n) ? 0 : heights[i];  // a final 0 empties the stack
            while (top > 0 && heights[stack[top - 1]] >= current) {
                int height = heights[stack[--top]];  // this bar cannot extend further
                int leftLimit = (top == 0) ? -1 : stack[top - 1];  // previous smaller bar
                long width = i - leftLimit - 1;
                best = Math.max(best, height * width);
            }
            stack[top++] = i;
        }
        return best;
    }
    // endregion largest

    // region brute
    static long largestRectangleBrute(int[] heights) {
        long best = 0;
        for (int left = 0; left < heights.length; left++) {
            int minHeight = Integer.MAX_VALUE;
            for (int right = left; right < heights.length; right++) {
                minHeight = Math.min(minHeight, heights[right]);
                best = Math.max(best, (long) minHeight * (right - left + 1));
            }
        }
        return best;
    }
    // endregion brute

    public static void main(String[] args) {
        int[] heights = {2, 1, 5, 6, 2, 3};
        System.out.println("monotonic stack: " + largestRectangle(heights));
        System.out.println("brute force:     " + largestRectangleBrute(heights));
    }
}
