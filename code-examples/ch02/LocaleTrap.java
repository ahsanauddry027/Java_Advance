import java.util.Locale;

/** String.format follows a locale: the same call can print different characters. */
public class LocaleTrap {

    public static void main(String[] args) {
        double average = 3.5;
        int big = 1_234_567;
        // region formats
        Locale bangla = Locale.forLanguageTag("bn-BD");
        String pattern = "%.2f  %,d";
        System.out.println("bn-BD   " + String.format(bangla, pattern, average, big));
        Locale german = Locale.GERMANY;
        System.out.println("de-DE   " + String.format(german, pattern, average, big));
        Locale neutral = Locale.ROOT;
        System.out.println("ROOT    " + String.format(neutral, pattern, average, big));
        // endregion formats
    }
}
