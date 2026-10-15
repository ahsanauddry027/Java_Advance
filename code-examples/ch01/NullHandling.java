import java.util.Objects;

/** Where NullPointerException comes from, and simple ways to avoid it. */
public class NullHandling {

    // region guard
    /** Rejects null at the start of a method, with a clear message. */
    static int countVowels(String text) {
        Objects.requireNonNull(text, "text must not be null");
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if ("aeiouAEIOU".indexOf(text.charAt(i)) >= 0) {
                count++;
            }
        }
        return count;
    }
    // endregion guard

    public static void main(String[] args) {
        // region npe
        String missing = null;
        try {
            System.out.println(missing.length());
        } catch (NullPointerException e) {
            System.out.println("NPE: " + e.getMessage());
        }
        Integer boxed = null;
        try {
            int value = boxed;                     // unboxing null
            System.out.println(value);
        } catch (NullPointerException e) {
            System.out.println("NPE (unboxing): " + e.getMessage());
        }
        // endregion npe

        // region safe
        String answer = null;
        System.out.println("\"yes\".equals(answer)       " + "yes".equals(answer));
        boolean same = Objects.equals(answer, null);  // true: both are null
        System.out.println("Objects.equals(answer, null) " + same);
        System.out.println("requireNonNullElse          "
                + Objects.requireNonNullElse(answer, "(no answer)"));
        // endregion safe

        try {
            countVowels(null);
        } catch (NullPointerException e) {
            System.out.println("guard: " + e.getMessage());
        }
    }
}
