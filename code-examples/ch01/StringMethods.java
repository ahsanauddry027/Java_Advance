import java.util.Arrays;

/** The String methods that appear most often in DSA problems, and their traps. */
public class StringMethods {

    public static void main(String[] args) {
        // region basics
        String s = "racecar";
        System.out.println(s.length() + " " + s.charAt(0) + " " + s.indexOf('c')
                + " " + s.lastIndexOf('c') + " " + s.substring(2, 5)
                + " " + s.contains("cec"));
        char[] letters = s.toCharArray();       // a copy you may modify
        Arrays.sort(letters);
        System.out.println(new String(letters) + " " + String.valueOf(letters, 0, 3));
        // endregion basics

        // region compare
        System.out.println("apple".compareTo("banana") + " " + "b".compareTo("a")
                + " " + "Apple".equalsIgnoreCase("APPLE"));
        // endregion compare

        // region split
        System.out.println(Arrays.toString("a,b,,c,,".split(",")));  // trailing "" lost
        System.out.println(Arrays.toString("1.5.2".split(".")));     // "." is a regex!
        System.out.println(Arrays.toString("1.5.2".split("\\.")));
        System.out.println(Arrays.toString("  two   spaces ".strip().split("\\s+")));
        // endregion split

        // region build
        System.out.println(String.join("-", "x", "y", "z") + " " + "ab".repeat(3)
                + " " + String.valueOf(42) + " " + Integer.parseInt("-17"));
        // endregion build

        // region chars
        char c = 'G';
        System.out.println(Character.isLetter(c) + " " + Character.isDigit('7')
                + " " + Character.toLowerCase(c) + " " + Character.isLetterOrDigit('_'));
        // endregion chars
    }
}
