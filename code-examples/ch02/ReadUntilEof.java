import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

/** Reads "name score" lines until the input ends; the number of lines is not given. */
public class ReadUntilEof {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder out = new StringBuilder();
        int count = 0;
        long total = 0;
        String line;
        while ((line = reader.readLine()) != null) {  // null means end of input
            line = line.strip();
            if (line.isEmpty()) {
                continue;                               // tolerate blank lines
            }
            String[] parts = line.split("\\s+");
            String name = parts[0];
            int score = Integer.parseInt(parts[1]);
            count++;
            total += score;
            out.append(name).append(": ").append(score).append('\n');
        }
        out.append("students: ").append(count)
                .append(", average: ")
                .append(String.format(Locale.ROOT, "%.2f", (double) total / count));
        System.out.println(out);
    }
}
