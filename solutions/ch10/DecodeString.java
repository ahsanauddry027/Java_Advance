import java.util.ArrayDeque;
import java.util.Deque;

/** Exercise 10.8: expand an encoded string such as "3[a2[c]]" into "accaccacc". */
public class DecodeString {

    // region solve
    static String decode(String encoded) {
        Deque<Integer> counts = new ArrayDeque<>();        // repeat count per open '['
        Deque<StringBuilder> outer = new ArrayDeque<>();   // text built before each '['
        StringBuilder current = new StringBuilder();
        int number = 0;
        for (int i = 0; i < encoded.length(); i++) {
            char ch = encoded.charAt(i);
            if (ch >= '0' && ch <= '9') {
                number = number * 10 + (ch - '0');         // a count may have many digits
            } else if (ch == '[') {
                counts.push(number);
                outer.push(current);
                current = new StringBuilder();             // start the bracketed part
                number = 0;
            } else if (ch == ']') {
                String inner = current.toString();
                current = outer.pop();
                current.append(inner.repeat(counts.pop()));
            } else {
                current.append(ch);
            }
        }
        return current.toString();
    }
    // endregion solve

    public static void main(String[] args) {
        String[] samples = {"3[a]2[bc]", "3[a2[c]]", "2[abc]3[cd]ef", "10[x]"};
        for (String s : samples) {
            System.out.printf("%-14s -> %s%n", s, decode(s));
        }
    }
}
