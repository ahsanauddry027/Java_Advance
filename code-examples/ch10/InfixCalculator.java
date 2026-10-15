import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Evaluates infix expressions with + - * /, parentheses and non-negative integers,
 * using one stack for numbers and one for operators.
 */
public class InfixCalculator {

    // region evaluate
    static long evaluate(String expression) {
        Deque<Long> values = new ArrayDeque<>();
        Deque<Character> operators = new ArrayDeque<>();
        int i = 0;
        while (i < expression.length()) {
            char ch = expression.charAt(i);
            if (ch == ' ') {
                i++;
            } else if (ch >= '0' && ch <= '9') {
                long number = 0;
                while (i < expression.length()
                        && expression.charAt(i) >= '0' && expression.charAt(i) <= '9') {
                    number = number * 10 + (expression.charAt(i) - '0');
                    i++;
                }
                values.push(number);
            } else if (ch == '(') {
                operators.push(ch);
                i++;
            } else if (ch == ')') {
                while (operators.peek() != '(') {
                    applyTop(values, operators);
                }
                operators.pop();  // discard the '('
                i++;
            } else {  // one of + - * /
                while (!operators.isEmpty() && operators.peek() != '('
                        && precedence(operators.peek()) >= precedence(ch)) {
                    applyTop(values, operators);  // earlier operator binds first
                }
                operators.push(ch);
                i++;
            }
        }
        while (!operators.isEmpty()) {
            applyTop(values, operators);
        }
        return values.pop();
    }

    private static int precedence(char operator) {
        return (operator == '*' || operator == '/') ? 2 : 1;
    }

    private static void applyTop(Deque<Long> values, Deque<Character> operators) {
        char operator = operators.pop();
        long right = values.pop();
        long left = values.pop();
        long result = switch (operator) {
            case '+' -> left + right;
            case '-' -> left - right;
            case '*' -> left * right;
            default -> left / right;
        };
        values.push(result);
    }
    // endregion evaluate

    public static void main(String[] args) {
        String[] expressions = {
                "2 + 3 * 4", "(2 + 3) * 4", "10 - 4 - 3", "8 / (3 - 1) * 5"};
        for (String expression : expressions) {
            System.out.printf("%-16s = %d%n", expression, evaluate(expression));
        }
    }
}
