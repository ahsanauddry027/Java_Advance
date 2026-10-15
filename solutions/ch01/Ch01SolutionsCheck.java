import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Checks the Chapter 1 solutions against slower, obviously correct versions. */
public class Ch01SolutionsCheck {

    private static final Random RANDOM = new Random(101);

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void main(String[] args) {
        checkMidpoint();
        checkRoles();
        checkDeepCopy();
        checkSums();
        checkLetterCounts();
        checkReverseWords();
        checkPalindrome();
        checkFactorial();
        checkRunLength();
        checkMultiply();
        System.out.println("Ch01SolutionsCheck: all checks passed");
    }

    private static void checkMidpoint() {
        for (int trial = 0; trial < 100_000; trial++) {
            int low = RANDOM.nextInt(Integer.MAX_VALUE);
            int high = low + RANDOM.nextInt(Integer.MAX_VALUE - low + 1);
            long exact = ((long) low + high) / 2;
            require(Midpoint.midSafe(low, high) == exact, "midSafe");
            require(Midpoint.midUnsignedShift(low, high) == exact, "midUnsignedShift");
        }
    }

    private static void checkRoles() {
        String typed = new StringBuilder("nimda").reverse().toString();
        require(RoleCheck.isAdmin(typed) && !RoleCheck.isAdmin(null)
                && !RoleCheck.isAdmin("Admin"), "isAdmin");
    }

    private static void checkDeepCopy() {
        for (int trial = 0; trial < 1_000; trial++) {
            int[][] source = new int[RANDOM.nextInt(5)][];
            for (int r = 0; r < source.length; r++) {
                source[r] = RANDOM.nextInt(4) == 0 ? null
                        : RANDOM.ints(RANDOM.nextInt(4), 0, 10).toArray();
            }
            int[][] copy = DeepCopy.deepCopy(source);
            require(Arrays.deepEquals(source, copy), "deepCopy equal");
            for (int r = 0; r < source.length; r++) {
                require(source[r] == null || source[r] != copy[r], "new rows");
            }
        }
        require(DeepCopy.deepCopy(null) == null, "deepCopy(null)");
    }

    private static void checkSums() {
        for (int trial = 0; trial < 2_000; trial++) {
            int count = RANDOM.nextInt(6);
            int[] values = new int[count];
            for (int i = 0; i < count; i++) {
                values[i] = RANDOM.nextInt();          // any int, including the extremes
            }
            BigInteger exact = BigInteger.ZERO;
            for (int v : values) {
                exact = exact.add(BigInteger.valueOf(v));
            }
            require(SafeSum.sumAsLong(values) == exact.longValueExact(), "sumAsLong");
            boolean fits = exact.bitLength() < 32;
            boolean everyPrefixFits = true;
            BigInteger prefix = BigInteger.ZERO;
            for (int v : values) {
                prefix = prefix.add(BigInteger.valueOf(v));
                everyPrefixFits &= prefix.bitLength() < 32;
            }
            try {
                int result = SafeSum.sumExact(values);
                require(everyPrefixFits && fits && result == exact.intValue(), "exact");
            } catch (ArithmeticException e) {
                require(!everyPrefixFits, "sumExact threw although no prefix overflowed");
            }
        }
    }

    private static void checkLetterCounts() {
        String alphabet = "abcxyz ,!AB1";
        for (int trial = 0; trial < 2_000; trial++) {
            StringBuilder sb = new StringBuilder();
            for (int i = RANDOM.nextInt(15); i > 0; i--) {
                sb.append(alphabet.charAt(RANDOM.nextInt(alphabet.length())));
            }
            String text = sb.toString();
            StringBuilder expected = new StringBuilder();
            for (char letter = 'a'; letter <= 'z'; letter++) {
                int count = 0;
                for (char ch : text.toCharArray()) {
                    if (ch == letter) {
                        count++;
                    }
                }
                if (count > 0) {
                    expected.append(letter).append(count);
                }
            }
            String actual = LetterCounts.letterCounts(text);
            require(actual.equals(expected.toString()), "letters");
        }
    }

    private static void checkReverseWords() {
        String[] pieces = {"a", "bb", "ccc", " ", "  ", "\t"};
        for (int trial = 0; trial < 2_000; trial++) {
            StringBuilder sb = new StringBuilder();
            for (int i = RANDOM.nextInt(8); i > 0; i--) {
                sb.append(pieces[RANDOM.nextInt(pieces.length)]);
            }
            String s = sb.toString();
            List<String> words = new ArrayList<>();
            for (String w : s.split("[ \\t]+")) {
                if (!w.isEmpty()) {
                    words.add(w);
                }
            }
            Collections.reverse(words);
            require(ReverseWords.reverseWords(s).equals(String.join(" ", words)),
                    "reverseWords [" + s + "]");
        }
    }

    private static void checkPalindrome() {
        String alphabet = "abA ,1";
        for (int trial = 0; trial < 5_000; trial++) {
            StringBuilder sb = new StringBuilder();
            for (int i = RANDOM.nextInt(9); i > 0; i--) {
                sb.append(alphabet.charAt(RANDOM.nextInt(alphabet.length())));
            }
            String s = sb.toString();
            StringBuilder cleaned = new StringBuilder();
            for (char ch : s.toCharArray()) {
                if (Character.isLetterOrDigit(ch)) {
                    cleaned.append(Character.toLowerCase(ch));
                }
            }
            boolean expected = cleaned.toString().contentEquals(
                    new StringBuilder(cleaned).reverse());
            require(Palindrome.isPalindrome(s) == expected, "palindrome " + s);
        }
    }

    private static void checkFactorial() {
        BigInteger factorial = BigInteger.ONE;
        BigInteger mod = BigInteger.valueOf(FactorialMod.MOD);
        for (int n = 0; n <= 2_000; n++) {
            if (n > 0) {
                factorial = factorial.multiply(BigInteger.valueOf(n));
            }
            int expected = factorial.mod(mod).intValue();
            require(FactorialMod.factorialMod(n) == expected, "n! " + n);
        }
    }

    private static void checkRunLength() {
        for (int trial = 0; trial < 3_000; trial++) {
            StringBuilder sb = new StringBuilder();
            for (int i = RANDOM.nextInt(12); i > 0; i--) {
                sb.append((char) ('a' + RANDOM.nextInt(2)));
            }
            String s = sb.toString();
            String expected = "";
            int i = 0;
            while (i < s.length()) {
                int j = i;
                while (j < s.length() && s.charAt(j) == s.charAt(i)) {
                    j++;
                }
                expected += s.charAt(i) + String.valueOf(j - i);
                i = j;
            }
            require(RunLength.encode(s).equals(expected), "encode " + s);
        }
    }

    private static void checkMultiply() {
        long[] specials = {0, 1, -1, 2, -2, 46_340, 46_341,
                3_037_000_499L, 3_037_000_500L,
                Integer.MAX_VALUE, Integer.MIN_VALUE, Long.MAX_VALUE, Long.MIN_VALUE};
        List<Long> values = new ArrayList<>();
        for (long v : specials) {
            values.add(v);
        }
        for (int i = 0; i < 300; i++) {
            values.add(RANDOM.nextLong() >> RANDOM.nextInt(64));
        }
        for (long a : values) {
            for (long b : values) {
                boolean expected;
                try {
                    Math.multiplyExact(a, b);
                    expected = false;
                } catch (ArithmeticException e) {
                    expected = true;
                }
                require(MultiplyOverflow.multiplyOverflows(a, b) == expected,
                        "long " + a + " * " + b);
                if (a == (int) a && b == (int) b) {
                    boolean intExpected;
                    try {
                        Math.multiplyExact((int) a, (int) b);
                        intExpected = false;
                    } catch (ArithmeticException e) {
                        intExpected = true;
                    }
                    boolean actual = MultiplyOverflow.multiplyOverflows((int) a, (int) b);
                    require(actual == intExpected,
                            "int " + a + " * " + b);
                }
            }
        }
    }
}
