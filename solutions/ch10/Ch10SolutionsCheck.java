import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/** Checks every Chapter 10 solution against a brute-force version on random inputs. */
public class Ch10SolutionsCheck {

    private static final Random RANDOM = new Random(1010);

    public static void main(String[] args) {
        checkFirstBracketError();
        checkCircularQueue();
        checkMinStack();
        checkDailyTemperatures();
        checkStockSpan();
        checkSimplifyPath();
        checkDecodeString();
        checkAsteroids();
        checkFirstNegatives();
        checkRemoveKDigits();
        checkSubarrayMinimums();
        checkRottingOranges();
        checkMaximalRectangle();
        System.out.println("Ch10SolutionsCheck: all checks passed");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static int[] randomArray(int minLength, int maxLength, int low, int high) {
        int length = minLength + RANDOM.nextInt(maxLength - minLength + 1);
        return RANDOM.ints(length, low, high + 1).toArray();
    }

    // ------------------------------------------------------------------ 10.2

    private static void checkFirstBracketError() {
        String alphabet = "()[]{}ab";
        for (int trial = 0; trial < 20_000; trial++) {
            StringBuilder sb = new StringBuilder();
            int length = RANDOM.nextInt(10);
            for (int i = 0; i < length; i++) {
                sb.append(alphabet.charAt(RANDOM.nextInt(alphabet.length())));
            }
            String s = sb.toString();
            int answer = FirstBracketError.firstError(s);
            require((answer == -1) == isBalancedSlow(s), "firstError balance " + s);
            if (answer >= 0) {
                // The reported position must agree with a slow rescan of the text.
                require(firstMismatchSlow(s) == answer, "firstError position " + s);
            }
        }
    }

    private static boolean isBalancedSlow(String s) {
        String brackets = s.replaceAll("[^()\\[\\]{}]", "");
        String previous;
        do {
            previous = brackets;
            brackets = brackets.replace("()", "").replace("[]", "").replace("{}", "");
        } while (!brackets.equals(previous));
        return brackets.isEmpty();
    }

    /** Slow reference: rescan from the start for every position. */
    private static int firstMismatchSlow(String s) {
        for (int end = 0; end < s.length(); end++) {
            List<Integer> open = new ArrayList<>();
            for (int i = 0; i <= end; i++) {
                char ch = s.charAt(i);
                if ("([{".indexOf(ch) >= 0) {
                    open.add(i);
                } else if (")]}".indexOf(ch) >= 0) {
                    if (open.isEmpty()
                            || "([{".indexOf(s.charAt(open.get(open.size() - 1)))
                            != ")]}".indexOf(ch)) {
                        return i;
                    }
                    open.remove(open.size() - 1);
                }
            }
            if (end == s.length() - 1 && !open.isEmpty()) {
                return open.get(0);
            }
        }
        return -1;
    }

    // ------------------------------------------------------------------ 10.3

    private static void checkCircularQueue() {
        for (int trial = 0; trial < 300; trial++) {
            int capacity = 1 + RANDOM.nextInt(5);
            CircularQueue mine = new CircularQueue(capacity);
            Deque<Integer> reference = new ArrayDeque<>();
            for (int op = 0; op < 100; op++) {
                if (RANDOM.nextBoolean()) {
                    int value = RANDOM.nextInt(100);
                    boolean expected = reference.size() < capacity;
                    if (expected) {
                        reference.offerLast(value);
                    }
                    require(mine.enqueue(value) == expected, "CircularQueue enqueue");
                } else {
                    boolean expected = !reference.isEmpty();
                    reference.pollFirst();
                    require(mine.dequeue() == expected, "CircularQueue dequeue");
                }
                int front = reference.isEmpty() ? -1 : reference.peekFirst();
                int rear = reference.isEmpty() ? -1 : reference.peekLast();
                require(mine.front() == front && mine.rear() == rear, "front/rear");
                require(mine.isFull() == (reference.size() == capacity), "isFull");
            }
        }
    }

    // ------------------------------------------------------------------ 10.4

    private static void checkMinStack() {
        MinStack mine = new MinStack();
        List<Integer> reference = new ArrayList<>();
        for (int op = 0; op < 20_000; op++) {
            if (reference.isEmpty() || RANDOM.nextBoolean()) {
                int value = RANDOM.nextInt(2001) - 1000;
                mine.push(value);
                reference.add(value);
            } else {
                int expectedMin = reference.stream().mapToInt(Integer::intValue).min()
                        .orElseThrow();
                require(mine.getMin() == expectedMin, "MinStack getMin");
                int last = reference.get(reference.size() - 1);
                require(mine.top() == last, "MinStack top");
                reference.remove(reference.size() - 1);
                require(mine.pop() == last, "MinStack pop");
            }
        }
    }

    // ------------------------------------------------------------------ 10.5, 10.6

    private static void checkDailyTemperatures() {
        for (int trial = 0; trial < 5_000; trial++) {
            int[] t = randomArray(0, 12, 20, 25);
            int[] expected = new int[t.length];
            for (int i = 0; i < t.length; i++) {
                for (int j = i + 1; j < t.length; j++) {
                    if (t[j] > t[i]) {
                        expected[i] = j - i;
                        break;
                    }
                }
            }
            int[] actual = DailyTemperatures.daysUntilWarmer(t);
            require(Arrays.equals(actual, expected), "daily");
        }
    }

    private static void checkStockSpan() {
        for (int trial = 0; trial < 3_000; trial++) {
            int[] prices = randomArray(1, 15, 1, 6);
            StockSpan spanner = new StockSpan();
            for (int i = 0; i < prices.length; i++) {
                int expected = 0;
                for (int j = i; j >= 0 && prices[j] <= prices[i]; j--) {
                    expected++;
                }
                require(spanner.next(prices[i]) == expected, "StockSpan");
            }
        }
    }

    // ------------------------------------------------------------------ 10.7, 10.8

    private static void checkSimplifyPath() {
        String[][] cases = {
            {"/home/", "/home"},
            {"/a/./b/../../c/", "/c"},
            {"/../", "/"},
            {"/home//user/./docs/..", "/home/user"},
            {"/...", "/..."},
            {"/a/b/c/../../../..", "/"},
            {"/.hidden/./x", "/.hidden/x"},
        };
        for (String[] c : cases) {
            require(SimplifyPath.simplify(c[0]).equals(c[1]), "simplify " + c[0]);
        }
    }

    private static void checkDecodeString() {
        for (int trial = 0; trial < 3_000; trial++) {
            StringBuilder encoded = new StringBuilder();
            String expected = randomEncoded(3, encoded);
            require(DecodeString.decode(encoded.toString()).equals(expected),
                    "decode " + encoded);
        }
    }

    private static String randomEncoded(int depth, StringBuilder encoded) {
        StringBuilder decoded = new StringBuilder();
        int parts = 1 + RANDOM.nextInt(3);
        for (int p = 0; p < parts; p++) {
            if (depth > 0 && RANDOM.nextBoolean()) {
                int count = 1 + RANDOM.nextInt(12);
                encoded.append(count).append('[');
                String inner = randomEncoded(depth - 1, encoded);
                encoded.append(']');
                decoded.append(inner.repeat(count));
            } else {
                char letter = (char) ('a' + RANDOM.nextInt(3));
                encoded.append(letter);
                decoded.append(letter);
            }
        }
        return decoded.toString();
    }

    // ------------------------------------------------------------------ 10.9, 10.10

    private static void checkAsteroids() {
        for (int trial = 0; trial < 5_000; trial++) {
            int[] a = randomArray(0, 10, -5, 5);
            for (int i = 0; i < a.length; i++) {
                if (a[i] == 0) {
                    a[i] = 1;
                }
            }
            require(Arrays.equals(AsteroidCollision.survivors(a), asteroidsSlow(a)),
                    "asteroids " + Arrays.toString(a));
        }
    }

    /** Repeatedly resolve the first adjacent right-mover/left-mover pair. */
    private static int[] asteroidsSlow(int[] input) {
        List<Integer> list = new ArrayList<>();
        for (int v : input) {
            list.add(v);
        }
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i + 1 < list.size(); i++) {
                int a = list.get(i);
                int b = list.get(i + 1);
                if (a > 0 && b < 0) {
                    if (a > -b) {
                        list.remove(i + 1);
                    } else if (a < -b) {
                        list.remove(i);
                    } else {
                        list.remove(i + 1);
                        list.remove(i);
                    }
                    changed = true;
                    break;
                }
            }
        }
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    private static void checkFirstNegatives() {
        for (int trial = 0; trial < 5_000; trial++) {
            int[] v = randomArray(1, 12, -5, 5);
            int k = 1 + RANDOM.nextInt(v.length);
            int[] expected = new int[v.length - k + 1];
            for (int s = 0; s + k <= v.length; s++) {
                for (int i = s; i < s + k; i++) {
                    if (v[i] < 0) {
                        expected[s] = v[i];
                        break;
                    }
                }
            }
            require(Arrays.equals(FirstNegativeInWindow.firstNegatives(v, k), expected),
                    "firstNegatives");
        }
    }

    // ------------------------------------------------------------------ 10.11, 10.12

    private static void checkRemoveKDigits() {
        for (int trial = 0; trial < 3_000; trial++) {
            int length = 1 + RANDOM.nextInt(8);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append((char) ('0' + RANDOM.nextInt(10)));
            }
            String number = sb.toString();
            int k = RANDOM.nextInt(length + 1);
            require(RemoveKDigits.removeKDigits(number, k).equals(removeSlow(number, k)),
                    "removeKDigits " + number + " k=" + k);
        }
    }

    /** Tries every subset of positions to keep (fine for up to 8 digits). */
    private static String removeSlow(String number, int k) {
        int n = number.length();
        String best = null;
        for (int mask = 0; mask < (1 << n); mask++) {
            if (Integer.bitCount(mask) != n - k) {
                continue;
            }
            StringBuilder kept = new StringBuilder();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    kept.append(number.charAt(i));
                }
            }
            String normalized = kept.toString().replaceFirst("^0+", "");
            if (normalized.isEmpty()) {
                normalized = "0";
            }
            if (best == null || normalized.length() < best.length()
                    || (normalized.length() == best.length()
                        && normalized.compareTo(best) < 0)) {
                best = normalized;
            }
        }
        return best;
    }

    private static void checkSubarrayMinimums() {
        for (int trial = 0; trial < 3_000; trial++) {
            int[] v = randomArray(1, 12, 1, 6);
            long expected = 0;
            for (int i = 0; i < v.length; i++) {
                int min = Integer.MAX_VALUE;
                for (int j = i; j < v.length; j++) {
                    min = Math.min(min, v[j]);
                    expected += min;
                }
            }
            expected %= SumOfSubarrayMinimums.MOD;
            require(SumOfSubarrayMinimums.sumOfMinimums(v) == expected, "minimums");
        }
        int[] big = new int[30_000];
        Arrays.fill(big, 30_000);
        long count = 30_000L * 30_001 / 2;                   // number of subarrays
        long mod = SumOfSubarrayMinimums.MOD;
        long expected = count % mod * 30_000 % mod;
        require(SumOfSubarrayMinimums.sumOfMinimums(big) == expected, "large input");
    }

    // ------------------------------------------------------------------ 10.13, 10.14

    private static void checkRottingOranges() {
        for (int trial = 0; trial < 3_000; trial++) {
            int rows = 1 + RANDOM.nextInt(4);
            int cols = 1 + RANDOM.nextInt(4);
            int[][] grid = new int[rows][cols];
            for (int[] row : grid) {
                for (int c = 0; c < cols; c++) {
                    row[c] = RANDOM.nextInt(3);
                }
            }
            int[][] copy = new int[rows][];
            for (int r = 0; r < rows; r++) {
                copy[r] = grid[r].clone();
            }
            require(RottingOranges.minutesToRot(grid) == rotSlow(copy), "rotting");
        }
    }

    /** Simulates minute by minute on a copy of the grid. */
    private static int rotSlow(int[][] grid) {
        int minutes = 0;
        while (true) {
            List<int[]> toRot = new ArrayList<>();
            boolean anyFresh = false;
            for (int r = 0; r < grid.length; r++) {
                for (int c = 0; c < grid[0].length; c++) {
                    if (grid[r][c] != 1) {
                        continue;
                    }
                    anyFresh = true;
                    int[][] around = {{r - 1, c}, {r + 1, c}, {r, c - 1}, {r, c + 1}};
                    for (int[] n : around) {
                        if (n[0] >= 0 && n[0] < grid.length && n[1] >= 0
                                && n[1] < grid[0].length && grid[n[0]][n[1]] == 2) {
                            toRot.add(new int[] {r, c});
                            break;
                        }
                    }
                }
            }
            if (!anyFresh) {
                return minutes;
            }
            if (toRot.isEmpty()) {
                return -1;
            }
            for (int[] cell : toRot) {
                grid[cell[0]][cell[1]] = 2;
            }
            minutes++;
        }
    }

    private static void checkMaximalRectangle() {
        for (int trial = 0; trial < 3_000; trial++) {
            int rows = 1 + RANDOM.nextInt(5);
            int cols = 1 + RANDOM.nextInt(5);
            char[][] m = new char[rows][cols];
            for (char[] row : m) {
                for (int c = 0; c < cols; c++) {
                    row[c] = RANDOM.nextInt(4) == 0 ? '0' : '1';
                }
            }
            long expected = 0;
            for (int r1 = 0; r1 < rows; r1++) {
                for (int c1 = 0; c1 < cols; c1++) {
                    for (int r2 = r1; r2 < rows; r2++) {
                        for (int c2 = c1; c2 < cols; c2++) {
                            boolean allOnes = true;
                            for (int r = r1; r <= r2 && allOnes; r++) {
                                for (int c = c1; c <= c2; c++) {
                                    if (m[r][c] != '1') {
                                        allOnes = false;
                                        break;
                                    }
                                }
                            }
                            if (allOnes) {
                                long area = (long) (r2 - r1 + 1) * (c2 - c1 + 1);
                                expected = Math.max(expected, area);
                            }
                        }
                    }
                }
            }
            require(MaximalRectangle.maximalRectangle(m) == expected, "maximal");
        }
    }
}
