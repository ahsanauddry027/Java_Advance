import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/** Exercise 2.1: BufferedReader input where numbers may be spread over many lines. */
public class TokenReaderSum {

    // region solve
    private static BufferedReader reader;
    private static StringTokenizer tokens = new StringTokenizer("");

    /** Returns the next token, reading more lines as needed (null at end of input). */
    static String next() throws IOException {
        while (!tokens.hasMoreTokens()) {
            String line = reader.readLine();
            if (line == null) {
                return null;
            }
            tokens = new StringTokenizer(line);   // blank lines simply give no tokens
        }
        return tokens.nextToken();
    }

    public static void main(String[] args) throws IOException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(next());
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += Integer.parseInt(next());
        }
        System.out.println(sum);
    }
    // endregion solve
}
