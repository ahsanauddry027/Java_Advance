/** Exercise 10.2: position of the first bracket error, or -1 if the brackets balance. */
public class FirstBracketError {

    // region solve
    static int firstError(String text) {
        int[] openIndex = new int[text.length()];  // stack of indices of open brackets
        int top = 0;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '(' || ch == '[' || ch == '{') {
                openIndex[top++] = i;
            } else if (ch == ')' || ch == ']' || ch == '}') {
                if (top == 0 || !matches(text.charAt(openIndex[top - 1]), ch)) {
                    return i;                  // a closer with no matching opener
                }
                top--;
            }
        }
        return top == 0 ? -1 : openIndex[0];   // earliest opener never closed
    }

    private static boolean matches(char open, char close) {
        return (open == '(' && close == ')') || (open == '[' && close == ']')
                || (open == '{' && close == '}');
    }
    // endregion solve

    public static void main(String[] args) {
        String[] samples = {"a(b[c]d)e", "x(y]z", "((a)", "a)b(", ""};
        for (String s : samples) {
            System.out.printf("%-12s %d%n", "\"" + s + "\"", firstError(s));
        }
    }
}
