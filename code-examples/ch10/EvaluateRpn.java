import java.util.ArrayDeque;
import java.util.Deque;

/** Evaluates an expression in Reverse Polish (postfix) notation, e.g. "3 4 + 2 *". */
public class EvaluateRpn {

    // region evaluate
    static int evaluate(String[] tokens) {
        Deque<Integer> operands = new ArrayDeque<>();
        for (String token : tokens) {
            switch (token) {
                case "+", "-", "*", "/" -> {
                    int right = operands.pop();  // the top is the RIGHT operand
                    int left = operands.pop();
                    operands.push(apply(token, left, right));
                }
                default -> operands.push(Integer.parseInt(token));
            }
        }
        if (operands.size() != 1) {
            throw new IllegalArgumentException("not a complete expression");
        }
        return operands.pop();
    }

    private static int apply(String operator, int left, int right) {
        return switch (operator) {
            case "+" -> left + right;
            case "-" -> left - right;
            case "*" -> left * right;
            case "/" -> left / right;  // Java int division rounds toward zero
            default -> throw new IllegalArgumentException("unknown operator " + operator);
        };
    }
    // endregion evaluate

    public static void main(String[] args) {
        String[] expressions = {"3 4 + 2 *", "5 1 2 + 4 * + 3 -", "7 2 /", "-7 2 /"};
        for (String expression : expressions) {
            int value = evaluate(expression.split(" "));
            System.out.printf("%-20s = %d%n", expression, value);
        }
    }
}
