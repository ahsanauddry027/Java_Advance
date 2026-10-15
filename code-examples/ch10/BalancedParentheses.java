import java.util.ArrayDeque;
import java.util.Deque;

/** Checks whether every bracket in a string is closed by the matching bracket. */
public class BalancedParentheses {

    // region isBalanced
    static boolean isBalanced(String text) {
        Deque<Character> open = new ArrayDeque<>();  // brackets not yet closed
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '(' || ch == '[' || ch == '{') {
                open.push(ch);
            } else if (ch == ')' || ch == ']' || ch == '}') {
                if (open.isEmpty() || open.pop() != matchingOpen(ch)) {
                    return false;  // nothing to close, or the wrong kind of bracket
                }
            }
            // any other character is ignored
        }
        return open.isEmpty();  // leftover openers were never closed
    }

    private static char matchingOpen(char close) {
        return switch (close) {
            case ')' -> '(';
            case ']' -> '[';
            default -> '{';
        };
    }
    // endregion isBalanced

    // region erasing
    /** Brute force: keep deleting adjacent pairs "()", "[]", "{}" until none remain. */
    static boolean isBalancedByErasing(String brackets) {
        String current = brackets;
        while (true) {
            String next = current.replace("()", "").replace("[]", "").replace("{}", "");
            if (next.equals(current)) {
                return current.isEmpty();
            }
            current = next;
        }
    }
    // endregion erasing

    public static void main(String[] args) {
        String[] samples = {"([]{})", "([)]", "((()", "())", "", "a[b(c)d]e"};
        for (String s : samples) {
            System.out.printf("%-12s %s%n", "\"" + s + "\"", isBalanced(s));
        }
    }
}
