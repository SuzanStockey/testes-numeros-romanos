import java.util.regex.Pattern;

/** Oráculos de teste: nenhuma dependência do código ou das tabelas de RomanNumerals. */
final class RomanReference {
    private static final String[] THOUSANDS = {"", "M", "MM", "MMM"};
    private static final String[] HUNDREDS = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
    private static final String[] TENS = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
    private static final String[] UNITS = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
    private static final Pattern CANONICAL = Pattern.compile(
            "M{0,3}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})");

    private RomanReference() {
    }

    static String encode(int n) {
        return THOUSANDS[n / 1000] + HUNDREDS[n / 100 % 10] + TENS[n / 10 % 10] + UNITS[n % 10];
    }

    static boolean isCanonical(String roman) {
        return roman != null && !roman.isEmpty() && CANONICAL.matcher(roman).matches();
    }

    record Pair(int number, String roman) {
    }
}
