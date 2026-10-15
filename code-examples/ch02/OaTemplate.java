import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * A reusable online-assessment template (Java 8 or later). Rename the class to what the
 * judge requires, usually Main, and replace solve().
 * Example problem: for each test case, read n and n integers; print min, max and sum.
 */
public class OaTemplate {

    // region main
    public static void main(String[] args) throws IOException {
        FastIn in = new FastIn(System.in);
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        int tests = in.nextInt();
        for (int t = 1; t <= tests; t++) {
            solve(in, out);
        }
        out.flush();  // without this, buffered output may never appear
    }
    // endregion main

    // region solve
    static void solve(FastIn in, PrintWriter out) throws IOException {
        int n = in.nextInt();
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        long sum = 0;
        for (int i = 0; i < n; i++) {
            long value = in.nextLong();
            min = Math.min(min, value);
            max = Math.max(max, value);
            sum += value;
        }
        out.println(min + " " + max + " " + sum);
    }
    // endregion solve

    // region fastin
    /** Minimal fast reader for whitespace-separated integers and words. */
    static final class FastIn {
        private final InputStream stream;
        private final byte[] buf = new byte[1 << 16];
        private int len;
        private int pos;

        FastIn(InputStream stream) {
            this.stream = stream;
        }

        private int read() throws IOException {
            if (pos == len) {
                len = stream.read(buf, 0, buf.length);
                pos = 0;
                if (len <= 0) {
                    len = 0;
                    return -1;              // end of input
                }
            }
            return buf[pos++] & 0xFF;
        }

        long nextLong() throws IOException {
            int c = read();
            while (c != -1 && c <= ' ') {
                c = read();                 // skip spaces and line breaks
            }
            boolean negative = c == '-';
            if (negative) {
                c = read();
            }
            long value = 0;
            while (c >= '0' && c <= '9') {
                value = value * 10 + (c - '0');
                c = read();
            }
            return negative ? -value : value;
        }

        int nextInt() throws IOException {
            return (int) nextLong();
        }

        String next() throws IOException {
            int c = read();
            while (c != -1 && c <= ' ') {
                c = read();
            }
            StringBuilder sb = new StringBuilder();
            while (c > ' ') {
                sb.append((char) c);
                c = read();
            }
            return sb.toString();
        }
    }
    // endregion fastin
}
