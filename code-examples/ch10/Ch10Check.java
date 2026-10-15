import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Random;

/** Randomized self-checks for the Chapter 10 examples; fails with an error on failure. */
public class Ch10Check {

    private static final Random RANDOM = new Random(10);

    public static void main(String[] args) {
        checkIntStack();
        checkLinkedStack();
        checkIntQueue();
        checkQueueWithStacks();
        checkStackWithQueue();
        checkBalanced();
        checkExpressions();
        checkNextGreater();
        checkLargestRectangle();
        checkSlidingWindow();
        checkGridBfs();
        System.out.println("Ch10Check: all checks passed");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void checkIntStack() {
        for (int trial = 0; trial < 500; trial++) {
            IntStack mine = new IntStack();
            Deque<Integer> reference = new ArrayDeque<>();
            for (int op = 0; op < 200; op++) {
                if (reference.isEmpty() || RANDOM.nextInt(3) > 0) {
                    int value = RANDOM.nextInt(1000) - 500;
                    mine.push(value);
                    reference.push(value);
                } else {
                    require(mine.peek() == reference.peek(), "IntStack peek");
                    require(mine.pop() == reference.pop(), "IntStack pop");
                }
                require(mine.size() == reference.size(), "IntStack size");
            }
        }
    }

    private static void checkLinkedStack() {
        LinkedStack<Integer> mine = new LinkedStack<>();
        Deque<Integer> reference = new ArrayDeque<>();
        for (int op = 0; op < 20_000; op++) {
            if (reference.isEmpty() || RANDOM.nextBoolean()) {
                int value = RANDOM.nextInt();
                mine.push(value);
                reference.push(value);
            } else {
                require(mine.peek().equals(reference.peek()), "LinkedStack peek");
                require(mine.pop().equals(reference.pop()), "LinkedStack pop");
            }
            require(mine.size() == reference.size(), "LinkedStack size");
        }
    }

    private static void checkIntQueue() {
        for (int trial = 0; trial < 500; trial++) {
            IntQueue mine = new IntQueue();
            Deque<Integer> reference = new ArrayDeque<>();
            for (int op = 0; op < 200; op++) {
                if (reference.isEmpty() || RANDOM.nextInt(3) > 0) {
                    int value = RANDOM.nextInt(1000);
                    mine.enqueue(value);
                    reference.offer(value);
                } else {
                    require(mine.peek() == reference.peek(), "IntQueue peek");
                    require(mine.dequeue() == reference.poll(), "IntQueue dequeue");
                }
                require(mine.size() == reference.size(), "IntQueue size");
            }
        }
    }

    private static void checkQueueWithStacks() {
        QueueWithStacks<Integer> mine = new QueueWithStacks<>();
        Deque<Integer> reference = new ArrayDeque<>();
        for (int op = 0; op < 20_000; op++) {
            if (reference.isEmpty() || RANDOM.nextBoolean()) {
                int value = RANDOM.nextInt();
                mine.enqueue(value);
                reference.offer(value);
            } else {
                require(mine.peek().equals(reference.peek()), "QueueWithStacks peek");
                require(mine.dequeue().equals(reference.poll()), "QueueWithStacks poll");
            }
            require(mine.isEmpty() == reference.isEmpty(), "QueueWithStacks isEmpty");
        }
    }

    private static void checkStackWithQueue() {
        StackWithQueue<Integer> mine = new StackWithQueue<>();
        Deque<Integer> reference = new ArrayDeque<>();
        for (int op = 0; op < 3_000; op++) {
            if (reference.isEmpty() || RANDOM.nextBoolean()) {
                int value = RANDOM.nextInt();
                mine.push(value);
                reference.push(value);
            } else {
                require(mine.peek().equals(reference.peek()), "StackWithQueue peek");
                require(mine.pop().equals(reference.pop()), "StackWithQueue pop");
            }
            require(mine.isEmpty() == reference.isEmpty(), "StackWithQueue isEmpty");
        }
    }

    private static void checkBalanced() {
        String alphabet = "()[]{}";
        for (int trial = 0; trial < 20_000; trial++) {
            int length = RANDOM.nextInt(11);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append(alphabet.charAt(RANDOM.nextInt(alphabet.length())));
            }
            String s = sb.toString();
            require(BalancedParentheses.isBalanced(s)
                    == BalancedParentheses.isBalancedByErasing(s), "isBalanced " + s);
        }
        require(BalancedParentheses.isBalanced("x(y[z]{w})v"), "letters are ignored");
    }

    /** Builds random expression trees and checks both evaluators against them. */
    private static void checkExpressions() {
        for (int trial = 0; trial < 5_000; trial++) {
            StringBuilder rpn = new StringBuilder();
            StringBuilder infix = new StringBuilder();
            long expected = randomExpression(3, rpn, infix);
            int rpnValue = EvaluateRpn.evaluate(rpn.toString().trim().split(" "));
            require(rpnValue == expected, "RPN " + rpn + " expected " + expected);
            long infixValue = InfixCalculator.evaluate(infix.toString());
            require(infixValue == expected, "infix " + infix);
            String flat = infix.toString().replace("(", "").replace(")", "");
            long flatExpected = precedenceValue(flat);
            if (flatExpected != Long.MIN_VALUE) {
                require(InfixCalculator.evaluate(flat) == flatExpected, "flat " + flat);
            }
        }
    }

    /** Appends an expression to both builders; returns its value (no division by 0). */
    private static long randomExpression(int depth, StringBuilder rpn,
                                         StringBuilder infix) {
        if (depth == 0 || RANDOM.nextInt(3) == 0) {
            int number = RANDOM.nextInt(10);  // small enough that int never overflows
            rpn.append(number).append(' ');
            infix.append(number);
            return number;
        }
        char operator = "+-*/".charAt(RANDOM.nextInt(4));
        StringBuilder leftRpn = new StringBuilder();
        StringBuilder leftInfix = new StringBuilder();
        long left = randomExpression(depth - 1, leftRpn, leftInfix);
        StringBuilder rightRpn = new StringBuilder();
        StringBuilder rightInfix = new StringBuilder();
        long right = randomExpression(depth - 1, rightRpn, rightInfix);
        if (operator == '/' && right == 0) {
            operator = '+';
        }
        rpn.append(leftRpn).append(rightRpn).append(operator).append(' ');
        infix.append('(').append(leftInfix).append(' ').append(operator).append(' ')
                .append(rightInfix).append(')');
        return switch (operator) {
            case '+' -> left + right;
            case '-' -> left - right;
            case '*' -> left * right;
            default -> left / right;
        };
    }

    /**
     * Independent evaluator for an expression without parentheses: first collapse every
     * run of * and /, left to right, then add and subtract the resulting terms.
     * Returns Long.MIN_VALUE if the expression divides by zero (the check then skips it).
     */
    private static long precedenceValue(String flat) {
        String[] tokens = flat.split(" ");
        long total = 0;
        long term = Long.parseLong(tokens[0]);
        char sign = '+';
        for (int i = 1; i < tokens.length; i += 2) {
            char operator = tokens[i].charAt(0);
            long number = Long.parseLong(tokens[i + 1]);
            if (operator == '*') {
                term *= number;
            } else if (operator == '/') {
                if (number == 0) {
                    return Long.MIN_VALUE;
                }
                term /= number;
            } else {
                total = sign == '+' ? total + term : total - term;
                sign = operator;
                term = number;
            }
        }
        return sign == '+' ? total + term : total - term;
    }

    private static int[] randomArray(int maxLength, int bound) {
        return RANDOM.ints(RANDOM.nextInt(maxLength + 1), 0, bound).toArray();
    }

    private static void checkNextGreater() {
        for (int trial = 0; trial < 5_000; trial++) {
            int[] values = randomArray(12, 6);
            require(Arrays.equals(NextGreater.nextGreater(values),
                    NextGreater.nextGreaterBrute(values)), "nextGreater");
            int[] previous = NextGreater.previousSmallerIndex(values);
            for (int i = 0; i < values.length; i++) {
                int expected = -1;
                for (int j = i - 1; j >= 0; j--) {
                    if (values[j] < values[i]) {
                        expected = j;
                        break;
                    }
                }
                require(previous[i] == expected, "previousSmallerIndex");
            }
        }
    }

    private static void checkLargestRectangle() {
        for (int trial = 0; trial < 5_000; trial++) {
            int[] heights = randomArray(12, 8);
            require(LargestRectangle.largestRectangle(heights)
                    == LargestRectangle.largestRectangleBrute(heights), "rectangle");
        }
        int[] tall = new int[100_000];
        Arrays.fill(tall, 100_000);
        long area = LargestRectangle.largestRectangle(tall);
        require(area == 10_000_000_000L, "no overflow");
    }

    private static void checkSlidingWindow() {
        for (int trial = 0; trial < 5_000; trial++) {
            int[] values = randomArray(12, 10);
            if (values.length == 0) {
                continue;
            }
            int k = 1 + RANDOM.nextInt(values.length);
            require(Arrays.equals(SlidingWindowMax.windowMaxima(values, k),
                    SlidingWindowMax.windowMaximaBrute(values, k)), "windowMaxima");
        }
    }

    /** Compares BFS with repeated relaxation (slow but clearly right) on small grids. */
    private static void checkGridBfs() {
        for (int trial = 0; trial < 2_000; trial++) {
            int rows = 1 + RANDOM.nextInt(5);
            int cols = 1 + RANDOM.nextInt(5);
            char[][] grid = new char[rows][cols];
            for (char[] row : grid) {
                for (int c = 0; c < cols; c++) {
                    row[c] = RANDOM.nextInt(10) < 3 ? '#' : '.';
                }
            }
            require(GridBfs.shortestPath(grid) == relaxation(grid), "GridBfs");
        }
    }

    private static int relaxation(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int infinity = Integer.MAX_VALUE / 2;
        int[][] dist = new int[rows][cols];
        for (int[] row : dist) {
            Arrays.fill(row, infinity);
        }
        if (grid[0][0] == '#' || grid[rows - 1][cols - 1] == '#') {
            return -1;
        }
        dist[0][0] = 0;
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (grid[r][c] == '#') {
                        continue;
                    }
                    int[][] around = {{r - 1, c}, {r + 1, c}, {r, c - 1}, {r, c + 1}};
                    for (int[] n : around) {
                        boolean inside =
                                n[0] >= 0 && n[0] < rows && n[1] >= 0 && n[1] < cols;
                        if (inside && dist[n[0]][n[1]] + 1 < dist[r][c]) {
                            dist[r][c] = dist[n[0]][n[1]] + 1;
                            changed = true;
                        }
                    }
                }
            }
        }
        int answer = dist[rows - 1][cols - 1];
        return answer >= infinity ? -1 : answer;
    }
}
