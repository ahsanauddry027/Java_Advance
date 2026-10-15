/** Exercise 1.7: reverse the order of words, collapsing runs of spaces. */
public class ReverseWords {

    // region solve
    static String reverseWords(String sentence) {
        String trimmed = sentence.strip();
        if (trimmed.isEmpty()) {
            return "";
        }
        String[] words = trimmed.split("\\s+");      // one or more whitespace characters
        StringBuilder result = new StringBuilder();
        for (int i = words.length - 1; i >= 0; i--) {
            result.append(words[i]);
            if (i > 0) {
                result.append(' ');
            }
        }
        return result.toString();
    }
    // endregion solve

    public static void main(String[] args) {
        System.out.println("[" + reverseWords("  the sky   is blue ") + "]");
        System.out.println("[" + reverseWords("single") + "]");
        System.out.println("[" + reverseWords("    ") + "]");
    }
}
