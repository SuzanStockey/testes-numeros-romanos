/** Conversão no domínio de 1 a 3999 e romanos canônicos definido em especificacao.md. */
public final class RomanNumerals {
    private static final int[] VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    private static final String[] SYMBOLS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    private RomanNumerals() {
    }

    /** Emite símbolos em ordem decrescente; rejeita valores fora de 1..3999. */
    public static String toRoman(int n) {
        if (n < 1 || n > 3999) {
            throw new IllegalArgumentException("O inteiro deve estar entre 1 e 3999.");
        }
        StringBuilder result = new StringBuilder();
        int remaining = n;
        for (int i = 0; i < VALUES.length; i++) {
            while (remaining >= VALUES[i]) {
                result.append(SYMBOLS[i]);
                remaining -= VALUES[i];
            }
        }
        return result.toString();
    }

    /** Rejeita null, texto vazio e representações não canônicas, sem normalização. */
    public static int fromRoman(String romanNumeral) {
        // O maior comprimento canônico no domínio é 15 (3888 = MMMDCCCLXXXVIII).
        if (romanNumeral == null || romanNumeral.isEmpty() || romanNumeral.length() > 15) {
            throw new IllegalArgumentException("Informe um romano canônico de 1 a 3999.");
        }
        int result = 0;
        for (int i = 0; i < romanNumeral.length(); i++) {
            int current = valueOf(romanNumeral.charAt(i));
            int next = i + 1 < romanNumeral.length() ? valueOf(romanNumeral.charAt(i + 1)) : 0;
            result += current < next ? -current : current;
        }
        // A reconstrução evita aceitar somas possíveis com uma grafia inválida, como IC.
        if (result < 1 || result > 3999 || !romanNumeral.equals(toRoman(result))) {
            throw new IllegalArgumentException("A representação romana não é canônica.");
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
