import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Times three ways of reading one million integers and three ways of printing them.
 * Absolute times depend on the computer; compare the ratios.
 */
public class IoBenchmark {

    static final int COUNT = 1_000_000;

    public static void main(String[] args) throws IOException {
        byte[] input = makeInput(COUNT);
        long expected = 0;
        for (int round = 0; round < 2; round++) {  // round 0 only warms up the JIT
            long t0 = System.nanoTime();
            long s1 = sumWithScanner(input);
            long t1 = System.nanoTime();
            long s2 = sumWithBufferedReader(input);
            long t2 = System.nanoTime();
            long s3 = sumWithFastReader(input);
            long t3 = System.nanoTime();
            if (s1 != s2 || s2 != s3) {
                throw new AssertionError("readers disagree");
            }
            expected = s1;
            if (round == 1) {
                report("Scanner                  ", t1 - t0, t1 - t0);
                report("BufferedReader+Tokenizer ", t2 - t1, t1 - t0);
                report("FastReader               ", t3 - t2, t1 - t0);
            }
        }
        System.out.println("(all three read the same sum: " + expected + ")");

        int[] values = new Random(7).ints(COUNT, 0, 1_000_000).toArray();
        for (int round = 0; round < 2; round++) {
            long t0 = System.nanoTime();
            printWithAutoFlush(values);
            long t1 = System.nanoTime();
            printWithPrintWriter(values);
            long t2 = System.nanoTime();
            printWithStringBuilder(values);
            long t3 = System.nanoTime();
            if (round == 1) {
                report("println, autoflush stream", t1 - t0, t1 - t0);
                report("PrintWriter, one flush   ", t2 - t1, t1 - t0);
                report("StringBuilder, one write ", t3 - t2, t1 - t0);
            }
        }
    }

    static void report(String label, long nanos, long baseline) {
        System.out.printf("%s %6d ms  (%.2f of the first)%n",
                label, nanos / 1_000_000, (double) nanos / baseline);
    }

    static byte[] makeInput(int count) {
        Random random = new Random(42);
        StringBuilder sb = new StringBuilder();
        sb.append(count).append('\n');
        for (int i = 0; i < count; i++) {
            sb.append(random.nextInt(2_000_001) - 1_000_000);
            sb.append(i % 10 == 9 ? '\n' : ' ');        // ten numbers per line
        }
        return sb.toString().getBytes(StandardCharsets.US_ASCII);
    }

    static long sumWithScanner(byte[] input) {
        Scanner in = new Scanner(new ByteArrayInputStream(input));
        int n = in.nextInt();
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += in.nextInt();
        }
        return sum;
    }

    static long sumWithBufferedReader(byte[] input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ByteArrayInputStream(input), StandardCharsets.US_ASCII));
        StringTokenizer tokens = new StringTokenizer(reader.readLine());
        int n = Integer.parseInt(tokens.nextToken());
        long sum = 0;
        for (int i = 0; i < n; i++) {
            while (!tokens.hasMoreTokens()) {
                tokens = new StringTokenizer(reader.readLine());  // next line
            }
            sum += Integer.parseInt(tokens.nextToken());
        }
        return sum;
    }

    static long sumWithFastReader(byte[] input) throws IOException {
        FastReader in = new FastReader(new ByteArrayInputStream(input));
        int n = in.nextInt();
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += in.nextInt();
        }
        return sum;
    }

    static void printWithAutoFlush(int[] values) {
        PrintStream out = new PrintStream(OutputStream.nullOutputStream(), true);
        for (int value : values) {
            out.println(value);                      // flushes after every line
        }
    }

    static void printWithPrintWriter(int[] values) {
        PrintWriter out = new PrintWriter(OutputStream.nullOutputStream());
        for (int value : values) {
            out.println(value);
        }
        out.flush();
    }

    static void printWithStringBuilder(int[] values) {
        StringBuilder sb = new StringBuilder();
        for (int value : values) {
            sb.append(value).append('\n');
        }
        PrintStream out = new PrintStream(OutputStream.nullOutputStream());
        out.print(sb);
        out.flush();
    }
}
