import net.jqwik.api.*;
import net.jqwik.api.statistics.Statistics;
import static org.junit.jupiter.api.Assertions.*;

/** Geradores e seis verificações compartilhados entre as cinco sementes fixas. */
final class RomanPropertyChecks {
    private RomanPropertyChecks() {
    }

    static Arbitrary<Integer> numbers() {
        Arbitrary<Integer> uniform = Arbitraries.integers().between(1, 3999)
                .withDistribution(RandomDistribution.uniform()).withoutEdgeCases();
        Arbitrary<Integer> boundaries = Arbitraries.of(FixedCases.boundaries()).withoutEdgeCases();
        return Arbitraries.frequencyOf(Tuple.of(8, uniform), Tuple.of(2, boundaries)).withoutEdgeCases();
    }

    static Arbitrary<RomanReference.Pair> pairs() {
        // O mapeamento recompõe o romano após cada redução do inteiro.
        return numbers().map(n -> new RomanReference.Pair(n, RomanReference.encode(n))).withoutEdgeCases();
    }

    static void collect(int n) {
        int subtractions = 0;
        for (int value = n % 1000; value > 0; value /= 10) {
            if (value % 10 == 4 || value % 10 == 9) subtractions++;
        }
        Statistics.label("subtracao").collect("CE-S" + Math.min(2, subtractions));
        Statistics.label("magnitude").collect("CE-M" + (n < 10 ? 1 : n < 100 ? 2 : n < 1000 ? 3 : 4));
        Statistics.label("centro-ou-extremo").collect(switch (n) {
            case 1, 4, 9, 40, 90, 400, 900, 3999 -> String.valueOf(n);
            default -> "outros";
        });
    }

    static void p01(int n) {
        collect(n);
        String roman = RomanNumerals.toRoman(n);
        assertTrue(RomanReference.isCanonical(roman), "CT-PBT-01: saída intermediária inválida para n=" + n + ": " + roman);
        assertEquals(n, RomanNumerals.fromRoman(roman), "CT-PBT-01: n=" + n);
    }

    static void p02(RomanReference.Pair pair) {
        collect(pair.number());
        int decoded = RomanNumerals.fromRoman(pair.roman());
        // Evita chamar o codificador fora de sua pré-condição se o decodificador falhar.
        assertTrue(decoded >= 1 && decoded <= 3999, "CT-PBT-02: valor intermediário fora do domínio: " + decoded);
        assertEquals(pair.roman(), RomanNumerals.toRoman(decoded), "CT-PBT-02: " + pair);
    }

    static void p03(int n) {
        collect(n);
        String roman = RomanNumerals.toRoman(n);
        assertTrue(RomanReference.isCanonical(roman), "CT-PBT-03: n=" + n + ", saída=" + roman);
    }

    static void p04(RomanReference.Pair pair) {
        collect(pair.number());
        int decoded = RomanNumerals.fromRoman(pair.roman());
        assertTrue(decoded >= 1 && decoded <= 3999, "CT-PBT-04: " + pair + ", saída=" + decoded);
    }

    static void p05(int n) {
        collect(n);
        assertEquals(RomanReference.encode(n), RomanNumerals.toRoman(n), "CT-PBT-05: n=" + n);
    }

    static void p06(RomanReference.Pair pair) {
        collect(pair.number());
        assertEquals(pair.number(), RomanNumerals.fromRoman(pair.roman()), "CT-PBT-06: " + pair);
    }
}
