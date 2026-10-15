import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Exercise 2.12: parse the integers on a line by hand, without split or StringTokenizer,
 * and print the sum of every line until the end of the input.
 */
public class LineParser {

    // region solve
    /** All integers on the line, in order. Accepts any amount of spaces and tabs. */
    static int[] parseInts(String line) {
        int[] values = new int[line.length() / 2 + 1];  // more than enough slots
        int count = 0;
        int i = 0;
        while (i < line.length()) {
            char c = line.charAt(i);
            if (c == ' ' || c == '\t') {
                i++;
                continue;
            }
            boolean negative = c == '-';
            if (negative) {
                i++;
            }
            int value = 0;
            while (i < line.length() && line.charAt(i) >= '0' && line.charAt(i) <= '9') {
                value = value * 10 + (line.charAt(i) - '0');
                i++;
            }
            values[count++] = negative ? -value : value;
        }
        return Arrays.copyOf(values, count);
    }
    // endregion solve

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        int lineNumber = 0;
        while ((line = reader.readLine()) != null) {
            lineNumber++;
            int[] values = parseInts(line);
            long sum = 0;
            for (int v : values) {
                sum += v;
            }
            System.out.println("line " + lineNumber + ": " + Arrays.toString(values)
                    + " sum " + sum);
        }
    }
}
