/**
 * Exercise 10.11: remove exactly k digits from a number (given as a string) so that the
 * remaining number is as small as possible. Leading zeros are dropped; "" becomes "0".
 */
public class RemoveKDigits {

    // region solve
    static String removeKDigits(String number, int k) {
        StringBuilder kept = new StringBuilder();  // a stack of digits, never decreasing
        int toRemove = k;
        for (int i = 0; i < number.length(); i++) {
            char digit = number.charAt(i);
            while (toRemove > 0 && !kept.isEmpty()
                    && kept.charAt(kept.length() - 1) > digit) {
                kept.deleteCharAt(kept.length() - 1);  // a smaller digit can move left
                toRemove--;
            }
            kept.append(digit);
        }
        kept.setLength(kept.length() - toRemove);     // removals still owed: cut the end
        int start = 0;
        while (start < kept.length() - 1 && kept.charAt(start) == '0') {
            start++;                                  // skip leading zeros
        }
        String result = kept.substring(start);
        return result.isEmpty() ? "0" : result;
    }
    // endregion solve

    public static void main(String[] args) {
        System.out.println(removeKDigits("1432219", 3));
        System.out.println(removeKDigits("10200", 1));
        System.out.println(removeKDigits("10", 2));
        System.out.println(removeKDigits("112", 1));
    }
}
