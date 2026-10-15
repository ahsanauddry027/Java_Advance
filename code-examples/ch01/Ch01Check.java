import java.util.Random;

/** Self-checks for the Chapter 1 examples. Exits with an error if any check fails. */
public class Ch01Check {

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void main(String[] args) {
        // joinNaive and joinWithBuilder build the same text.
        for (int n = 0; n <= 300; n++) {
            require(StringBuilding.joinNaive(n).equals(StringBuilding.joinWithBuilder(n)),
                    "join " + n);
        }
        // charactersCopiedNaive equals the total length of all intermediate strings.
        for (int n = 0; n <= 300; n++) {
            long expected = 0;
            String current = "";
            for (int i = 0; i < n; i++) {
                current += i + ",";
                expected += current.length();
            }
            require(StringBuilding.charactersCopiedNaive(n) == expected, "copied " + n);
        }
        // countVowels against a direct count.
        Random random = new Random(1);
        String alphabet = "aeiouAEIOUbcdxyz ";
        for (int trial = 0; trial < 2_000; trial++) {
            StringBuilder sb = new StringBuilder();
            int length = random.nextInt(20);
            int vowels = 0;
            for (int i = 0; i < length; i++) {
                char ch = alphabet.charAt(random.nextInt(alphabet.length()));
                sb.append(ch);
                if ("aeiouAEIOU".indexOf(ch) >= 0) {
                    vowels++;
                }
            }
            require(NullHandling.countVowels(sb.toString()) == vowels, "countVowels");
        }
        System.out.println("Ch01Check: all checks passed");
    }
}
