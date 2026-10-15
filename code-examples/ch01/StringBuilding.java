/** Strings never change; building text with += copies it again and again. */
public class StringBuilding {

    // region naive
    /** Builds "0,1,2,...," with +=; each += makes a new string and copies the old. */
    static String joinNaive(int n) {
        String result = "";
        for (int i = 0; i < n; i++) {
            result += i + ",";
        }
        return result;
    }
    // endregion naive

    // region builder
    /** Builds the same text with one growable buffer. */
    static String joinWithBuilder(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(i).append(',');
        }
        return sb.toString();
    }
    // endregion builder

    // region count
    /** Characters that joinNaive copies in total: every new string is written in full. */
    static long charactersCopiedNaive(int n) {
        long copied = 0;
        long length = 0;
        for (int i = 0; i < n; i++) {
            length += String.valueOf(i).length() + 1;  // the string after this step
            copied += length;
        }
        return copied;
    }
    // endregion count

    public static void main(String[] args) {
        // region immutable
        String word = "stack";
        String upper = word.toUpperCase();  // returns a NEW string; word is unchanged
        word.concat("s");                   // the result is thrown away: a classic bug
        System.out.println(word + " " + upper);
        // endregion immutable

        boolean same = joinNaive(2_000).equals(joinWithBuilder(2_000));
        System.out.println("same text: " + same);
        for (int n : new int[] {1_000, 10_000, 100_000}) {
            System.out.printf("n = %,7d: final length %,9d, += copies %,16d characters%n",
                    n, joinWithBuilder(n).length(), charactersCopiedNaive(n));
        }

        // region builder-methods
        StringBuilder sb = new StringBuilder("level");
        sb.insert(0, '[').append(']');     // [level]
        sb.setCharAt(1, 'L');              // [Level]
        sb.deleteCharAt(sb.length() - 1);  // [Level
        System.out.println(sb + ", length " + sb.length() + ", [2] " + sb.charAt(2));
        System.out.println(new StringBuilder("stressed").reverse());
        // endregion builder-methods
    }
}
