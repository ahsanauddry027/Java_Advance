/** Exercise 1.9: palindrome check that ignores case and non-alphanumeric characters. */
public class Palindrome {

    // region solve
    static boolean isPalindrome(String text) {
        int left = 0;
        int right = text.length() - 1;
        while (left < right) {
            char a = text.charAt(left);
            char b = text.charAt(right);
            if (!Character.isLetterOrDigit(a)) {
                left++;                               // skip punctuation and spaces
            } else if (!Character.isLetterOrDigit(b)) {
                right--;
            } else if (Character.toLowerCase(a) != Character.toLowerCase(b)) {
                return false;
            } else {
                left++;
                right--;
            }
        }
        return true;
    }
    // endregion solve

    public static void main(String[] args) {
        System.out.println(isPalindrome("A man, a plan, a canal: Panama"));
        System.out.println(isPalindrome("race a car"));
        System.out.println(isPalindrome(" ,. "));
    }
}
