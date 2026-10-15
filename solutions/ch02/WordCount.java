import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/** Exercise 2.2: count lines, words and characters until the end of the input. */
public class WordCount {

    public static void main(String[] args) throws IOException {
        // region solve
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int lines = 0;
        int words = 0;
        long characters = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            lines++;
            characters += line.length();          // readLine removes the line break
            words += new StringTokenizer(line).countTokens();
        }
        System.out.println(lines + " lines, " + words + " words, " + characters
                + " characters (not counting line breaks)");
        // endregion solve
    }
}
