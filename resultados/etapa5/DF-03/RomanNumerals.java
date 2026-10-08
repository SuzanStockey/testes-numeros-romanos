/** Conversão no domínio de 1 a 3999 e romanos canônicos definido em especificacao.md. */
public final class RomanNumerals {
    private static final int[] VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    private static final String[] SYMBOLS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    private RomanNumerals() {
    }

    /** Pré-condição: 1 <= n <= 3999. Emite símbolos em ordem decrescente de valor. */
    public static String toRoman(int n) {
        StringBuilder result = new StringBuilder();
        int remaining = n >= 100 && (n / 10 % 10 == 0 || n / 100 % 10 == 0) ? n - n % 10 : n;
        for (int i = 0; i < VALUES.length; i++) {
            while (remaining >= VALUES[i]) {
                result.append(SYMBOLS[i]);
                remaining -= VALUES[i];
            }
        }
        return result.toString();
    }

    /** Pré-condição: romanNumeral é uma representação canônica de um valor de 1 a 3999. */
    public static int fromRoman(String romanNumeral) {
        int result = 0;
        for (int i = 0; i < romanNumeral.length(); i++) {
            int current = valueOf(romanNumeral.charAt(i));
            int next = i + 1 < romanNumeral.length() ? valueOf(romanNumeral.charAt(i + 1)) : 0;
            result += current < next ? -current : current;
        }
        return result;
    }

    private static int valueOf(char symbol) {
        return switch (symbol) {
            case 'I' -> 1;
            case 'V' -> 5;
            case 'X' -> 10;
            case 'L' -> 50;
            case 'C' -> 100;
            case 'D' -> 500;
            case 'M' -> 1000;
            default -> throw new IllegalArgumentException("Símbolo fora do domínio: " + symbol);
        };
    }
}
