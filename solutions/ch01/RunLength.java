/** Exercise 1.11: run-length encoding, "aaabcc" becomes "a3b1c2". */
public class RunLength {

    // region solve
    static String encode(String text) {
        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < text.length()) {
            char current = text.charAt(i);
            int runEnd = i;
            while (runEnd < text.length() && text.charAt(runEnd) == current) {
                runEnd++;                           // extend the run of equal characters
            }
            out.append(current).append(runEnd - i);
            i = runEnd;                             // continue after the run
        }
        return out.toString();
    }
    // endregion solve

    public static void main(String[] args) {
        System.out.println(encode("aaabcc"));
        System.out.println(encode("abc"));
        System.out.println("[" + encode("") + "]");
        System.out.println(encode("z".repeat(12)));
    }
}
