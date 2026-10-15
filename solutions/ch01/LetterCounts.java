/** Exercise 1.6: count lowercase letters with an int[26] instead of a map. */
public class LetterCounts {

    // region solve
    static String letterCounts(String text) {
        int[] counts = new int[26];
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch >= 'a' && ch <= 'z') {
                counts[ch - 'a']++;            // 'a' -> 0, 'b' -> 1, ..., 'z' -> 25
            }
        }
        StringBuilder result = new StringBuilder();
        for (int letter = 0; letter < 26; letter++) {
            if (counts[letter] > 0) {
                result.append((char) ('a' + letter)).append(counts[letter]);
            }
        }
        return result.toString();
    }
    // endregion solve

    public static void main(String[] args) {
        System.out.println(letterCounts("banana"));
        System.out.println(letterCounts("hello, world!"));
        System.out.println("[" + letterCounts("123") + "]");
    }
}
