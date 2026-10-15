import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/** The same task as ScannerSum, with BufferedReader and StringTokenizer. */
public class BufferedSum {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(reader.readLine().trim());
        StringTokenizer tokens = new StringTokenizer(reader.readLine());
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += Integer.parseInt(tokens.nextToken());
        }
        System.out.println(sum);
    }
}
