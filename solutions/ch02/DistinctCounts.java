import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * Exercise 2.11: for each test case, print every distinct value in increasing order with
 * its count. The total input may hold 2 * 10^5 numbers, so input and output must be fast.
 */
public class DistinctCounts {

    // region solve
    public static void main(String[] args) throws IOException {
        Reader in = new Reader(System.in);
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        int tests = in.nextInt();
        for (int t = 1; t <= tests; t++) {
            int n = in.nextInt();
            int[] values = new int[n];
            for (int i = 0; i < n; i++) {
                values[i] = in.nextInt();
            }
            Arrays.sort(values);                     // equal values become neighbours
            StringBuilder line = new StringBuilder("Case #").append(t).append(':');
            int i = 0;
            while (i < n) {
                int j = i;
                while (j < n && values[j] == values[i]) {
                    j++;                             // values[i..j-1] are all equal
                }
                line.append(' ').append(values[i]).append('x').append(j - i);
                i = j;
            }
            out.println(line);
        }
        out.flush();
    }
    // endregion solve

    /** The reader from OaTemplate (Chapter 2), reduced to what this problem needs. */
    static final class Reader {
        private final InputStream stream;
        private final byte[] buf = new byte[1 << 16];
        private int len;
        private int pos;

        Reader(InputStream stream) {
            this.stream = stream;
        }

        private int read() throws IOException {
            if (pos == len) {
                len = stream.read(buf, 0, buf.length);
                pos = 0;
                if (len <= 0) {
                    len = 0;
                    return -1;
                }
            }
            return buf[pos++] & 0xFF;
        }

        int nextInt() throws IOException {
            int c = read();
            while (c != -1 && c <= ' ') {
                c = read();
            }
            boolean negative = c == '-';
            if (negative) {
                c = read();
            }
            int value = 0;
            while (c >= '0' && c <= '9') {
                value = value * 10 + (c - '0');
                c = read();
            }
            return negative ? -value : value;
        }
    }
}
