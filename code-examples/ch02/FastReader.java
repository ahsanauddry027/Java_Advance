import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Reads whitespace-separated numbers and words from a byte stream, much faster than
 * Scanner. Words are read as ASCII; use BufferedReader for text in other scripts.
 */
public class FastReader {

    // region reader
    private final InputStream in;
    private final byte[] buffer;
    private int length;    // how many bytes the buffer currently holds
    private int position;  // index of the next unread byte

    public FastReader(InputStream in) {
        this(in, 1 << 16);  // 64 KB
    }

    FastReader(InputStream in, int bufferSize) {
        this.in = in;
        this.buffer = new byte[bufferSize];
    }

    /** Makes sure at least one unread byte is buffered; returns false at end of input. */
    private boolean fill() throws IOException {
        if (position < length) {
            return true;
        }
        length = in.read(buffer, 0, buffer.length);
        position = 0;
        if (length <= 0) {
            length = 0;
            return false;
        }
        return true;
    }

    /** True if another token follows; skips the whitespace before it. */
    public boolean hasNext() throws IOException {
        while (fill()) {
            if ((buffer[position] & 0xFF) > ' ') {
                return true;
            }
            position++;
        }
        return false;
    }

    public long nextLong() throws IOException {
        if (!hasNext()) {
            throw new EOFException("no more input");
        }
        boolean negative = buffer[position] == '-';
        if (negative) {
            position++;
        }
        long value = 0;
        while (fill() && buffer[position] >= '0' && buffer[position] <= '9') {
            value = value * 10 + (buffer[position] - '0');
            position++;
        }
        return negative ? -value : value;
    }

    public int nextInt() throws IOException {
        return (int) nextLong();  // assumes the input respects the int range
    }

    public String next() throws IOException {
        if (!hasNext()) {
            throw new EOFException("no more input");
        }
        StringBuilder token = new StringBuilder();
        while (fill() && (buffer[position] & 0xFF) > ' ') {
            token.append((char) buffer[position]);
            position++;
        }
        return token.toString();
    }
    // endregion reader

    public static void main(String[] args) throws IOException {
        FastReader reader = new FastReader(System.in);
        long sum = 0;
        int count = 0;
        while (reader.hasNext()) {  // read until the end of the input
            sum += reader.nextLong();
            count++;
        }
        System.out.println(count + " numbers, sum " + sum);
    }
}
